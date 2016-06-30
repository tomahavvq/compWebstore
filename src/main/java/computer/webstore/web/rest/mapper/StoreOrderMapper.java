package computer.webstore.web.rest.mapper;

import computer.webstore.domain.*;
import computer.webstore.web.rest.dto.StoreOrderDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StoreOrder and its DTO StoreOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface StoreOrderMapper {

    @Mapping(source = "user.id", target = "userId")
    StoreOrderDTO storeOrderToStoreOrderDTO(StoreOrder storeOrder);

    List<StoreOrderDTO> storeOrdersToStoreOrderDTOs(List<StoreOrder> storeOrders);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "orderDetails", ignore = true)
    StoreOrder storeOrderDTOToStoreOrder(StoreOrderDTO storeOrderDTO);

    List<StoreOrder> storeOrderDTOsToStoreOrders(List<StoreOrderDTO> storeOrderDTOs);
}
