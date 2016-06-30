package computer.webstore.web.rest;

import computer.webstore.ComputerWebstoreApp;
import computer.webstore.domain.Address;
import computer.webstore.repository.AddressRepository;
import computer.webstore.service.AddressService;
import computer.webstore.web.rest.dto.AddressDTO;
import computer.webstore.web.rest.mapper.AddressMapper;

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
 * Test class for the AddressResource REST controller.
 *
 * @see AddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ComputerWebstoreApp.class)
@WebAppConfiguration
@IntegrationTest
public class AddressResourceIntTest {

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_ZIP_CODE = "AAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBB";
    private static final String DEFAULT_STREET = "AAAAA";
    private static final String UPDATED_STREET = "BBBBB";
    private static final String DEFAULT_STREET_NUMBER = "AAAAA";
    private static final String UPDATED_STREET_NUMBER = "BBBBB";
    private static final String DEFAULT_LOCAL_NUMBER = "AAAAA";
    private static final String UPDATED_LOCAL_NUMBER = "BBBBB";

    @Inject
    private AddressRepository addressRepository;

    @Inject
    private AddressMapper addressMapper;

    @Inject
    private AddressService addressService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAddressMockMvc;

    private Address address;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressResource addressResource = new AddressResource();
        ReflectionTestUtils.setField(addressResource, "addressService", addressService);
        ReflectionTestUtils.setField(addressResource, "addressMapper", addressMapper);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        address = new Address();
        address.setCity(DEFAULT_CITY);
        address.setZipCode(DEFAULT_ZIP_CODE);
        address.setStreet(DEFAULT_STREET);
        address.setStreetNumber(DEFAULT_STREET_NUMBER);
        address.setLocalNumber(DEFAULT_LOCAL_NUMBER);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address
        AddressDTO addressDTO = addressMapper.addressToAddressDTO(address);

        restAddressMockMvc.perform(post("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
                .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addresses.get(addresses.size() - 1);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testAddress.getStreetNumber()).isEqualTo(DEFAULT_STREET_NUMBER);
        assertThat(testAddress.getLocalNumber()).isEqualTo(DEFAULT_LOCAL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addresses
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
                .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
                .andExpect(jsonPath("$.[*].streetNumber").value(hasItem(DEFAULT_STREET_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].localNumber").value(hasItem(DEFAULT_LOCAL_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.streetNumber").value(DEFAULT_STREET_NUMBER.toString()))
            .andExpect(jsonPath("$.localNumber").value(DEFAULT_LOCAL_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = new Address();
        updatedAddress.setId(address.getId());
        updatedAddress.setCity(UPDATED_CITY);
        updatedAddress.setZipCode(UPDATED_ZIP_CODE);
        updatedAddress.setStreet(UPDATED_STREET);
        updatedAddress.setStreetNumber(UPDATED_STREET_NUMBER);
        updatedAddress.setLocalNumber(UPDATED_LOCAL_NUMBER);
        AddressDTO addressDTO = addressMapper.addressToAddressDTO(updatedAddress);

        restAddressMockMvc.perform(put("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
                .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addresses.get(addresses.size() - 1);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAddress.getStreetNumber()).isEqualTo(UPDATED_STREET_NUMBER);
        assertThat(testAddress.getLocalNumber()).isEqualTo(UPDATED_LOCAL_NUMBER);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Get the address
        restAddressMockMvc.perform(delete("/api/addresses/{id}", address.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
