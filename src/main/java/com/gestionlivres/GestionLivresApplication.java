package com.gestionlivres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import provider.ZeroMqProvider;

@SpringBootApplication
public class GestionLivresApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionLivresApplication.class, args);
		try {
			Context context = ZMQ.context(1);;
			Socket input = context.socket(SocketType.REP);
			input.bind("ipc://5000");
            while (!Thread.currentThread().isInterrupted()) {
            	ZeroMqProvider receivedMessage = ZeroMqProvider.recvMsg(input);
    			System.err.println("le message reçu est : " + receivedMessage);
    			RestTemplate restTemplate = new RestTemplate();
    			String booklist = restTemplate.getForObject("http://localhost:9091/livres/1", String.class);
    			ZeroMqProvider.newStringMsg(booklist).send(input);
    			System.err.println("le message envoyé est : "+ booklist);
            }
		}catch (Exception e) {
			System.err.println("pffffffffff!!! " + e.getMessage());
		}
	}

}
