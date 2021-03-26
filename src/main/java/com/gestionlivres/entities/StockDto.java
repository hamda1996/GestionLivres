package com.gestionlivres.entities;

public class StockDto {

	private long productId;
	private int quantityToReduce;
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getQuantityToReduce() {
		return quantityToReduce;
	}
	public void setQuantityToReduce(int quantityToReduce) {
		this.quantityToReduce = quantityToReduce;
	}
	
}
