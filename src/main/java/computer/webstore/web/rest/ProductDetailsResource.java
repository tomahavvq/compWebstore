package computer.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import computer.webstore.domain.ProductDetails;
import computer.webstore.service.ProductDetailsService;
import computer.webstore.web.rest.util.HeaderUtil;
import computer.webstore.web.rest.dto.ProductDetailsDTO;
import computer.webstore.web.rest.mapper.ProductDetailsMapper;
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
 * REST controller for managing ProductDetails.
 */
@RestController
@RequestMapping("/api")
public class ProductDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ProductDetailsResource.class);
        
    @Inject
    private ProductDetailsService productDetailsService;
    
    @Inject
    private ProductDetailsMapper productDetailsMapper;
    
    /**
     * POST  /product-details : Create a new productDetails.
     *
     * @param productDetailsDTO the productDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productDetailsDTO, or with status 400 (Bad Request) if the productDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-details",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductDetailsDTO> createProductDetails(@RequestBody ProductDetailsDTO productDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save ProductDetails : {}", productDetailsDTO);
        if (productDetailsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productDetails", "idexists", "A new productDetails cannot already have an ID")).body(null);
        }
        ProductDetailsDTO result = productDetailsService.save(productDetailsDTO);
        return ResponseEntity.created(new URI("/api/product-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-details : Updates an existing productDetails.
     *
     * @param productDetailsDTO the productDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productDetailsDTO,
     * or with status 400 (Bad Request) if the productDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the productDetailsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-details",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductDetailsDTO> updateProductDetails(@RequestBody ProductDetailsDTO productDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update ProductDetails : {}", productDetailsDTO);
        if (productDetailsDTO.getId() == null) {
            return createProductDetails(productDetailsDTO);
        }
        ProductDetailsDTO result = productDetailsService.save(productDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productDetails", productDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-details : get all the productDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productDetails in body
     */
    @RequestMapping(value = "/product-details",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ProductDetailsDTO> getAllProductDetails() {
        log.debug("REST request to get all ProductDetails");
        return productDetailsService.findAll();
    }

    /**
     * GET  /product-details/:id : get the "id" productDetails.
     *
     * @param id the id of the productDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productDetailsDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/product-details/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductDetailsDTO> getProductDetails(@PathVariable Long id) {
        log.debug("REST request to get ProductDetails : {}", id);
        ProductDetailsDTO productDetailsDTO = productDetailsService.findOne(id);
        return Optional.ofNullable(productDetailsDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /product-details/:id : delete the "id" productDetails.
     *
     * @param id the id of the productDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/product-details/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductDetails(@PathVariable Long id) {
        log.debug("REST request to delete ProductDetails : {}", id);
        productDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productDetails", id.toString())).build();
    }

}
