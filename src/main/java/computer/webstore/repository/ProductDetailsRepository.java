package computer.webstore.repository;

import computer.webstore.domain.ProductDetails;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductDetails entity.
 */
@SuppressWarnings("unused")
public interface ProductDetailsRepository extends JpaRepository<ProductDetails,Long> {

}
