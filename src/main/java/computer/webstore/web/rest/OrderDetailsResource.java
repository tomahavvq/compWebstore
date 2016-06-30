package computer.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import computer.webstore.domain.OrderDetails;
import computer.webstore.service.OrderDetailsService;
import computer.webstore.web.rest.util.HeaderUtil;
import computer.webstore.web.rest.util.PaginationUtil;
import computer.webstore.web.rest.dto.OrderDetailsDTO;
import computer.webstore.web.rest.mapper.OrderDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing OrderDetails.
 */
@RestController
@RequestMapping("/api")
public class OrderDetailsResource {

    private final Logger log = LoggerFactory.getLogger(OrderDetailsResource.class);
        
    @Inject
    private OrderDetailsService orderDetailsService;
    
    @Inject
    private OrderDetailsMapper orderDetailsMapper;
    
    /**
     * POST  /order-details : Create a new orderDetails.
     *
     * @param orderDetailsDTO the orderDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderDetailsDTO, or with status 400 (Bad Request) if the orderDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-details",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderDetailsDTO> createOrderDetails(@RequestBody OrderDetailsDTO orderDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save OrderDetails : {}", orderDetailsDTO);
        if (orderDetailsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderDetails", "idexists", "A new orderDetails cannot already have an ID")).body(null);
        }
        OrderDetailsDTO result = orderDetailsService.save(orderDetailsDTO);
        return ResponseEntity.created(new URI("/api/order-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-details : Updates an existing orderDetails.
     *
     * @param orderDetailsDTO the orderDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderDetailsDTO,
     * or with status 400 (Bad Request) if the orderDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderDetailsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-details",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderDetailsDTO> updateOrderDetails(@RequestBody OrderDetailsDTO orderDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update OrderDetails : {}", orderDetailsDTO);
        if (orderDetailsDTO.getId() == null) {
            return createOrderDetails(orderDetailsDTO);
        }
        OrderDetailsDTO result = orderDetailsService.save(orderDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderDetails", orderDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-details : get all the orderDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDetails in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-details",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderDetailsDTO>> getAllOrderDetails(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderDetails");
        Page<OrderDetails> page = orderDetailsService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-details");
        return new ResponseEntity<>(orderDetailsMapper.orderDetailsToOrderDetailsDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-details/:id : get the "id" orderDetails.
     *
     * @param id the id of the orderDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDetailsDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-details/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderDetailsDTO> getOrderDetails(@PathVariable Long id) {
        log.debug("REST request to get OrderDetails : {}", id);
        OrderDetailsDTO orderDetailsDTO = orderDetailsService.findOne(id);
        return Optional.ofNullable(orderDetailsDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-details/:id : delete the "id" orderDetails.
     *
     * @param id the id of the orderDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-details/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderDetails(@PathVariable Long id) {
        log.debug("REST request to delete OrderDetails : {}", id);
        orderDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderDetails", id.toString())).build();
    }

}
