package apiserver.apiserver.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@Column(unique = true, nullable = true)
	private Long erpId;
	private String company;
	private String firstname;
	private String lastname;
	private String phone;
	private String email;

	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="street", column=@Column(name="delivery_street")),
	    @AttributeOverride(name="city", column=@Column(name="delivery_city")),
	    @AttributeOverride(name="state", column=@Column(name="delivery_state")),
	    @AttributeOverride(name="postalCode", column=@Column(name="delivery_postalCode")),
	    @AttributeOverride(name="country", column=@Column(name="delivery_country"))
	})
	private Address deliveryAddress;
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="street", column=@Column(name="billing_street")),
	    @AttributeOverride(name="city", column=@Column(name="billing_city")),
	    @AttributeOverride(name="state", column=@Column(name="billing_state")),
	    @AttributeOverride(name="postalCode", column=@Column(name="billing_postalCode")),
	    @AttributeOverride(name="country", column=@Column(name="billing_country"))
	})
	private Address billingAddress;

	@Column(unique = true, nullable = false)
	private String username;
	private boolean business;
	private boolean deleted;
	
	public User() {
		
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getErpId() {
		return erpId;
	}

	public void setErpId(Long erpId) {
		this.erpId = erpId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public Address getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isBusiness() {
		return business;
	}
	public void setBusiness(boolean business) {
		this.business = business;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", erpId=" + erpId + ", company=" + company + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", phone=" + phone + ", email=" + email + ", deliveryAddress="
				+ deliveryAddress + ", billingAddress=" + billingAddress + ", username=" + username + ", business="
				+ business + ", deleted=" + deleted + "]";
	}	
	
}