package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.po.*;
import site.patrickshao.admin.common.utils.ArrayUtils;

import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
@Service
public class AuthorizationService {
    @Autowired
    private DefaultRepository<UserPO> userRepository;
    @Autowired
    private DefaultRepository<UserRolePO> userRoleRepository;
    @Autowired
    private DefaultRepository<RolePermissionPO> rolePermissionRepository;
    @Autowired
    private DefaultRepository<PermissionPO> permissionRepository;
    @Autowired
    private DefaultRepository<ActionRequirePO> actionRequireRepository;
    @Autowired
    private DefaultRepository<ActionPO> actionRepository;


    private List<PermissionPO> getActionRequirePermissionPO(Long actionId) {
        ActionRequirePO po = new ActionRequirePO();
        po.setActionID(actionId);
        List<Long> list = actionRequireRepository
                .selectByPartition(po)
                .stream()
                .map(ActionRequirePO::getActionID)
                .toList();
        return permissionRepository.selectByIds(list);
    }

    private ActionPO getActionPO(String actionName) {
        return ArrayUtils.getOnlyOne(
                actionRepository.selectByWrapper(
                        new QueryWrapper<ActionPO>()
                                .eq(DataBaseFields.ActionPO.ACTION_NAME, actionName)
                ));
    }
}
