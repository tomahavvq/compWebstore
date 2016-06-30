package computer.webstore.service;

import computer.webstore.domain.StoreOrder;
import computer.webstore.repository.StoreOrderRepository;
import computer.webstore.web.rest.dto.StoreOrderDTO;
import computer.webstore.web.rest.mapper.StoreOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StoreOrder.
 */
@Service
@Transactional
public class StoreOrderService {

    private final Logger log = LoggerFactory.getLogger(StoreOrderService.class);
    
    @Inject
    private StoreOrderRepository storeOrderRepository;
    
    @Inject
    private StoreOrderMapper storeOrderMapper;
    
    /**
     * Save a storeOrder.
     * 
     * @param storeOrderDTO the entity to save
     * @return the persisted entity
     */
    public StoreOrderDTO save(StoreOrderDTO storeOrderDTO) {
        log.debug("Request to save StoreOrder : {}", storeOrderDTO);
        StoreOrder storeOrder = storeOrderMapper.storeOrderDTOToStoreOrder(storeOrderDTO);
        storeOrder = storeOrderRepository.save(storeOrder);
        StoreOrderDTO result = storeOrderMapper.storeOrderToStoreOrderDTO(storeOrder);
        return result;
    }

    /**
     *  Get all the storeOrders.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<StoreOrderDTO> findAll() {
        log.debug("Request to get all StoreOrders");
        List<StoreOrderDTO> result = storeOrderRepository.findAll().stream()
            .map(storeOrderMapper::storeOrderToStoreOrderDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one storeOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public StoreOrderDTO findOne(Long id) {
        log.debug("Request to get StoreOrder : {}", id);
        StoreOrder storeOrder = storeOrderRepository.findOne(id);
        StoreOrderDTO storeOrderDTO = storeOrderMapper.storeOrderToStoreOrderDTO(storeOrder);
        return storeOrderDTO;
    }

    /**
     *  Delete the  storeOrder by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StoreOrder : {}", id);
        storeOrderRepository.delete(id);
    }
}
