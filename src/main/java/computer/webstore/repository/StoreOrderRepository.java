package computer.webstore.repository;

import computer.webstore.domain.StoreOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StoreOrder entity.
 */
@SuppressWarnings("unused")
public interface StoreOrderRepository extends JpaRepository<StoreOrder,Long> {

    @Query("select storeOrder from StoreOrder storeOrder where storeOrder.user.login = ?#{principal.username}")
    List<StoreOrder> findByUserIsCurrentUser();

}
