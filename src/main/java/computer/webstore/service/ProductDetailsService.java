package computer.webstore.service;

import computer.webstore.domain.ProductDetails;
import computer.webstore.repository.ProductDetailsRepository;
import computer.webstore.web.rest.dto.ProductDetailsDTO;
import computer.webstore.web.rest.mapper.ProductDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProductDetails.
 */
@Service
@Transactional
public class ProductDetailsService {

    private final Logger log = LoggerFactory.getLogger(ProductDetailsService.class);
    
    @Inject
    private ProductDetailsRepository productDetailsRepository;
    
    @Inject
    private ProductDetailsMapper productDetailsMapper;
    
    /**
     * Save a productDetails.
     * 
     * @param productDetailsDTO the entity to save
     * @return the persisted entity
     */
    public ProductDetailsDTO save(ProductDetailsDTO productDetailsDTO) {
        log.debug("Request to save ProductDetails : {}", productDetailsDTO);
        ProductDetails productDetails = productDetailsMapper.productDetailsDTOToProductDetails(productDetailsDTO);
        productDetails = productDetailsRepository.save(productDetails);
        ProductDetailsDTO result = productDetailsMapper.productDetailsToProductDetailsDTO(productDetails);
        return result;
    }

    /**
     *  Get all the productDetails.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProductDetailsDTO> findAll() {
        log.debug("Request to get all ProductDetails");
        List<ProductDetailsDTO> result = productDetailsRepository.findAll().stream()
            .map(productDetailsMapper::productDetailsToProductDetailsDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one productDetails by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProductDetailsDTO findOne(Long id) {
        log.debug("Request to get ProductDetails : {}", id);
        ProductDetails productDetails = productDetailsRepository.findOne(id);
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.productDetailsToProductDetailsDTO(productDetails);
        return productDetailsDTO;
    }

    /**
     *  Delete the  productDetails by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductDetails : {}", id);
        productDetailsRepository.delete(id);
    }
}
