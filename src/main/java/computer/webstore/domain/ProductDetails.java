package computer.webstore.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductDetails.
 */
@Entity
@Table(name = "product_details")
public class ProductDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "details")
    private String details;

    @Column(name = "attributes")
    private String attributes;

    @Column(name = "production_year")
    private String productionYear;

    @OneToOne
    @JoinColumn(unique = true)
    private Product product;

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
        ProductDetails productDetails = (ProductDetails) o;
        if(productDetails.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productDetails.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
            "id=" + id +
            ", details='" + details + "'" +
            ", attributes='" + attributes + "'" +
            ", productionYear='" + productionYear + "'" +
            '}';
    }
}
