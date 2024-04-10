package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.PartitionField;

import java.util.Date;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Audit`")
@GenerateRepository
public class AuditPO extends AbstractPersistObject {
    private Date operationTime;
    private Long operatorId;
    private String operatorName;
    @PartitionField
    private Long appId;
    private String appName;
    private Long namespaceId;
    private String namespaceName;

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AuditPO auditPO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(operationTime, auditPO.operationTime) && Objects.equals(operatorId, auditPO.operatorId) && Objects.equals(operatorName, auditPO.operatorName) && Objects.equals(appId, auditPO.appId) && Objects.equals(appName, auditPO.appName) && Objects.equals(namespaceId, auditPO.namespaceId) && Objects.equals(namespaceName, auditPO.namespaceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), operationTime, operatorId, operatorName, appId, appName, namespaceId, namespaceName);
    }

    @Override
    public String toString() {
        return "AuditPO{" +
                "operationTime=" + operationTime +
                ", operatorId=" + operatorId +
                ", operatorName='" + operatorName + '\'' +
                ", appId=" + appId +
                ", appName='" + appName + '\'' +
                ", namespaceId=" + namespaceId +
                ", namespaceName='" + namespaceName + '\'' +
                "} " + super.toString();
    }
}
