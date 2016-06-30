package computer.webstore.web.rest;

import computer.webstore.ComputerWebstoreApp;
import computer.webstore.domain.ProductDetails;
import computer.webstore.repository.ProductDetailsRepository;
import computer.webstore.service.ProductDetailsService;
import computer.webstore.web.rest.dto.ProductDetailsDTO;
import computer.webstore.web.rest.mapper.ProductDetailsMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ProductDetailsResource REST controller.
 *
 * @see ProductDetailsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ComputerWebstoreApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProductDetailsResourceIntTest {

    private static final String DEFAULT_DETAILS = "AAAAA";
    private static final String UPDATED_DETAILS = "BBBBB";
    private static final String DEFAULT_ATTRIBUTES = "AAAAA";
    private static final String UPDATED_ATTRIBUTES = "BBBBB";
    private static final String DEFAULT_PRODUCTION_YEAR = "AAAAA";
    private static final String UPDATED_PRODUCTION_YEAR = "BBBBB";

    @Inject
    private ProductDetailsRepository productDetailsRepository;

    @Inject
    private ProductDetailsMapper productDetailsMapper;

    @Inject
    private ProductDetailsService productDetailsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductDetailsMockMvc;

    private ProductDetails productDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductDetailsResource productDetailsResource = new ProductDetailsResource();
        ReflectionTestUtils.setField(productDetailsResource, "productDetailsService", productDetailsService);
        ReflectionTestUtils.setField(productDetailsResource, "productDetailsMapper", productDetailsMapper);
        this.restProductDetailsMockMvc = MockMvcBuilders.standaloneSetup(productDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productDetails = new ProductDetails();
        productDetails.setDetails(DEFAULT_DETAILS);
        productDetails.setAttributes(DEFAULT_ATTRIBUTES);
        productDetails.setProductionYear(DEFAULT_PRODUCTION_YEAR);
    }

    @Test
    @Transactional
    public void createProductDetails() throws Exception {
        int databaseSizeBeforeCreate = productDetailsRepository.findAll().size();

        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.productDetailsToProductDetailsDTO(productDetails);

        restProductDetailsMockMvc.perform(post("/api/product-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productDetailsDTO)))
                .andExpect(status().isCreated());

        // Validate the ProductDetails in the database
        List<ProductDetails> productDetails = productDetailsRepository.findAll();
        assertThat(productDetails).hasSize(databaseSizeBeforeCreate + 1);
        ProductDetails testProductDetails = productDetails.get(productDetails.size() - 1);
        assertThat(testProductDetails.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testProductDetails.getAttributes()).isEqualTo(DEFAULT_ATTRIBUTES);
        assertThat(testProductDetails.getProductionYear()).isEqualTo(DEFAULT_PRODUCTION_YEAR);
    }

    @Test
    @Transactional
    public void getAllProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);

        // Get all the productDetails
        restProductDetailsMockMvc.perform(get("/api/product-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
                .andExpect(jsonPath("$.[*].attributes").value(hasItem(DEFAULT_ATTRIBUTES.toString())))
                .andExpect(jsonPath("$.[*].productionYear").value(hasItem(DEFAULT_PRODUCTION_YEAR.toString())));
    }

    @Test
    @Transactional
    public void getProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);

        // Get the productDetails
        restProductDetailsMockMvc.perform(get("/api/product-details/{id}", productDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productDetails.getId().intValue()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
            .andExpect(jsonPath("$.attributes").value(DEFAULT_ATTRIBUTES.toString()))
            .andExpect(jsonPath("$.productionYear").value(DEFAULT_PRODUCTION_YEAR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductDetails() throws Exception {
        // Get the productDetails
        restProductDetailsMockMvc.perform(get("/api/product-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);
        int databaseSizeBeforeUpdate = productDetailsRepository.findAll().size();

        // Update the productDetails
        ProductDetails updatedProductDetails = new ProductDetails();
        updatedProductDetails.setId(productDetails.getId());
        updatedProductDetails.setDetails(UPDATED_DETAILS);
        updatedProductDetails.setAttributes(UPDATED_ATTRIBUTES);
        updatedProductDetails.setProductionYear(UPDATED_PRODUCTION_YEAR);
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.productDetailsToProductDetailsDTO(updatedProductDetails);

        restProductDetailsMockMvc.perform(put("/api/product-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productDetailsDTO)))
                .andExpect(status().isOk());

        // Validate the ProductDetails in the database
        List<ProductDetails> productDetails = productDetailsRepository.findAll();
        assertThat(productDetails).hasSize(databaseSizeBeforeUpdate);
        ProductDetails testProductDetails = productDetails.get(productDetails.size() - 1);
        assertThat(testProductDetails.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testProductDetails.getAttributes()).isEqualTo(UPDATED_ATTRIBUTES);
        assertThat(testProductDetails.getProductionYear()).isEqualTo(UPDATED_PRODUCTION_YEAR);
    }

    @Test
    @Transactional
    public void deleteProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);
        int databaseSizeBeforeDelete = productDetailsRepository.findAll().size();

        // Get the productDetails
        restProductDetailsMockMvc.perform(delete("/api/product-details/{id}", productDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductDetails> productDetails = productDetailsRepository.findAll();
        assertThat(productDetails).hasSize(databaseSizeBeforeDelete - 1);
    }
}
