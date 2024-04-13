package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.constants.NamespaceTypes;
import site.patrickshao.admin.common.entity.po.NamespacePO;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@ParametersAreNonnullByDefault
public class NamespaceService {
    @Autowired
    private BranchService branchService;
    @Autowired
    private DefaultRepository<NamespacePO> namespaceRepository;

    @Transactional
    @PreAuthorize("Application#Visible")
    public List<NamespacePO> getNamespaceList(Long applicationId) {
        return namespaceRepository.selectByParentId(applicationId);
    }

    @Transactional
    @PreAuthorize("Namespace#CreateNamespace")
    public Long createNamespace(NamespacePO namespacePO) {
        Throwables.validateRequest(namespacePO.getApplicationId() != null,
                "Application id cannot be null");
        Throwables.validateRequest(namespacePO.getName() != null, "Namespace name cannot be null");
        Throwables.validateRequest(namespacePO.getType() != null, "Namespace type cannot be null");
        validateNamespaceName(namespacePO);
        return namespaceRepository.create(namespacePO, AuthorizationContext.getUsername()).getId();
    }

    @Transactional
    @PreAuthorize("Namespace#DeleteNamespace")
    public void deleteNamespace(Long namespaceId, String operator) {
        Throwables.validateRequest(namespaceRepository.exists(namespaceId), "Namespace does not exist");
        branchService.deleteBranchByNamespaceId(namespaceId, operator);
        namespaceRepository.deleteById(namespaceId, AuthorizationContext.getUsername());
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Namespace#EditNamespaceName")
    public void editNamespaceName(Long namespaceId, String newName) {
        NamespacePO namespacePO = namespaceRepository.selectById(namespaceId);
        Throwables.validateRequest(namespacePO != null, "Namespace does not exist");
        Throwables.validateRequest(isApplicationOwnedNamespaceUnique(
                namespacePO.getApplicationId(), newName), "Namespace already exists");
        NamespacePO namespace = new NamespacePO();
        namespace.setNamespaceId(namespaceId);
        namespace.setName(newName);
        namespaceRepository.updateById(namespace, AuthorizationContext.getUsername());
    }


    @Transactional
    @PreAuthorize("Namespace#EditNamespaceComment")
    public void editNamespaceComment(Long namespaceId, String newComment) {
        NamespacePO namespacePO = namespaceRepository.selectById(namespaceId);
        Throwables.validateRequest(namespacePO != null, "Namespace does not exist");
        NamespacePO namespace = new NamespacePO();
        namespace.setNamespaceId(namespaceId);
        namespace.setComment(newComment);
        namespaceRepository.updateById(namespace, AuthorizationContext.getUsername());
    }


    @Transactional
    @NotForController
    public void deleteNamespaceByApplicationId(Long applicationId, String operator) {
        List<NamespacePO> ls = namespaceRepository.selectByParentId(applicationId);
        ls.forEach(i -> branchService.deleteBranchByNamespaceId(i.getId(), operator));
        namespaceRepository.deleteByParentId(applicationId, AuthorizationContext.getUsername());
    }

    @Transactional
    @NotForController
    public boolean isApplicationOwnedNamespaceUnique(Long applicationId, String namespaceName) {
        return namespaceRepository.selectByWrapper(new QueryWrapper<NamespacePO>()
                .eq(DataBaseFields.Namespace.APPLICATION_ID, applicationId)
                .eq(DataBaseFields.Namespace.NAMESPACE_NAME, namespaceName)
        ).isEmpty();
    }

    @Transactional
    @NotForController
    public boolean isPublicNamespaceUnique(Long applicationId, String namespaceName) {
        return namespaceRepository.selectByWrapper(new QueryWrapper<NamespacePO>()
                .eq(DataBaseFields.Namespace.NAMESPACE_NAME, namespaceName)
                .eq(DataBaseFields.Namespace.APPLICATION_ID, applicationId)
        ).isEmpty();
    }


    @Transactional
    @NotForController
    public void validateNamespaceName(NamespacePO namespacePO) {
        if (namespacePO.getType().equals(NamespaceTypes.PUBLIC)) {
            Throwables.validateRequest(isPublicNamespaceUnique(
                            namespacePO.getApplicationId(),
                            namespacePO.getName()),
                    "Namespace already exists");
        } else {
            Throwables.validateRequest(isApplicationOwnedNamespaceUnique(
                            namespacePO.getApplicationId(),
                            namespacePO.getName()),
                    "Namespace already exists");
        }
    }
}
