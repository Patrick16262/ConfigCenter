package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.po.BranchPO;
import site.patrickshao.admin.common.entity.po.ModificationPO;
import site.patrickshao.admin.common.entity.po.PublishPO;

import java.util.List;
import java.util.Map;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
public class PublishService {
    private DefaultRepository<PublishPO> publishRepository;
    private DefaultRepository<ModificationPO> modificationRepository;

    @Transactional
    public List<PublishPO> getNeedApprovePublishList(Long userId) {
        return publishRepository.selectByWrapper(new QueryWrapper<PublishPO>()
                .eq(DataBaseFields.Publish.AUTHORIZER, userId)
                .eq(DataBaseFields.Publish.WORKSPACE, true));
    }

    @Transactional
    @NotForController
    public void deletePublishByBranchId(Long branchId, String operator) {
        List<PublishPO> ls = publishRepository.selectByParentId(branchId);
        ls.forEach(i -> deleteModificationByPublishId(i.getId(), operator));
        publishRepository.deleteByParentId(branchId, operator);
    }

    @Transactional
    @NotForController
    public void deleteModificationByPublishId(Long publishId, String operator) {
        modificationRepository.deleteByParentId(publishId, operator);
    }

    @Transactional
    @NotForController
    public void initializeForBranch(BranchPO branchPO, String operatorName) {
        PublishPO workspace = new PublishPO();
        workspace.setBranchId(branchPO.getId());
        workspace.setApplicationId(branchPO.getApplicationId());
        workspace.setNamespaceId(branchPO.getNamespaceId());
        workspace.setName("$$$$WorkSpace$$$");
        workspace.setAuthorizer(operatorName);
        workspace.setWorkspace(true);
        publishRepository.create(workspace, operatorName);

        PublishPO initialPublish = new PublishPO();
        initialPublish.setBranchId(branchPO.getId());
        initialPublish.setApplicationId(branchPO.getApplicationId());
        initialPublish.setNamespaceId(branchPO.getNamespaceId());
        initialPublish.setName("Initial");
        initialPublish.setAuthorizer(operatorName);
        initialPublish.setWorkspace(false);
        Long head = publishRepository.create(initialPublish, operatorName).getId();
        branchPO.setBranchHead(head);
    }


}
