package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.constants.BranchType;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.po.*;
import site.patrickshao.admin.common.utils.ModificationUtils;
import site.patrickshao.admin.common.utils.PojoLinkedBranch;
import site.patrickshao.admin.common.utils.Throwables;

import java.util.*;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class BranchService {
    @Autowired
    private DefaultRepository<BranchPO> branchRepository;
    @Autowired
    private DefaultRepository<ItemPO> itemRepository;
    @Autowired
    private PublishService publishService;
    @Autowired
    private DefaultRepository<PublishPO> publishRepository;
    @Autowired
    private DefaultRepository<ModificationPO> modificationRepository;
    @Autowired
    private DefaultRepository<UserPO> userRepository;

    @Transactional
    @PreAuthorize("Cluster#DeleteClusterDeferred")
    public void deleteBranch(Long branchId, String operator) {
        var branch = branchRepository.selectById(branchId);
        Throwables.validateRequest(branch != null);
        deleteItemByBranchId(branchId, operator);
        branchRepository.deleteById(branchId, operator);
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Branch#Visit")
    public List<ItemPO> viewItemsInCertainPublish(Long publishId) {
        BranchPO branch = branchRepository.selectById(publishId);
        Throwables.validateRequest(branch != null);
        PojoLinkedBranch<PublishPO> branches = getPublishLinkedBranch(branch.getNamespaceId(), branch.getClusterId());
        var ls = branches.getPojoListToPoint(publishId).orElse(null);
        Throwables.validateRequest(ls != null);
        return ls.stream()
                .map(p -> modificationRepository.selectByParentId(p.getId()))
                .reduce(new ArrayList<>(), ModificationUtils::mergeModification)
                .stream()
                .map(i -> ModificationUtils.applyModification(null, i))
                .filter(Optional::isEmpty)
                .map(Optional::get)
                .toList();
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    public void rollbackPublish(Long branchId) {
        Long previous = getCurrentHeadPublish(branchId).getPreviousId();
        Throwables.validateRequest(previous != null, "No previous publish to rollback");
        processHeadMove(branchId, previous, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("Item#Visit")
    public List<ItemPO> getReleasedItemList(Long branchId) {
        Throwables.validateRequest(branchRepository.exists(branchId));
        return itemRepository.selectByParentId(branchId);
    }

    /**
     * 也可以直接发布
     *
     * @return Long publishId
     */
    @SuppressWarnings("ConstantValue")
    @Transactional
    @PreAuthorize("Branch#PublishChanges")
    public Long approvePublish(Long branchId, String publishName) {
        PublishPO previousPublish = getCurrentHeadPublish(branchId);
        PublishPO workspace = getWorkspace(branchId);
        Throwables.validateRequest(workspace.getAuthorizer().equals(AuthorizationContext.getUsername()),
                "You are not authorized to approve this publish");

        PublishPO newPublish = new PublishPO();

        newPublish.setBranchId(branchId);
        newPublish.setApplicationId(workspace.getApplicationId());
        newPublish.setNamespaceId(workspace.getNamespaceId());
        newPublish.setName(publishName);
        newPublish.setAuthorizer(workspace.getAuthorizer());
        newPublish.setPreviousId(previousPublish.getId());

        publishRepository.create(newPublish, AuthorizationContext.getUsername());
        previousPublish.setNextId(newPublish.getId());
        publishRepository.updateByIdWithoutOperator(previousPublish);
//        branchPO.setBranchHead(newPublish.getId());
//        branchRepository.updateById(branchPO, AuthorizationContext.getUsername());
        //todo
        return newPublish.getId();
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Branch#Visit")
    public void markPublishForApproval(Long branchId, Long authorizer) {
        PublishPO workspace = getWorkspace(branchId);
        Throwables.validateRequest(workspace.getAuthorizer() == null,
                "Workspace already marked for approval");
        UserPO user = userRepository.selectById(authorizer);
        Throwables.validateRequest(user != null, "User does not exist");
        workspace.setAuthorizer(user.getUserName());
        publishRepository.updateById(workspace, AuthorizationContext.getUsername());
    }


    @Transactional
    public void rejectPublish(Long branchId) {
        PublishPO workspace = getWorkspace(branchId);
        Throwables.validateRequest(workspace.getAuthorizer().equals(AuthorizationContext.getUsername()),
                "You are not authorized to reject this publish");
        clearWorkspaceAuthorizer(branchId);
    }

    @NotForController
    public PublishPO getWorkspace(Long branchId) {
        BranchPO branch = branchRepository.selectById(branchId);
        Throwables.validateRequest(branch != null, "Branch does not exist");
        return publishRepository.selectByWrapper(
                new QueryWrapper<PublishPO>()
                        .eq(DataBaseFields.Publish.BRANCH_ID, branchId)
                        .eq(DataBaseFields.Publish.WORKSPACE, true)
        ).get(0);
    }

    @NotForController
    public void deleteBranchByNamespaceId(Long namespaceId, String operator) {
        List<BranchPO> ls = branchRepository.selectByParentId(NamespacePO.class, namespaceId);
        ls.forEach(i -> deleteItemByBranchId(i.getId(), operator));
        ls.forEach(i -> publishService.deletePublishByBranchId(i.getId(), operator));
        branchRepository.deleteByParentId(NamespacePO.class, namespaceId, AuthorizationContext.getUsername());
    }

    @NotForController
    public void deleteBranchByClusterId(Long clusterId, String operator) {
        List<BranchPO> ls = branchRepository.selectByParentId(ClusterPO.class, clusterId);
        ls.forEach(i -> deleteItemByBranchId(i.getId(), operator));
        ls.forEach(i -> publishService.deletePublishByBranchId(i.getId(), operator));
        branchRepository.deleteByParentId(ClusterPO.class, clusterId, AuthorizationContext.getUsername());
    }

    @NotForController
    public void deleteItemByBranchId(Long branchId, String operator) {
        itemRepository.deleteByParentId(branchId, operator);
    }

    @NotForController
    public void createNamespaceClusterDefaultBranch(Long applicationId, Long namespaceId, Long clusterId, String userName) {
        BranchPO branchPO = new BranchPO();
        branchPO.setBranchType(BranchType.MAIN);
        branchPO.setNamespaceId(namespaceId);
        branchPO.setApplicationId(applicationId);
        branchPO.setClusterId(clusterId);
        branchPO.setName("Master");
        branchPO.setBranchHead((long) Integer.MAX_VALUE);
        branchRepository.create(branchPO, userName);
        publishService.initializeForBranch(branchPO, userName);
        branchRepository.updateById(branchPO, userName);
    }

    @NotForController
    public boolean isBranchNameUnique(Long namespaceID, Long applicationId, String name) {
        //not validate request
        return branchRepository.selectByWrapper(new QueryWrapper<BranchPO>()
                .eq(DataBaseFields.Branch.NAMESPACE_ID, namespaceID)
                .eq(DataBaseFields.Branch.APPLICATION_ID, applicationId)
        ).isEmpty();
    }

    @NotForController
    @SuppressWarnings("DataFlowIssue")
    public void createItemReleased(Long branchId, String key, String value, String userName) {
        BranchPO branch = branchRepository.selectById(branchId);
        Throwables.validateRequest(branch != null, "Branch does not exist");
        var item = new ItemPO();
        item.setBranchId(branchId);
        item.setKey(key);
        item.setValue(value);
        item.setApplicationId(branch.getApplicationId());
        item.setNamespaceId(branch.getNamespaceId());
        itemRepository.create(item, userName);
    }

    @NotForController
    public void updateReleasedItem(Long branchId, String key, String newValue, String userName) {
        BranchPO branch = branchRepository.selectById(branchId);
        Throwables.validateRequest(branch != null, "Branch does not exist");
        List<ItemPO> items = itemRepository.selectByWrapper(
                new QueryWrapper<ItemPO>()
                        .eq(DataBaseFields.Item.BRANCH_ID, branchId)
                        .eq(DataBaseFields.Item.VALUE, key)
        );
        Throwables.validateRequest(items.size() == 1, "Item does not exist");
        ItemPO itemPO = new ItemPO();
        itemPO.setValue(items.get(0).getValue());
        itemRepository.updateById(itemPO, userName);
    }

    @NotForController
    public void deleteReleasedItem(Long itemId, String userName) {
        Throwables.validateRequest(itemRepository.exists(itemId), "Item does not exist");
        itemRepository.deleteById(itemId, userName);
    }

    @NotForController
    @Nullable
    public ItemPO getReleasedItemByKey(Long branchId, String key) {
        List<ItemPO> itemList = itemRepository.selectByWrapper(
                new QueryWrapper<ItemPO>()
                        .eq(DataBaseFields.Item.BRANCH_ID, branchId)
                        .eq(DataBaseFields.Item.KEY, key)
        );
        return itemList.isEmpty() ? null : itemList.get(0);
    }


    @SuppressWarnings("DataFlowIssue")
    @NotForController
    @Nullable
    public BranchPO findMainBranchOf(Long clusterId, Long namespaceId) {
        var branchPO = branchRepository.selectByWrapper(new QueryWrapper<BranchPO>()
                .eq(DataBaseFields.Branch.CLUSTER_ID, clusterId)
                .eq(DataBaseFields.Branch.NAMESPACE_ID, namespaceId)
                .eq(DataBaseFields.Branch.BRANCH_TYPE, BranchType.MAIN));
        Throwables.validateRequest(branchPO != null, "Branch not found");
        return branchPO.get(0);
    }

    @Transactional
    @PreAuthorize("Namespace#Visit")
    public PojoLinkedBranch<PublishPO> getPublishLinkedBranch(Long namespaceId, Long clusterId) {
        Map<Class<?>, Long> map = new HashMap<>();
        map.put(NamespacePO.class, namespaceId);
        map.put(ClusterPO.class, clusterId);
        List<PublishPO> ls = branchRepository.selectByParentIds(map).stream()
                .map(b -> publishRepository.selectByParentId(b.getId()))
                .flatMap(List::stream)
                .toList();
        return PojoLinkedBranch.linkPojo(ls);
    }

    @NotForController
    public void checkIfWorkspaceModifiable(Long branchId) {
        PublishPO workspace = getWorkspace(branchId);
        Throwables.validateRequest(workspace.getAuthorizer() == null,
                "Workspace is not modifiable," +
                        " it is marked for approval and locked before approval or rejection");
    }

    /**
     * 当主干被回滚后再次发布，原来的发布将被挪入一个新的branch中
     * @author Shao Yibo
     * @date 2024/4/14
     */

    private void makeAbandonedMainBranch(Long startPublishId) {

//        PublishPO cur = publishRepository.selectById(startPublishId);
//        while (cur.getNextId() != null) {
//            cur.setBranchId();
//            cur = publishRepository.selectById(cur.getNextId());
//        }

    }

    @SuppressWarnings({"SpringTransactionalMethodCallsInspection", "DataFlowIssue"})
    private void processHeadMove(Long branchId, Long newHead, String operator) {
        BranchPO branch = branchRepository.selectById(branchId);
        Throwables.validateRequest(branch != null);
        branch.setBranchHead(newHead);
        branchRepository.updateById(branch, operator);

        List<ItemPO> curItems = viewItemsInCertainPublish(newHead);
        itemRepository.deleteByParentId(branchId, operator);
        curItems.forEach(i -> itemRepository.create(i, operator));

        clearWorkspace(branchId);
    }

    private void clearWorkspaceAuthorizer(Long branchId) {
        PublishPO workspace = getWorkspace(branchId);
        publishRepository.updateByUpdateWrapper(new UpdateWrapper<PublishPO>()
                        .set(DataBaseFields.Publish.AUTHORIZER, null)
                        .eq(DataBaseFields.ID, workspace.getId()),
                AuthorizationContext.getUsername());
    }

    private void clearWorkspace(Long branchId) {
        PublishPO workspace = getWorkspace(branchId);
        publishService.deleteModificationByPublishId(workspace.getId(), AuthorizationContext.getUsername());
        if (workspace.getAuthorizer() != null) {
            publishRepository.updateByUpdateWrapperWithoutOperator(new UpdateWrapper<PublishPO>()
                    .set(DataBaseFields.Publish.AUTHORIZER, null)
                    .eq(DataBaseFields.ID, workspace.getId())
            );
        }
    }

    private PublishPO getCurrentHeadPublish(BranchPO branchPO) {
        return publishRepository.selectById(branchPO.getBranchHead());
    }

    @SuppressWarnings("DataFlowIssue")
    private PublishPO getCurrentHeadPublish(Long branchId) {
        var branchPo = branchRepository.selectById(branchId);
        Throwables.validateRequest(branchPo != null);
        return publishRepository.selectById(branchPo.getBranchHead());
    }

    private BranchPO simplyMakeBranch() {
        BranchPO po = new BranchPO();
        po.setApplicationId(AuthorizationContext.getTargetApplicationId());
        po.setApplicationId(AuthorizationContext.getTargetNamespaceId());
        branchRepository.updateById(po, AuthorizationContext.getUsername());
        return po;
    }

}
