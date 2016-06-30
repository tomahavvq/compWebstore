package computer.webstore.web.rest;

import computer.webstore.ComputerWebstoreApp;
import computer.webstore.domain.StoreOrder;
import computer.webstore.repository.StoreOrderRepository;
import computer.webstore.service.StoreOrderService;
import computer.webstore.web.rest.dto.StoreOrderDTO;
import computer.webstore.web.rest.mapper.StoreOrderMapper;

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
 * Test class for the StoreOrderResource REST controller.
 *
 * @see StoreOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ComputerWebstoreApp.class)
@WebAppConfiguration
@IntegrationTest
public class StoreOrderResourceIntTest {


    private static final Integer DEFAULT_PRODUCT_AMOUNT = 1;
    private static final Integer UPDATED_PRODUCT_AMOUNT = 2;

    private static final Double DEFAULT_TOTAL_SUM = 1D;
    private static final Double UPDATED_TOTAL_SUM = 2D;

    @Inject
    private StoreOrderRepository storeOrderRepository;

    @Inject
    private StoreOrderMapper storeOrderMapper;

    @Inject
    private StoreOrderService storeOrderService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStoreOrderMockMvc;

    private StoreOrder storeOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoreOrderResource storeOrderResource = new StoreOrderResource();
        ReflectionTestUtils.setField(storeOrderResource, "storeOrderService", storeOrderService);
        ReflectionTestUtils.setField(storeOrderResource, "storeOrderMapper", storeOrderMapper);
        this.restStoreOrderMockMvc = MockMvcBuilders.standaloneSetup(storeOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        storeOrder = new StoreOrder();
        storeOrder.setProductAmount(DEFAULT_PRODUCT_AMOUNT);
        storeOrder.setTotalSum(DEFAULT_TOTAL_SUM);
    }

    @Test
    @Transactional
    public void createStoreOrder() throws Exception {
        int databaseSizeBeforeCreate = storeOrderRepository.findAll().size();

        // Create the StoreOrder
        StoreOrderDTO storeOrderDTO = storeOrderMapper.storeOrderToStoreOrderDTO(storeOrder);

        restStoreOrderMockMvc.perform(post("/api/store-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storeOrderDTO)))
                .andExpect(status().isCreated());

        // Validate the StoreOrder in the database
        List<StoreOrder> storeOrders = storeOrderRepository.findAll();
        assertThat(storeOrders).hasSize(databaseSizeBeforeCreate + 1);
        StoreOrder testStoreOrder = storeOrders.get(storeOrders.size() - 1);
        assertThat(testStoreOrder.getProductAmount()).isEqualTo(DEFAULT_PRODUCT_AMOUNT);
        assertThat(testStoreOrder.getTotalSum()).isEqualTo(DEFAULT_TOTAL_SUM);
    }

    @Test
    @Transactional
    public void getAllStoreOrders() throws Exception {
        // Initialize the database
        storeOrderRepository.saveAndFlush(storeOrder);

        // Get all the storeOrders
        restStoreOrderMockMvc.perform(get("/api/store-orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(storeOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].productAmount").value(hasItem(DEFAULT_PRODUCT_AMOUNT)))
                .andExpect(jsonPath("$.[*].totalSum").value(hasItem(DEFAULT_TOTAL_SUM.doubleValue())));
    }

    @Test
    @Transactional
    public void getStoreOrder() throws Exception {
        // Initialize the database
        storeOrderRepository.saveAndFlush(storeOrder);

        // Get the storeOrder
        restStoreOrderMockMvc.perform(get("/api/store-orders/{id}", storeOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(storeOrder.getId().intValue()))
            .andExpect(jsonPath("$.productAmount").value(DEFAULT_PRODUCT_AMOUNT))
            .andExpect(jsonPath("$.totalSum").value(DEFAULT_TOTAL_SUM.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreOrder() throws Exception {
        // Get the storeOrder
        restStoreOrderMockMvc.perform(get("/api/store-orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreOrder() throws Exception {
        // Initialize the database
        storeOrderRepository.saveAndFlush(storeOrder);
        int databaseSizeBeforeUpdate = storeOrderRepository.findAll().size();

        // Update the storeOrder
        StoreOrder updatedStoreOrder = new StoreOrder();
        updatedStoreOrder.setId(storeOrder.getId());
        updatedStoreOrder.setProductAmount(UPDATED_PRODUCT_AMOUNT);
        updatedStoreOrder.setTotalSum(UPDATED_TOTAL_SUM);
        StoreOrderDTO storeOrderDTO = storeOrderMapper.storeOrderToStoreOrderDTO(updatedStoreOrder);

        restStoreOrderMockMvc.perform(put("/api/store-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storeOrderDTO)))
                .andExpect(status().isOk());

        // Validate the StoreOrder in the database
        List<StoreOrder> storeOrders = storeOrderRepository.findAll();
        assertThat(storeOrders).hasSize(databaseSizeBeforeUpdate);
        StoreOrder testStoreOrder = storeOrders.get(storeOrders.size() - 1);
        assertThat(testStoreOrder.getProductAmount()).isEqualTo(UPDATED_PRODUCT_AMOUNT);
        assertThat(testStoreOrder.getTotalSum()).isEqualTo(UPDATED_TOTAL_SUM);
    }

    @Test
    @Transactional
    public void deleteStoreOrder() throws Exception {
        // Initialize the database
        storeOrderRepository.saveAndFlush(storeOrder);
        int databaseSizeBeforeDelete = storeOrderRepository.findAll().size();

        // Get the storeOrder
        restStoreOrderMockMvc.perform(delete("/api/store-orders/{id}", storeOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreOrder> storeOrders = storeOrderRepository.findAll();
        assertThat(storeOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
