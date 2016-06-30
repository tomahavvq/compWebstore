package computer.webstore.web.rest;

import computer.webstore.ComputerWebstoreApp;
import computer.webstore.domain.OrderDetails;
import computer.webstore.repository.OrderDetailsRepository;
import computer.webstore.service.OrderDetailsService;
import computer.webstore.web.rest.dto.OrderDetailsDTO;
import computer.webstore.web.rest.mapper.OrderDetailsMapper;

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
 * Test class for the OrderDetailsResource REST controller.
 *
 * @see OrderDetailsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ComputerWebstoreApp.class)
@WebAppConfiguration
@IntegrationTest
public class OrderDetailsResourceIntTest {


    private static final Double DEFAULT_SUM = 1D;
    private static final Double UPDATED_SUM = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Inject
    private OrderDetailsRepository orderDetailsRepository;

    @Inject
    private OrderDetailsMapper orderDetailsMapper;

    @Inject
    private OrderDetailsService orderDetailsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderDetailsMockMvc;

    private OrderDetails orderDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderDetailsResource orderDetailsResource = new OrderDetailsResource();
        ReflectionTestUtils.setField(orderDetailsResource, "orderDetailsService", orderDetailsService);
        ReflectionTestUtils.setField(orderDetailsResource, "orderDetailsMapper", orderDetailsMapper);
        this.restOrderDetailsMockMvc = MockMvcBuilders.standaloneSetup(orderDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderDetails = new OrderDetails();
        orderDetails.setSum(DEFAULT_SUM);
        orderDetails.setQuantity(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = orderDetailsRepository.findAll().size();

        // Create the OrderDetails
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.orderDetailsToOrderDetailsDTO(orderDetails);

        restOrderDetailsMockMvc.perform(post("/api/order-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO)))
                .andExpect(status().isCreated());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetails = orderDetailsRepository.findAll();
        assertThat(orderDetails).hasSize(databaseSizeBeforeCreate + 1);
        OrderDetails testOrderDetails = orderDetails.get(orderDetails.size() - 1);
        assertThat(testOrderDetails.getSum()).isEqualTo(DEFAULT_SUM);
        assertThat(testOrderDetails.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetails
        restOrderDetailsMockMvc.perform(get("/api/order-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].sum").value(hasItem(DEFAULT_SUM.doubleValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get the orderDetails
        restOrderDetailsMockMvc.perform(get("/api/order-details/{id}", orderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderDetails.getId().intValue()))
            .andExpect(jsonPath("$.sum").value(DEFAULT_SUM.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingOrderDetails() throws Exception {
        // Get the orderDetails
        restOrderDetailsMockMvc.perform(get("/api/order-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();

        // Update the orderDetails
        OrderDetails updatedOrderDetails = new OrderDetails();
        updatedOrderDetails.setId(orderDetails.getId());
        updatedOrderDetails.setSum(UPDATED_SUM);
        updatedOrderDetails.setQuantity(UPDATED_QUANTITY);
        OrderDetailsDTO orderDetailsDTO = orderDetailsMapper.orderDetailsToOrderDetailsDTO(updatedOrderDetails);

        restOrderDetailsMockMvc.perform(put("/api/order-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDetailsDTO)))
                .andExpect(status().isOk());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetails = orderDetailsRepository.findAll();
        assertThat(orderDetails).hasSize(databaseSizeBeforeUpdate);
        OrderDetails testOrderDetails = orderDetails.get(orderDetails.size() - 1);
        assertThat(testOrderDetails.getSum()).isEqualTo(UPDATED_SUM);
        assertThat(testOrderDetails.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void deleteOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);
        int databaseSizeBeforeDelete = orderDetailsRepository.findAll().size();

        // Get the orderDetails
        restOrderDetailsMockMvc.perform(delete("/api/order-details/{id}", orderDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderDetails> orderDetails = orderDetailsRepository.findAll();
        assertThat(orderDetails).hasSize(databaseSizeBeforeDelete - 1);
    }
}
