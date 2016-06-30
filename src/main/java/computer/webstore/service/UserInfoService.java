package computer.webstore.service;

import computer.webstore.domain.UserInfo;
import computer.webstore.repository.UserInfoRepository;
import computer.webstore.web.rest.dto.UserInfoDTO;
import computer.webstore.web.rest.mapper.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing UserInfo.
 */
@Service
@Transactional
public class UserInfoService {

    private final Logger log = LoggerFactory.getLogger(UserInfoService.class);
    
    @Inject
    private UserInfoRepository userInfoRepository;
    
    @Inject
    private UserInfoMapper userInfoMapper;
    
    /**
     * Save a userInfo.
     * 
     * @param userInfoDTO the entity to save
     * @return the persisted entity
     */
    public UserInfoDTO save(UserInfoDTO userInfoDTO) {
        log.debug("Request to save UserInfo : {}", userInfoDTO);
        UserInfo userInfo = userInfoMapper.userInfoDTOToUserInfo(userInfoDTO);
        userInfo = userInfoRepository.save(userInfo);
        UserInfoDTO result = userInfoMapper.userInfoToUserInfoDTO(userInfo);
        return result;
    }

    /**
     *  Get all the userInfos.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<UserInfoDTO> findAll() {
        log.debug("Request to get all UserInfos");
        List<UserInfoDTO> result = userInfoRepository.findAll().stream()
            .map(userInfoMapper::userInfoToUserInfoDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one userInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UserInfoDTO findOne(Long id) {
        log.debug("Request to get UserInfo : {}", id);
        UserInfo userInfo = userInfoRepository.findOne(id);
        UserInfoDTO userInfoDTO = userInfoMapper.userInfoToUserInfoDTO(userInfo);
        return userInfoDTO;
    }

    /**
     *  Delete the  userInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserInfo : {}", id);
        userInfoRepository.delete(id);
    }
}
