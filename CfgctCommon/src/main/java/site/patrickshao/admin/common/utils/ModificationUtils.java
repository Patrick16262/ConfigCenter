package site.patrickshao.admin.common.utils;

import jakarta.annotation.Nullable;
import site.patrickshao.admin.common.constants.OperationNames;
import site.patrickshao.admin.common.entity.po.ItemPO;
import site.patrickshao.admin.common.entity.po.ModificationPO;
import site.patrickshao.admin.common.exception.IllegalDataRelationException;
import site.patrickshao.admin.common.exception.http.Http400BadRequest;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
public class ModificationUtils {
    private ModificationUtils() {
    }

    /**
     * Merge two modifications
     * 当两个操作无法合并时，返回null
     *
     * @param oldOne oldOne modification
     * @param newOne new modification
     * @return merged modification
     */
    @Nullable
    @ParametersAreNullableByDefault
    public static ModificationPO mergeModification(ModificationPO oldOne, ModificationPO newOne) {
        if (oldOne == null) {
            return newOne;
        }
        if (newOne == null) {
            return oldOne;
        }
        ModificationPO po = new ModificationPO();
        po.setPublishId(oldOne.getPublishId());
        po.setApplicationId(oldOne.getApplicationId());
        po.setNamespaceId(oldOne.getNamespaceId());
        if (newOne.getOperation().equals(OperationNames.DELETE)) {
            if (oldOne.getOperation().equals(OperationNames.DELETE)) {
                throw new Http400BadRequest("Cannot delete a deleted item");
            }
            po.setOperation(OperationNames.DELETE);
            return po;
        }
        if (oldOne.getOperation().equals(OperationNames.CREATE)) {
            if (newOne.getOperation().equals(OperationNames.UPDATE)) {
                po.setOperation(OperationNames.CREATE);
                po.setKey(newOne.getKey() == null ? oldOne.getKey() : newOne.getKey());
                po.setValue(newOne.getValue() == null ? oldOne.getValue() : newOne.getValue());
                return po;
            }
        }
        if (oldOne.getOperation().equals(OperationNames.UPDATE)) {
            if (newOne.getOperation().equals(OperationNames.UPDATE)) {
                po.setOperation(OperationNames.UPDATE);
                po.setKey(newOne.getKey() == null ? oldOne.getKey() : newOne.getKey());
                po.setValue(newOne.getValue() == null ? oldOne.getValue() : newOne.getValue());
                return po;
            }
        }
        if (oldOne.getOperation().equals(OperationNames.DELETE)) {
            if (newOne.getOperation().equals(OperationNames.CREATE)) {
                po.setOperation(OperationNames.UPDATE);
                po.setKey(newOne.getKey());
                po.setValue(newOne.getValue());
                return po;
            }
        }
        return null;
    }



    @ParametersAreNullableByDefault
    public static Optional<ItemPO> applyModification(ItemPO item, ModificationPO modification) {
        if (modification == null) {
            if (item == null) {
                return Optional.empty();
            } else {
                return Optional.of(item);
            }
        }
        if (item == null) {
            Throwables.throwOnCondition(!modification.getOperation().equals(OperationNames.CREATE)).illegalArgument();
            return Optional.empty();
        }
        ItemPO newItem = new ItemPO();
        Throwables.throwOnCondition((modification.getOperation().equals(OperationNames.CREATE))).illegalArgument();
        if (modification.getOperation().equals(OperationNames.DELETE)) {
            return Optional.empty();
        }
        newItem.setKey(modification.getKey() == null ? item.getKey() : modification.getKey());
        newItem.setValue(modification.getValue() == null ? item.getValue() : modification.getValue());
        return Optional.of(newItem);
    }

    @ParametersAreNonnullByDefault
    public static List<ItemPO> applyModification(List<ItemPO> itemList, List<ModificationPO> modificationList) {
        Map<String, ItemPO> itemMap = itemList.stream().collect(Collectors.toMap(ItemPO::getKey, i -> i));
        Map<String, ModificationPO> modificationMap = modificationList.stream()
                .collect(Collectors.toMap(ModificationPO::getKey, i -> i));
        List<ItemPO> res = new ArrayList<>();
        for (Map.Entry<String, ItemPO> entry : itemMap.entrySet()) {
            String key = entry.getKey();
            ItemPO item = entry.getValue();
            ModificationPO modification = modificationMap.get(key);
            applyModification(item, modification).ifPresent(res::add);
        }
        return res;
    }

    @ParametersAreNonnullByDefault
    public static List<ModificationPO> mergeModification(List<ModificationPO> oldList, List<ModificationPO> newList) {
        var oldMap = oldList.stream().collect(Collectors.toMap(
                ModificationPO::getKey,
                i -> i,
                (a, b) -> {
                    throw new IllegalDataRelationException("" + a + b);
                }));
        var newMap = newList.stream().collect(Collectors.toMap(
                ModificationPO::getKey,
                i -> i,
                (a, b) -> {
                    throw new IllegalDataRelationException("" + a + b);
                }));
        return new ArrayList<>(mergeModificationWithMap(oldMap, newMap).values());
    }

    private static Map<String, ModificationPO> mergeModificationWithMap(Map<String, ModificationPO> oldMap, Map<String, ModificationPO> newMap) {
        HashMap<String, ModificationPO> resMap = new HashMap<>();
        for (Map.Entry<String, ModificationPO> entry : oldMap.entrySet()) {
            String key = entry.getKey();
            ModificationPO old = entry.getValue();
            ModificationPO newOne = newMap.get(key);
            if (newOne == null) {
                resMap.put(key, old);
            } else {
                ModificationPO merged = mergeModification(old, newOne);
                Throwables.throwOnCondition(merged == null)
                        .badDataRelation("Cannot merge modification. \n old: " + old + " \n new: " + newOne);
                resMap.put(key, merged);
            }
        }
        return resMap;
    }

}
