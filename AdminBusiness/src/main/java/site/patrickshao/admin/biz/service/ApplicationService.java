package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.consts.ProfileValues;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.entity.po.AccessTokenPO;
import site.patrickshao.admin.common.entity.po.ApplicationPO;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static site.patrickshao.admin.common.constants.DataBaseFields.Application.APPLICATION_NAME;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ParametersAreNonnullByDefault
@Service
public class ApplicationService {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private DefaultRepository<ApplicationPO> applicationRepository;
    @Autowired
    private NamespaceService namespaceService;
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private DefaultRepository<AccessTokenPO> accessTokenRepository;

    @Transactional
    public List<ApplicationPO> getVisibleApplicationList() {
        List<ApplicationPO> applicationPOList = applicationRepository.selectByWrapper(new QueryWrapper<>());
        return applicationPOList.stream()
                .filter(applicationPO -> authorizationService
                        .checkIfHavePermissionToSeeApplication(AuthorizationContext.getSpecifiedUserDTO(),
                                applicationPO.getId()))
                .toList();
    }

    @Transactional
    @PreAuthorize("Application#Create")
    public Long createApplication(ApplicationPO applicationPO) {
        Throwables.throwOnNull(applicationPO.getDeferredDelete(), applicationPO.getTobeDeletedAt());
        Throwables.validateRequest(isApplicationNameUnique(applicationPO.getApplicationName())
                , "Application already exists");
        return applicationRepository.create(applicationPO, AuthorizationContext.getUsername()).getId();
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Application#Delete")
    public void DelayedDeleteApplication(Long applicationId) {
        ApplicationPO currentApplication = applicationRepository.selectById(applicationId);
        Throwables.validateRequest(currentApplication != null, "Application not found");
        Throwables.validateRequest(!currentApplication.getDeferredDelete(), "Application already marked for deletion");
        ApplicationPO applicationPO = new ApplicationPO();
        applicationPO.setDeferredDelete(true);
        applicationPO.setTobeDeletedAt(Date.from(Instant.now().plusSeconds(ProfileValues.APPLICATIONS_DELETED_DELAY)));
        applicationRepository.updateById(applicationPO, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("Application#Delete")
    public void cancelDeleteApplication(Long applicationId) {
        ApplicationPO currentApplication = applicationRepository.selectById(applicationId);
        Throwables.validateRequest(currentApplication != null, "Application not found");
        Throwables.validateRequest(currentApplication.getDeferredDelete(), "Application not marked for deletion");
        ApplicationPO applicationPO = new ApplicationPO();
        applicationPO.setDeferredDelete(false);
        applicationPO.setTobeDeletedAt(java.util.Date.from(Instant.MAX));
        applicationRepository.updateById(applicationPO, AuthorizationContext.getUsername());
    }
    @Transactional
    @PreAuthorize("Application#EditApplicationName")
    public void editApplicationName(Long applicationId, String newApplicationName){
        ApplicationPO currentApplication = applicationRepository.selectById(applicationId);
        Throwables.validateRequest(currentApplication != null, "Application not found");
        Throwables.validateRequest(isApplicationNameUnique(newApplicationName),
                "Application already exists");
        ApplicationPO applicationPO = new ApplicationPO();
        applicationPO.setApplicationName(newApplicationName);
        applicationRepository.updateById(applicationPO, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("Application#EditNickName")
    public void editApplicationNickname(Long applicationId, String newNickName) {
        ApplicationPO currentApplication = applicationRepository.selectById(applicationId);
        Throwables.validateRequest(currentApplication != null, "Application not found");
        ApplicationPO applicationPO = new ApplicationPO();
        applicationPO.setNickName(newNickName);
        applicationRepository.updateById(applicationPO, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("Application#EditComment")
    public void editApplicationComment(Long applicationId, String newComment) {
        ApplicationPO currentApplication = applicationRepository.selectById(applicationId);
        Throwables.validateRequest(currentApplication != null, "Application not found");
        ApplicationPO applicationPO = new ApplicationPO();
        applicationPO.setComment(newComment);
        applicationRepository.updateById(applicationPO, AuthorizationContext.getUsername());
    }


    @Transactional
    @NotForController
    public boolean isApplicationNameUnique(String applicationName) {
        return applicationRepository.selectByWrapper(
                new QueryWrapper<ApplicationPO>().eq(APPLICATION_NAME, applicationName)).isEmpty();
    }


    @Transactional
    @NotForController
    public void deleteApplication(Long applicationId, String operator) {
        clusterService.deleteClusterByApplicationId(applicationId, operator);
        namespaceService.deleteNamespaceByApplicationId(applicationId, operator);
        deleteAccessTokenByApplicationId(applicationId, operator);
    }

    @Transactional
    @NotForController
    public void deleteAccessTokenByApplicationId(Long applicationId, String operator) {
        accessTokenRepository.deleteByParentId(applicationId, operator);
    }


}
