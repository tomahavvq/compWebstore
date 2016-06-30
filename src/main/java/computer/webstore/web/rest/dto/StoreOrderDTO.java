package computer.webstore.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the StoreOrder entity.
 */
public class StoreOrderDTO implements Serializable {

    private Long id;

    private Integer productAmount;

    private Double totalSum;


    private Long userId;
    
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StoreOrderDTO storeOrderDTO = (StoreOrderDTO) o;

        if ( ! Objects.equals(id, storeOrderDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StoreOrderDTO{" +
            "id=" + id +
            ", productAmount='" + productAmount + "'" +
            ", totalSum='" + totalSum + "'" +
            '}';
    }
}
