package site.patrickshao.admin.common.entity.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public class UserPermissionBO implements Serializable {
    private Long id;
    private String username;
    private List<Long> permissionIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserPermissionBO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(permissionIds, that.permissionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, permissionIds);
    }

    @Override
    public String toString() {
        return "UserPermissionBO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", permissionIds=" + permissionIds +
                '}';
    }
}
