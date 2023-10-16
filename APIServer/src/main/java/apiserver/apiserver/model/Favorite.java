package apiserver.apiserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class Favorite {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favouriteId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Favorite() {
    	
    }
    
    public Favorite(User user, Product product) {
    	this.user = user;
    	this.product = product;
    }

	public Long getFavouriteId() {
		return favouriteId;
	}

	public Product getProduct() {
		return product;
	}

	public User getUser() {
		return user;
	}

	public void setFavouriteId(Long favouriteId) {
		this.favouriteId = favouriteId;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setUser(User user) {
		this.user = user;
	}

    
    
}