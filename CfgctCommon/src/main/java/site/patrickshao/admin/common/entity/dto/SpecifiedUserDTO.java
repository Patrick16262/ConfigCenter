package site.patrickshao.admin.common.entity.dto;

import com.google.common.base.MoreObjects;
import site.patrickshao.admin.common.entity.PojoWithIdentifier;

import java.util.List;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class SpecifiedUserDTO implements PojoWithIdentifier {
    private Long id;
    private String name;
    private String email;
    private String nickName;
    private List<SpecifiedRoleDTO> role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<SpecifiedRoleDTO> getRole() {
        return role;
    }

    public void setRole(List<SpecifiedRoleDTO> role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SpecifiedUserDTO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(nickName, that.nickName) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, nickName, role);
    }

    @Override
    public String toString() {
        return "UserDetailDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", role=" + role +
                '}';
    }

//    @Override
//    public String toString() {
//        return MoreObjects.toStringHelper(this)
//                .add("id", id)
//                .add("name", name)
//                .add("email", email)
//                .add("nickName", nickName)
//                .add("role", role)
//                .toString();
//    }

    /**
     * @return
     */
    @Override
    public Long getPojoIdentifier() {
        return id;
    }
}
