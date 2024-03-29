package provider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class ZeroMqProvider  implements Deque<ZFrame> {
	
	/**
     * Hold internal list of ZFrame objects
     */
    private final ArrayDeque<ZFrame> frames;

    /**
     * Class Constructor
     */
    public ZeroMqProvider ()
    {
        this.frames = new ArrayDeque<ZFrame>(4);
    }

    /**
     * Destructor. Explicitly destroys all ZFrames contains in the ZMsg
     */
    public void destroy()
    {
        if (frames == null)
            return;
        for (ZFrame f : frames) {
            f.destroy();
        }
        frames.clear();
    }

    /**
     * Return total number of bytes contained in all ZFrames in this ZMsg
     * @return
     */
    public long contentSize()
    {
        long size = 0;
        for (ZFrame f : frames) {
            size += f.size();
        }
        return size;
    }

    /**
     * Add a String as a new ZFrame to the end of list
     * @param str
     *            String to add to list
     */
    public void addString(String str)
    {
        this.add(str);
    }

    /**
     * Creates copy of this ZMsg. Also duplicates all frame content.
     * @return The duplicated ZMsg object, else null if this ZMsg contains an
     *         empty frame set
     */
    public ZeroMqProvider duplicate()
    {
        ZeroMqProvider msg = new ZeroMqProvider();
        for (ZFrame f : frames)
            msg.add(f.duplicate());
        return msg;
    }

    /**
     * Push frame plus empty frame to front of message, before 1st frame.
     * Message takes ownership of frame, will destroy it when message is sent.
     * @param frame
     */
    public void wrap(ZFrame frame)
    {
        if (frame != null) {
            push(new ZFrame(""));
            push(frame);
        }
    }

    /**
     * Pop frame off front of message, caller now owns frame. If next frame is
     * empty, pops and destroys that empty frame (e.g. useful when unwrapping
     * ROUTER socket envelopes)
     * @return Unwrapped frame
     */
    public ZFrame unwrap()
    {
        if (size() == 0)
            return null;
        ZFrame f = pop();
        ZFrame empty = getFirst();
        if (empty.hasData() && empty.size() == 0) {
            empty = pop();
            empty.destroy();
        }
        return f;
    }

    /**
     * Send message to 0MQ socket.
     * @param socket
     *            0MQ socket to send ZMsg on.
     */
    public void send(Socket socket)
    {
        send(socket, false);
    }

    /**
     * Send message to 0MQ socket, destroys contents after sending if destroy
     * param is set to true. If the message has no frames, sends nothing but
     * still destroy()s the ZMsg object
     * @param socket
     *            0MQ socket to send ZMsg on.
     */
    public void send(Socket socket, boolean destroy)
    {
        if (socket == null)
            throw new IllegalArgumentException("socket is null");
        if (frames.size() == 0)
            return;
        Iterator<ZFrame> i = frames.iterator();
        while (i.hasNext()) {
            ZFrame f = i.next();
            f.send(socket, (i.hasNext()) ? ZMQ.SNDMORE : 0);
        }
        if (destroy) {
            destroy();
        }
    }

    /**
     * Receives message from socket, returns ZMsg object or null if the recv was
     * interrupted. Does a blocking recv, if you want not to block then use the
     * ZLoop class or ZMQ.Poller to check for socket input before receiving or
     * recvMsg with flag ZMQ.DONTWAIT.
     * @param socket
     * @return
     */
    public static ZeroMqProvider recvMsg(Socket socket)
    {
        return recvMsg(socket, 0);
    }

    /**
     * Receives message from socket, returns ZMsg object or null if the recv was
     * interrupted. Does a blocking recv, if you want not to block then use the
     * ZLoop class or ZMQ.Poller to check for socket input before receiving.
     * @param socket
     * @param flag
     *            see ZMQ constants
     * @return
     */
    public static ZeroMqProvider recvMsg(Socket socket, int flag)
    {
        if (socket == null)
            throw new IllegalArgumentException("socket is null");

        ZeroMqProvider msg = new ZeroMqProvider();

        while (true) {
            ZFrame f = ZFrame.recvFrame(socket, flag);
            if (f == null || !f.hasData()) {
                // If receive failed or was interrupted
                msg.destroy();
                msg = null;
                break;
            }
            msg.add(f);
            if (!f.hasMore())
                break;
        }
        return msg;
    }

    /**
     * Save message to an open data output stream. Data saved as: 4 bytes:
     * number of frames For every frame: 4 bytes: byte size of frame data + n
     * bytes: frame byte data
     * @param msg
     *            ZMsg to save
     * @param file
     *            DataOutputStream
     * @return True if saved OK, else false
     */
    public static boolean save(ZeroMqProvider msg, DataOutputStream file)
    {
        if (msg == null)
            return false;

        try {
            // Write number of frames
            file.writeInt(msg.size());
            if (msg.size() > 0) {
                for (ZFrame f : msg) {
                    // Write byte size of frame
                    file.writeInt(f.size());
                    // Write frame byte data
                    file.write(f.getData());
                }
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Load / append a ZMsg from an open DataInputStream
     * @param file
     *            DataInputStream connected to file
     * @return ZMsg object
     */
    public static ZeroMqProvider load(DataInputStream file)
    {
        if (file == null)
            return null;
        ZeroMqProvider rcvMsg = new ZeroMqProvider();

        try {
            int msgSize = file.readInt();
            if (msgSize > 0) {
                int msgNbr = 0;
                while (++msgNbr <= msgSize) {
                    int frameSize = file.readInt();
                    byte[] data = new byte[frameSize];
                    file.read(data);
                    rcvMsg.add(new ZFrame(data));
                }
            }
            return rcvMsg;
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Create a new ZMsg from one or more Strings
     * @param strings
     *            Strings to add as frames.
     * @return ZMsg object
     */
    public static ZeroMqProvider newStringMsg(String... strings)
    {
        ZeroMqProvider msg = new ZeroMqProvider();
        for (String data : strings) {
            msg.addString(data);
        }
        return msg;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ZeroMqProvider zMsg = (ZeroMqProvider) o;

        if (frames == null || zMsg.frames == null)
            return false;

        // based on AbstractList
        Iterator<ZFrame> e1 = frames.iterator();
        Iterator<ZFrame> e2 = zMsg.frames.iterator();
        while (e1.hasNext() && e2.hasNext()) {
            ZFrame o1 = e1.next();
            ZFrame o2 = e2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    @Override
    public int hashCode()
    {
        if (frames == null || frames.size() == 0)
            return 0;

        int result = 1;
        for (ZFrame frame : frames)
            result = 31 * result + (frame == null ? 0 : frame.hashCode());

        return result;
    }

    /**
     * Dump the message in human readable format. This should only be used for
     * debugging and tracing, inefficient in handling large messages.
     **/
    public void dump(Appendable out)
    {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.printf("--------------------------------------\n");
            for (ZFrame frame : frames) {
                pw.printf("[%03d] %s\n", frame.getData().length, frame.toString());
            }
            out.append(sw.getBuffer());
            sw.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Message dump exception " + super.toString(), e);
        }
    }

    /**
     * Convert the message to a string, for use in debugging.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        dump(sb);
        return sb.toString();
    }

    // ********* Convenience Deque methods for common data types *** //

    public void addFirst(String stringValue)
    {
        addFirst(new ZFrame(stringValue));
    }

    public void addFirst(byte[] data)
    {
        addFirst(new ZFrame(data));
    }

    public void addLast(String stringValue)
    {
        addLast(new ZFrame(stringValue));
    }

    public void addLast(byte[] data)
    {
        addLast(new ZFrame(data));
    }

    // ********* Convenience Queue methods for common data types *** //

    public void push(String str)
    {
        push(new ZFrame(str));
    }

    public void push(byte[] data)
    {
        push(new ZFrame(data));
    }

    public boolean add(String stringValue)
    {
        return add(new ZFrame(stringValue));
    }

    public boolean add(byte[] data)
    {
        return add(new ZFrame(data));
    }

    // ********* Implement Iterable Interface *************** //
    @Override
    public Iterator<ZFrame> iterator()
    {
        // TODO Auto-generated method stub
        return frames.iterator();
    }

    // ********* Implement Deque Interface ****************** //
    @Override
    public boolean addAll(Collection<? extends ZFrame> arg0)
    {
        return frames.addAll(arg0);
    }

    @Override
    public void clear()
    {
        frames.clear();

    }

    @Override
    public boolean containsAll(Collection<?> arg0)
    {
        return frames.containsAll(arg0);
    }

    @Override
    public boolean isEmpty()
    {
        return frames.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> arg0)
    {
        return frames.removeAll(arg0);
    }

    @Override
    public boolean retainAll(Collection<?> arg0)
    {
        return frames.retainAll(arg0);
    }

    @Override
    public Object[] toArray()
    {
        return frames.toArray();
    }

    @Override
    public <T> T[] toArray(T[] arg0)
    {
        return frames.toArray(arg0);
    }

    @Override
    public boolean add(ZFrame e)
    {
        return frames.add(e);
    }

    @Override
    public void addFirst(ZFrame e)
    {
        frames.addFirst(e);
    }

    @Override
    public void addLast(ZFrame e)
    {
        frames.addLast(e);
    }

    @Override
    public boolean contains(Object o)
    {
        return frames.contains(o);
    }

    @Override
    public Iterator<ZFrame> descendingIterator()
    {
        return frames.descendingIterator();
    }

    @Override
    public ZFrame element()
    {
        return frames.element();
    }

    @Override
    public ZFrame getFirst()
    {
        try {
            return frames.getFirst();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public ZFrame getLast()
    {
        try {
            return frames.getLast();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public boolean offer(ZFrame e)
    {
        return frames.offer(e);
    }

    @Override
    public boolean offerFirst(ZFrame e)
    {
        return frames.offerFirst(e);
    }

    @Override
    public boolean offerLast(ZFrame e)
    {
        return frames.offerLast(e);
    }

    @Override
    public ZFrame peek()
    {
        return frames.peek();
    }

    @Override
    public ZFrame peekFirst()
    {
        try {
            return frames.peekFirst();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public ZFrame peekLast()
    {
        try {
            return frames.peekLast();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public ZFrame poll()
    {
        return frames.poll();
    }

    @Override
    public ZFrame pollFirst()
    {
        return frames.pollFirst();
    }

    @Override
    public ZFrame pollLast()
    {
        return frames.pollLast();
    }

    @Override
    public ZFrame pop()
    {
        try {
            return frames.pop();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Pop a ZFrame and return the toString() representation of it.
     * @return toString version of pop'ed frame, or null if no frame exists.
     */
    public String popString()
    {
        ZFrame frame = pop();
        if (frame == null)
            return null;

        return frame.toString();
    }

    @Override
    public void push(ZFrame e)
    {
        frames.push(e);
    }

    @Override
    public ZFrame remove()
    {
        return frames.remove();
    }

    @Override
    public boolean remove(Object o)
    {
        return frames.remove(o);
    }

    @Override
    public ZFrame removeFirst()
    {
        try {
            return frames.removeFirst();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o)
    {
        return frames.removeFirstOccurrence(o);
    }

    @Override
    public ZFrame removeLast()
    {
        try {
            return frames.removeLast();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public boolean removeLastOccurrence(Object o)
    {
        return frames.removeLastOccurrence(o);
    }

    @Override
    public int size()
    {
        return frames.size();
    }

}
