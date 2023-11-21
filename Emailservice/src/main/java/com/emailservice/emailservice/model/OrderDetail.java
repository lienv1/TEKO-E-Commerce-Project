package com.emailservice.emailservice.model;

public class OrderDetail {
	
    private Long id;
    private Order order;
    private Product product;
    private Integer quantity;
    
	public OrderDetail() {
	}
	
	public Long getId() {
		return id;
	}
	public Order getOrder() {
		return order;
	}
	public Product getProduct() {
		return product;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

    
}
