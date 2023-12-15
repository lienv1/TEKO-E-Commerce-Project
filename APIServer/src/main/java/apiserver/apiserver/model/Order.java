package apiserver.apiserver.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "orders") // "orders" is used as the table's name, it can be any non-reserved name
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Comment out it order needs to be saved manually
	private Long orderId;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "userId")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<OrderDetail>();

	private String comment;

	private Date orderDate;

	private Timestamp created;

	@JsonIgnore
	private Boolean deleted;

//Comment out it order needs to be saved manually
	@PrePersist
	protected void onCreate() {
		created = new Timestamp(System.currentTimeMillis());
	}

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

	public String getComment() {
		return comment;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public Timestamp getCreated() {
		return created;
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

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void addOrderDetail(OrderDetail orderDetail) {
		orderDetails.add(orderDetail);
		orderDetail.setOrder(this);
	}

	public void removeOrderDetail(OrderDetail orderDetail) {
		orderDetails.remove(orderDetail);
		orderDetail.setOrder(null);
	}

}
