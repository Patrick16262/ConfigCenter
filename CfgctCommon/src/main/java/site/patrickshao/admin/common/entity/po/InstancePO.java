package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;

import java.util.Date;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Instance`")
@GenerateRepository
public class InstancePO extends AbstractBasicFieldsObject {
    @TableField("`ip`")
    private String ip;
    private Date lastConnectTime;
    private String applicationName;
    private String clusterName;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLastConnectTime() {
        return lastConnectTime;
    }

    public void setLastConnectTime(Date lastConnectTime) {
        this.lastConnectTime = lastConnectTime;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof InstancePO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(ip, that.ip) && Objects.equals(lastConnectTime, that.lastConnectTime) && Objects.equals(applicationName, that.applicationName) && Objects.equals(clusterName, that.clusterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ip, lastConnectTime, applicationName, clusterName);
    }

    @Override
    public String toString() {
        return "InstancePO{" +
                "ip='" + ip + '\'' +
                ", lastConnectTime=" + lastConnectTime +
                ", applicationName='" + applicationName + '\'' +
                ", clusterName='" + clusterName + '\'' +
                "} " + super.toString();
    }
}
