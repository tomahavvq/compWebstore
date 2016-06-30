package computer.webstore.web.rest.mapper;

import computer.webstore.domain.*;
import computer.webstore.web.rest.dto.OrderDetailsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OrderDetails and its DTO OrderDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderDetailsMapper {

    @Mapping(source = "storeOrder.id", target = "storeOrderId")
    @Mapping(source = "product.id", target = "productId")
    OrderDetailsDTO orderDetailsToOrderDetailsDTO(OrderDetails orderDetails);

    List<OrderDetailsDTO> orderDetailsToOrderDetailsDTOs(List<OrderDetails> orderDetails);

    @Mapping(source = "storeOrderId", target = "storeOrder")
    @Mapping(source = "productId", target = "product")
    OrderDetails orderDetailsDTOToOrderDetails(OrderDetailsDTO orderDetailsDTO);

    List<OrderDetails> orderDetailsDTOsToOrderDetails(List<OrderDetailsDTO> orderDetailsDTOs);

    default StoreOrder storeOrderFromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setId(id);
        return storeOrder;
    }

    default Product productFromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
