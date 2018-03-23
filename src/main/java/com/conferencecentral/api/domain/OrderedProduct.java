package com.conferencecentral.api.domain;

public class OrderedProduct {
//	private String id;
	private long qty;
//	private Product product;
	
	@SuppressWarnings("unused")
	private OrderedProduct() {}
	
	public OrderedProduct(long qty) {
//		this.id = id;
		this.qty = qty;
	}

//	public String getId() {
//		return id;
//	}

	public long getQty() {
		return qty;
	}
//
//	public Product getProduct() {
//		return product;
//	}
//
//	public void setProduct(Product product) {
//		this.product = product;
//	}
	
}
