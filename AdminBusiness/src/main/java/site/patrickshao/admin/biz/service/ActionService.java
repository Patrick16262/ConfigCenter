package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.po.ActionPO;
import site.patrickshao.admin.common.entity.po.ActionRequirePO;
import site.patrickshao.admin.common.entity.po.PermissionPO;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/12
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@ParametersAreNonnullByDefault
public class ActionService {
    @Autowired
    private DefaultRepository<ActionPO> actionRepository;
    @Autowired
    private DefaultRepository<ActionRequirePO> actionRequireRepository;
    @Autowired
    private DefaultRepository<PermissionPO> permissionRepository;
    @Transactional
    public List<PermissionPO> getActionRequiredPermissionPO(Long actionId) {
        List<Long> list = actionRequireRepository
                .selectByParentId(actionId)
                .stream()
                .map(ActionRequirePO::getPermissionID)
                .toList();
        return permissionRepository.selectByIds(list);
    }

    @Transactional
    public List<ActionPO> getAllActionPO() {
        return actionRepository.selectByWrapper(new QueryWrapper<>());
    }

    @Transactional
    @Nullable
    public List<PermissionPO> getPermissionByActionName(String actionName) {
        if (getActionPO(actionName) == null) return null;
        return permissionRepository.selectByIds(
                actionRepository.selectByWrapper(
                                new QueryWrapper<ActionPO>()
                                        .eq(DataBaseFields.ActionPO.ACTION_NAME, actionName)
                        ).stream()
                        .map(ActionPO::getId)
                        .map(actionRequireRepository::selectByParentId)
                        .flatMap(List::stream)
                        .map(ActionRequirePO::getPermissionID)
                        .toList()
        );
    }

    @Nullable
    private ActionPO getActionPO(String actionName) {
        List<ActionPO> actionPOS = actionRepository.selectByWrapper(
                new QueryWrapper<ActionPO>()
                        .eq(DataBaseFields.ActionPO.ACTION_NAME, actionName)
        );
        if (actionPOS.size() != 1) return null;
        else return actionPOS.get(0);
    }
}
