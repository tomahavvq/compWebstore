package computer.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import computer.webstore.domain.UserInfo;
import computer.webstore.service.UserInfoService;
import computer.webstore.web.rest.util.HeaderUtil;
import computer.webstore.web.rest.dto.UserInfoDTO;
import computer.webstore.web.rest.mapper.UserInfoMapper;
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
 * REST controller for managing UserInfo.
 */
@RestController
@RequestMapping("/api")
public class UserInfoResource {

    private final Logger log = LoggerFactory.getLogger(UserInfoResource.class);
        
    @Inject
    private UserInfoService userInfoService;
    
    @Inject
    private UserInfoMapper userInfoMapper;
    
    /**
     * POST  /user-infos : Create a new userInfo.
     *
     * @param userInfoDTO the userInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userInfoDTO, or with status 400 (Bad Request) if the userInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserInfoDTO> createUserInfo(@RequestBody UserInfoDTO userInfoDTO) throws URISyntaxException {
        log.debug("REST request to save UserInfo : {}", userInfoDTO);
        if (userInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userInfo", "idexists", "A new userInfo cannot already have an ID")).body(null);
        }
        UserInfoDTO result = userInfoService.save(userInfoDTO);
        return ResponseEntity.created(new URI("/api/user-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-infos : Updates an existing userInfo.
     *
     * @param userInfoDTO the userInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userInfoDTO,
     * or with status 400 (Bad Request) if the userInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the userInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO) throws URISyntaxException {
        log.debug("REST request to update UserInfo : {}", userInfoDTO);
        if (userInfoDTO.getId() == null) {
            return createUserInfo(userInfoDTO);
        }
        UserInfoDTO result = userInfoService.save(userInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userInfo", userInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-infos : get all the userInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userInfos in body
     */
    @RequestMapping(value = "/user-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<UserInfoDTO> getAllUserInfos() {
        log.debug("REST request to get all UserInfos");
        return userInfoService.findAll();
    }

    /**
     * GET  /user-infos/:id : get the "id" userInfo.
     *
     * @param id the id of the userInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable Long id) {
        log.debug("REST request to get UserInfo : {}", id);
        UserInfoDTO userInfoDTO = userInfoService.findOne(id);
        return Optional.ofNullable(userInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-infos/:id : delete the "id" userInfo.
     *
     * @param id the id of the userInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserInfo(@PathVariable Long id) {
        log.debug("REST request to delete UserInfo : {}", id);
        userInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userInfo", id.toString())).build();
    }

}
