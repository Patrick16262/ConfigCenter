package site.patrickshao.admin.common.utils;

import site.patrickshao.admin.common.entity.bo.SpecifiedPermissionBO;
import site.patrickshao.admin.common.entity.dto.SpecifiedRoleDTO;
import site.patrickshao.admin.common.entity.dto.SpecifiedUserDTO;
import site.patrickshao.admin.common.entity.po.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/12
 */
@ParametersAreNonnullByDefault
public class PojoUtils {
    public static SpecifiedPermissionBO makeSpecifiedPermissionBO(UserRolePO userRolePO, PermissionPO permissionPO) {
        SpecifiedPermissionBO specifiedPermissionBO = new SpecifiedPermissionBO();
        specifiedPermissionBO.setId(permissionPO.getId());
        specifiedPermissionBO.setName(permissionPO.getPermissionName());
        specifiedPermissionBO.setApplicationId(userRolePO.getApplicationSpecification());
        specifiedPermissionBO.setNamespaceId(userRolePO.getNamespaceSpecification());
        return specifiedPermissionBO;
    }

    public static SpecifiedRoleDTO makeSpecifiedRoleDTO(UserRolePO userRolePO, RolePO rolePO, List<SpecifiedPermissionBO> SpecifiedPermissionBOList) {
        SpecifiedRoleDTO specifiedRoleDTO = new SpecifiedRoleDTO();
        specifiedRoleDTO.setId(rolePO.getId());
        specifiedRoleDTO.setName(rolePO.getRoleName());
        specifiedRoleDTO.setApplicationId(userRolePO.getApplicationSpecification());
        specifiedRoleDTO.setNamespaceId(userRolePO.getNamespaceSpecification());
        specifiedRoleDTO.setPermissions(SpecifiedPermissionBOList);
        return specifiedRoleDTO;
    }

    public static SpecifiedUserDTO makeSpecifiedUserDTO(UserPO userPO, List<SpecifiedRoleDTO> specifiedRoleDTOList) {
        SpecifiedUserDTO specifiedUserDTO = new SpecifiedUserDTO();
        specifiedUserDTO.setId(userPO.getId());
        specifiedUserDTO.setName(userPO.getUserName());
        specifiedUserDTO.setEmail(userPO.getEmail());
        specifiedUserDTO.setNickName(userPO.getNickname());
        specifiedUserDTO.setRole(specifiedRoleDTOList);
        return specifiedUserDTO;
    }

    public static List<SpecifiedPermissionBO> getAllPermissionsDistinct(SpecifiedUserDTO specifiedUserDTO) {
        List<SpecifiedPermissionBO> list= specifiedUserDTO.getRole().stream().flatMap(role -> role.getPermissions().stream()).toList();
        return ArrayUtils.distinct(list);
    }

    private PojoUtils() {
    }
}
