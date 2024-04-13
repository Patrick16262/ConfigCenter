package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import site.patrickshao.admin.common.utils.Throwables;

import java.util.List;

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

    @Transactional
    @PreAuthorize("Cluster#DeleteClusterDeferred")
    public void deleteBranch(Long branchId, String operator) {
        var branch = branchRepository.selectById(branchId);
        Throwables.validateRequest(branch != null);
        deleteItemByBranchId(branchId, operator);
        branchRepository.deleteById(branchId, operator);
    }


    @Transactional
    @PreAuthorize("Item#Visit")
    public List<ItemPO> getReleasedItemList(Long branchId) {
        Throwables.validateRequest(branchRepository.exists(branchId));
        return itemRepository.selectByParentId(branchId);
    }

    @NotForController
    public PublishPO getWorkspace(Long branchId) {
        BranchPO branch = branchRepository.selectById(branchId);
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


}
