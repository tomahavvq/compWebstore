package computer.webstore.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the OrderDetails entity.
 */
public class OrderDetailsDTO implements Serializable {

    private Long id;

    private Double sum;

    private Integer quantity;


    private Long storeOrderId;
    
    private Long productId;
    
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

    public Long getStoreOrderId() {
        return storeOrderId;
    }

    public void setStoreOrderId(Long storeOrderId) {
        this.storeOrderId = storeOrderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderDetailsDTO orderDetailsDTO = (OrderDetailsDTO) o;

        if ( ! Objects.equals(id, orderDetailsDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderDetailsDTO{" +
            "id=" + id +
            ", sum='" + sum + "'" +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
