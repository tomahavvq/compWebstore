package computer.webstore.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrderDetails.
 */
@Entity
@Table(name = "order_details")
public class OrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sum")
    private Double sum;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    private StoreOrder storeOrder;

    @ManyToOne
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public StoreOrder getStoreOrder() {
        return storeOrder;
    }

    public void setStoreOrder(StoreOrder storeOrder) {
        this.storeOrder = storeOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDetails orderDetails = (OrderDetails) o;
        if(orderDetails.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderDetails.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
            "id=" + id +
            ", sum='" + sum + "'" +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
