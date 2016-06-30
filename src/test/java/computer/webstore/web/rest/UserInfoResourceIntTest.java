package computer.webstore.web.rest;

import computer.webstore.ComputerWebstoreApp;
import computer.webstore.domain.UserInfo;
import computer.webstore.repository.UserInfoRepository;
import computer.webstore.service.UserInfoService;
import computer.webstore.web.rest.dto.UserInfoDTO;
import computer.webstore.web.rest.mapper.UserInfoMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserInfoResource REST controller.
 *
 * @see UserInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ComputerWebstoreApp.class)
@WebAppConfiguration
@IntegrationTest
public class UserInfoResourceIntTest {


    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private UserInfoRepository userInfoRepository;

    @Inject
    private UserInfoMapper userInfoMapper;

    @Inject
    private UserInfoService userInfoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserInfoResource userInfoResource = new UserInfoResource();
        ReflectionTestUtils.setField(userInfoResource, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(userInfoResource, "userInfoMapper", userInfoMapper);
        this.restUserInfoMockMvc = MockMvcBuilders.standaloneSetup(userInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userInfo = new UserInfo();
        userInfo.setBirthDate(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo
        UserInfoDTO userInfoDTO = userInfoMapper.userInfoToUserInfoDTO(userInfo);

        restUserInfoMockMvc.perform(post("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfos.get(userInfos.size() - 1);
        assertThat(testUserInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfos
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = new UserInfo();
        updatedUserInfo.setId(userInfo.getId());
        updatedUserInfo.setBirthDate(UPDATED_BIRTH_DATE);
        UserInfoDTO userInfoDTO = userInfoMapper.userInfoToUserInfoDTO(updatedUserInfo);

        restUserInfoMockMvc.perform(put("/api/user-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
                .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfos.get(userInfos.size() - 1);
        assertThat(testUserInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void deleteUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);
        int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Get the userInfo
        restUserInfoMockMvc.perform(delete("/api/user-infos/{id}", userInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserInfo> userInfos = userInfoRepository.findAll();
        assertThat(userInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
