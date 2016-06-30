package computer.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import computer.webstore.domain.StoreOrder;
import computer.webstore.service.StoreOrderService;
import computer.webstore.web.rest.util.HeaderUtil;
import computer.webstore.web.rest.dto.StoreOrderDTO;
import computer.webstore.web.rest.mapper.StoreOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing StoreOrder.
 */
@RestController
@RequestMapping("/api")
public class StoreOrderResource {

    private final Logger log = LoggerFactory.getLogger(StoreOrderResource.class);
        
    @Inject
    private StoreOrderService storeOrderService;
    
    @Inject
    private StoreOrderMapper storeOrderMapper;
    
    /**
     * POST  /store-orders : Create a new storeOrder.
     *
     * @param storeOrderDTO the storeOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeOrderDTO, or with status 400 (Bad Request) if the storeOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/store-orders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreOrderDTO> createStoreOrder(@RequestBody StoreOrderDTO storeOrderDTO) throws URISyntaxException {
        log.debug("REST request to save StoreOrder : {}", storeOrderDTO);
        if (storeOrderDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("storeOrder", "idexists", "A new storeOrder cannot already have an ID")).body(null);
        }
        StoreOrderDTO result = storeOrderService.save(storeOrderDTO);
        return ResponseEntity.created(new URI("/api/store-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("storeOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-orders : Updates an existing storeOrder.
     *
     * @param storeOrderDTO the storeOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeOrderDTO,
     * or with status 400 (Bad Request) if the storeOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the storeOrderDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/store-orders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreOrderDTO> updateStoreOrder(@RequestBody StoreOrderDTO storeOrderDTO) throws URISyntaxException {
        log.debug("REST request to update StoreOrder : {}", storeOrderDTO);
        if (storeOrderDTO.getId() == null) {
            return createStoreOrder(storeOrderDTO);
        }
        StoreOrderDTO result = storeOrderService.save(storeOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("storeOrder", storeOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-orders : get all the storeOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of storeOrders in body
     */
    @RequestMapping(value = "/store-orders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<StoreOrderDTO> getAllStoreOrders() {
        log.debug("REST request to get all StoreOrders");
        return storeOrderService.findAll();
    }

    /**
     * GET  /store-orders/:id : get the "id" storeOrder.
     *
     * @param id the id of the storeOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeOrderDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/store-orders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StoreOrderDTO> getStoreOrder(@PathVariable Long id) {
        log.debug("REST request to get StoreOrder : {}", id);
        StoreOrderDTO storeOrderDTO = storeOrderService.findOne(id);
        return Optional.ofNullable(storeOrderDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /store-orders/:id : delete the "id" storeOrder.
     *
     * @param id the id of the storeOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/store-orders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStoreOrder(@PathVariable Long id) {
        log.debug("REST request to delete StoreOrder : {}", id);
        storeOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("storeOrder", id.toString())).build();
    }

}
