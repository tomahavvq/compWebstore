package computer.webstore.web.rest.mapper;

import computer.webstore.domain.*;
import computer.webstore.web.rest.dto.ProductDetailsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProductDetails and its DTO ProductDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductDetailsMapper {

    @Mapping(source = "product.id", target = "productId")
    ProductDetailsDTO productDetailsToProductDetailsDTO(ProductDetails productDetails);

    List<ProductDetailsDTO> productDetailsToProductDetailsDTOs(List<ProductDetails> productDetails);

    @Mapping(source = "productId", target = "product")
    ProductDetails productDetailsDTOToProductDetails(ProductDetailsDTO productDetailsDTO);

    List<ProductDetails> productDetailsDTOsToProductDetails(List<ProductDetailsDTO> productDetailsDTOs);

    default Product productFromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
