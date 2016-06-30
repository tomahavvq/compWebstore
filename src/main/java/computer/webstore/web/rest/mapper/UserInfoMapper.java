package computer.webstore.web.rest.mapper;

import computer.webstore.domain.*;
import computer.webstore.web.rest.dto.UserInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity UserInfo and its DTO UserInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface UserInfoMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "address.id", target = "addressId")
    UserInfoDTO userInfoToUserInfoDTO(UserInfo userInfo);

    List<UserInfoDTO> userInfosToUserInfoDTOs(List<UserInfo> userInfos);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "addressId", target = "address")
    UserInfo userInfoDTOToUserInfo(UserInfoDTO userInfoDTO);

    List<UserInfo> userInfoDTOsToUserInfos(List<UserInfoDTO> userInfoDTOs);

    default Address addressFromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
