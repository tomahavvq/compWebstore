package computer.webstore.service;

import computer.webstore.domain.OrderDetails;
import computer.webstore.repository.OrderDetailsRepository;
import computer.webstore.web.rest.dto.OrderDetailsDTO;
import computer.webstore.web.rest.mapper.OrderDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing OrderDetails.
 */
@Service
@Transactional
public class OrderDetailsService {

    private final Logger log = LoggerFactory.getLogger(OrderDetailsService.class);
    
    @Inject
    private OrderDetailsRepository orderDetailsRepository;
    
    @Inject
    private OrderDetailsMapper orderDetailsMapper;
    
    /**
     * Save a orderDetails.
     * 
     * @param orderDetailsDTO the entity to save
     * @return the persisted entity
     */
    public OrderDetailsDTO save(OrderDetailsDTO orderDetailsDTO) {
        log.debug("Request to save OrderDetails : {}", orderDetailsDTO);
        OrderDetails orderDetails = orderDetailsMapper.orderDetailsDTOToOrderDetails(orderDetailsDTO);
        orderDetails = orderDetailsRepository.save(orderDetails);
        OrderDetailsDTO result = orderDetailsMapper.orderDetailsToOrderDetailsDTO(orderDetails);
        return result;
    }

    /**
     *  Get all the orderDetails.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OrderDetails> findAll(Pageable pageable) {
        log.debug("Request to get all OrderDetails");
        Page<OrderDetails> result = orderDetailsRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one orderDetails by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OrderDetailsDTO findOne(Long id) {
        log.debug("Request to get OrderDetails : {}", id);
        OrderDetails orderDetails = orderDetailsRepository.findOne(id);
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.orderDetailsToOrderDetailsDTO(orderDetails);
        return orderDetailsDTO;
    }

    /**
     *  Delete the  orderDetails by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderDetails : {}", id);
        orderDetailsRepository.delete(id);
    }
}
