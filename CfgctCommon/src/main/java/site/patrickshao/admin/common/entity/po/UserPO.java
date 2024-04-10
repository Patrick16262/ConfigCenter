package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`User`")
@GenerateRepository
public class UserPO extends AbstractFullFieldsObject {
    @TableField("`userName`")
    private String userName;
    @TableField("`email`")
    private String email;
    @TableField("`userName`")
    private String nickname;
    @TableField("`password`")
    private String password;
    @TableField("`enabled`")
    private Boolean enabled;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserPO userPO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(userName, userPO.userName) && Objects.equals(email, userPO.email) && Objects.equals(nickname, userPO.nickname) && Objects.equals(password, userPO.password) && Objects.equals(enabled, userPO.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName, email, nickname, password, enabled);
    }

    @Override
    public String toString() {
        return "UserPO{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickname + '\'' +
                ", passWord='" + password + '\'' +
                ", enabled=" + enabled +
                "} " + super.toString();
    }
}
