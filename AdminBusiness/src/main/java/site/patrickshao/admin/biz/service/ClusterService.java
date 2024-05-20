package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.consts.ProfileValues;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.po.ClusterPO;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@ParametersAreNonnullByDefault
public class ClusterService {
    @Autowired
    private BranchService branchService;
    @Autowired
    private DefaultRepository<ClusterPO> clusterRepository;

    @Transactional
    @PreAuthorize("Application#Visible")
    public List<ClusterPO> getClusterList(Long applicationId) {
        return clusterRepository.selectByParentId(applicationId);
    }

    @Transactional
    @PreAuthorize("Cluster#CreateCluster")
    public Long createCluster(ClusterPO clusterPO) {
        Throwables.validateRequest(clusterPO.getApplicationId() != null, "Application id cannot be null");
        Throwables.validateRequest(clusterPO.getName() != null, "Cluster name cannot be null");
        Throwables.validateRequest(clusterRepository.selectByWrapper(
                new QueryWrapper<ClusterPO>()
                        .eq(DataBaseFields.Cluster.APPLICATION_ID, clusterPO.getApplicationId())
                        .eq(DataBaseFields.Cluster.NAME, clusterPO.getName())
        ).isEmpty(), "Cluster already exists");
        return clusterRepository.create(clusterPO, AuthorizationContext.getUsername()).getId();
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Cluster#DeleteClusterDeferred")
    public void deleteClusterDeferred(Long clusterId) {
        ClusterPO clusterPO = clusterRepository.selectById(clusterId);
        Throwables.validateRequest(clusterPO != null, "Cluster not found");
        Throwables.validateRequest(!clusterPO.getDeferredDelete(), "Cluster already marked for deletion");
        clusterPO = new ClusterPO();
        clusterPO.setId(clusterId);
        clusterPO.setDeferredDelete(true);
        clusterPO.setTobeDeletedAt(Date.from(Instant.now().plusSeconds(ProfileValues.CLUSTER_DELETED_DELAY)));
        clusterRepository.updateById(clusterPO, AuthorizationContext.getUsername());
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Cluster#DeleteClusterDeferred")
    public void cancelDeleteCluster(Long clusterId) {
        ClusterPO clusterP = clusterRepository.selectById(clusterId);
        Throwables.validateRequest(clusterP != null, "Cluster not found");
        Throwables.validateRequest(clusterP.getDeferredDelete(), "Cluster not marked for deletion");
        ClusterPO clusterPO = new ClusterPO();
        clusterPO.setId(clusterId);
        clusterPO.setDeferredDelete(false);
        clusterPO.setTobeDeletedAt(Date.from(Instant.MAX));
        clusterRepository.updateById(clusterPO, AuthorizationContext.getUsername());
    }


    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Cluster#EditClusterName")
    public void editClusterName(Long clusterId, String newName) {
        ClusterPO clusterP = clusterRepository.selectById(clusterId);
        Throwables.validateRequest(clusterP != null, "Cluster not found");
        Throwables.validateRequest(isClusterNameUnique(clusterP.getApplicationId(), newName),
                "Cluster name already exists");
        ClusterPO clusterPO = new ClusterPO();
        clusterPO.setId(clusterId);
        clusterPO.setName(newName);
        clusterRepository.updateById(clusterPO, AuthorizationContext.getUsername());
    }


    @Transactional
    @PreAuthorize("Cluster#EditClusterComment")
    public void editClusterComment(Long clusterId, String newComment) {
        ClusterPO clusterPO = clusterRepository.selectById(clusterId);
        Throwables.validateRequest(clusterPO != null, "Cluster not found");
        clusterPO = new ClusterPO();
        clusterPO.setId(clusterId);
        clusterPO.setComment(newComment);
        clusterRepository.updateById(clusterPO, AuthorizationContext.getUsername());
    }

    @NotForController
    public boolean isClusterNameUnique(Long applicationId, String clusterName) {
        return clusterRepository.selectByWrapper(
                new QueryWrapper<ClusterPO>()
                        .eq(DataBaseFields.Cluster.APPLICATION_ID, applicationId)
                        .eq(DataBaseFields.Cluster.NAME, clusterName)).isEmpty();
    }

    @NotForController
    public void deleteCluster(Long clusterId, String operator) {
        branchService.deleteBranchByClusterId(clusterId, operator);
        clusterRepository.deleteById(clusterId, operator);
    }

    @NotForController
    public void deleteClusterByApplicationId(Long applicationId, String operator) {
        List<ClusterPO> clusters = clusterRepository.selectByParentId(applicationId);
        clusters.forEach(i -> branchService.deleteBranchByClusterId(i.getId(), operator));
        clusterRepository.deleteByParentId(applicationId, operator);
    }
}
