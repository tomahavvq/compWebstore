package computer.webstore.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ProductDetails entity.
 */
public class ProductDetailsDTO implements Serializable {

    private Long id;

    private String details;

    private String attributes;

    private String productionYear;


    private Long productId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
    public String getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear = productionYear;
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

        ProductDetailsDTO productDetailsDTO = (ProductDetailsDTO) o;

        if ( ! Objects.equals(id, productDetailsDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductDetailsDTO{" +
            "id=" + id +
            ", details='" + details + "'" +
            ", attributes='" + attributes + "'" +
            ", productionYear='" + productionYear + "'" +
            '}';
    }
}
