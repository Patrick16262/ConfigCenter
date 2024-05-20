package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.constants.OperationNames;
import site.patrickshao.admin.common.entity.po.BranchPO;
import site.patrickshao.admin.common.entity.po.ItemPO;
import site.patrickshao.admin.common.entity.po.ModificationPO;
import site.patrickshao.admin.common.entity.po.PublishPO;
import site.patrickshao.admin.common.utils.ModificationUtils;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
@Service
@ParametersAreNonnullByDefault
public class ItemModificationService {
    @Autowired
    private DefaultRepository<BranchPO> branchRepository;
    @Autowired
    private BranchService branchService;
    @Autowired
    private DefaultRepository<ItemPO> itemRepository;
    @Autowired
    private DefaultRepository<ModificationPO> modificationRepository;

    @Transactional
    @PreAuthorize("Item#Visit")
    public List<ItemPO> getItemListNotRelease(Long branchId) {
        List<ItemPO> releasedItems = branchService.getReleasedItemList(branchId);
        List<ModificationPO> modificationList = getWorkspaceModificationList(branchId);
        return ModificationUtils.applyModification(releasedItems, modificationList);
    }

    @Transactional
    @PreAuthorize("Item#Visit")
    public List<ModificationPO> getModificationListNotRelease(Long branchId) {
        return getWorkspaceModificationList(branchId);
    }

    @Transactional
    @PreAuthorize("Item#AddItem")
    public void createItemNotRelease(Long branchId, String key, String value, String operator) {
        branchService.checkIfWorkspaceModifiable(branchId);
        BranchPO branchPO = branchRepository.selectById(branchId);
        ItemPO itemPO = getItemNotRelease(branchId, key);
        Throwables.validateRequest(itemPO != null, "Item already exists");
        ModificationPO modificationPO = new ModificationPO();
        modificationPO.setOperation(OperationNames.CREATE);
        modificationPO.setKey(key);
        modificationPO.setValue(value);
        replaceOrCreateWorkspaceModificationByKey(branchId, key, modificationPO, operator);
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Item#EditItemKeyValue")
    public void updateItemKeyValueNotRelease(Long branchId, String key, String value, String operator) {
        branchService.checkIfWorkspaceModifiable(branchId);
        BranchPO branchPO = branchRepository.selectById(branchId);
        ItemPO itemPO = getItemNotRelease(branchId, key);
        Throwables.validateRequest(itemPO != null, "Item does not exist");
        ModificationPO modificationPO = new ModificationPO();
        modificationPO.setOperation(OperationNames.UPDATE);
        ModificationPO old = getWorkspaceModificationByKey(branchId, key);
        ModificationPO newModification = ModificationUtils.mergeModification(old, modificationPO);
        replaceOrCreateWorkspaceModificationByKey(branchId, key, newModification, operator);
    }


    @Transactional
    @PreAuthorize("Item#DeleteItem")
    public void deleteItemNotRelease(Long branchId, String key, String operator) {
        branchService.checkIfWorkspaceModifiable(branchId);
        BranchPO branch = branchRepository.selectById(branchId);
        ItemPO curItem = getItemNotRelease(branchId, key);
        Throwables.validateRequest(curItem != null, "Item doesn't exist");
        ModificationPO modificationPO = new ModificationPO();
        modificationPO.setOperation(OperationNames.DELETE);
        replaceOrCreateWorkspaceModificationByKey(branchId, key, modificationPO, operator);
    }

    @Nullable
    @Transactional
    @PreAuthorize("Item#Visit")
    public ItemPO getItemNotRelease(Long branchId, String key) {
        BranchPO branch = branchRepository.selectById(branchId);
        Throwables.validateRequest(branch != null, "Invalid Branch Id");
        ItemPO itemPO = branchService.getReleasedItemByKey(branchId, key);
        ModificationPO modificationPO = getWorkspaceModificationByKey(branchId, key);
        return ModificationUtils.applyModification(itemPO, modificationPO).orElse(null);
    }

    private List<ModificationPO> getWorkspaceModificationList(Long branchId) {
        return modificationRepository
                .selectByWrapper(new QueryWrapper<ModificationPO>()
                        .eq(DataBaseFields.Modification.PUBLISH_ID, branchService.getWorkspace(branchId)));
    }

    private void replaceOrCreateWorkspaceModificationByKey(Long branchId, String key, ModificationPO modificationPO, String operator) {
        ModificationPO current = getWorkspaceModificationByKey(branchId, key);
        PublishPO workspace = branchService.getWorkspace(branchId);
        modificationPO.setKey(key);
        modificationPO.setApplicationId(workspace.getApplicationId());
        modificationPO.setNamespaceId(workspace.getNamespaceId());
        modificationPO.setPublishId(workspace.getId());
        if (current != null) {
            modificationRepository.deleteById(current.getId(), operator);
            return;
        }
        modificationRepository.create(modificationPO, operator);
    }

    @Nullable
    private ModificationPO getWorkspaceModificationByKey(Long branchId, String key) {
        List<ModificationPO> modificationPOList = modificationRepository
                .selectByWrapper(new QueryWrapper<ModificationPO>()
                        .eq(DataBaseFields.Modification.KEY, key)
                        .eq(DataBaseFields.Modification.PUBLISH_ID, branchService.getWorkspace(branchId)));
        assert modificationPOList.size() < 2;
        if (modificationPOList.isEmpty()) {
            return null;
        } else {
            return modificationPOList.get(0);
        }
    }

}
