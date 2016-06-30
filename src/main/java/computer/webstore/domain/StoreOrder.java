package computer.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StoreOrder.
 */
@Entity
@Table(name = "store_order")
public class StoreOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_amount")
    private Integer productAmount;

    @Column(name = "total_sum")
    private Double totalSum;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "storeOrder")
    @JsonIgnore
    private Set<OrderDetails> orderDetails = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreOrder storeOrder = (StoreOrder) o;
        if(storeOrder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storeOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StoreOrder{" +
            "id=" + id +
            ", productAmount='" + productAmount + "'" +
            ", totalSum='" + totalSum + "'" +
            '}';
    }
}
