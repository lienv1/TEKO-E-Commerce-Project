package apiserver.apiserver.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_address_id")
	private Address deliveryAddress;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "billing_address_id")
	private Address billingAddress;

	private String username;
	private boolean isBusiness;
	private boolean isDeleted;
}