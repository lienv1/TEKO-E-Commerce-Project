package apiserver.apiserver.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails;

	private Date orderDate;
	
	private Timestamp orderCreated;

	private Boolean deleted;

	public Order() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public User getUser() {
		return user;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public Timestamp getOrderCreated() {
		return orderCreated;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderCreated(Timestamp orderCreated) {
		this.orderCreated = orderCreated;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
