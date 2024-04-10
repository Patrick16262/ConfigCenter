package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`ModificationMessage`")
@GenerateRepository
public class ModificationMessagePO extends AbstractBasicFieldsObject {
    private String operationType;
    private String operationArgument;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationArgument() {
        return operationArgument;
    }

    public void setOperationArgument(String operationArgument) {
        this.operationArgument = operationArgument;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ModificationMessagePO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(operationType, that.operationType) && Objects.equals(operationArgument, that.operationArgument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), operationType, operationArgument);
    }

    @Override
    public String toString() {
        return "ModificationMessagePO{" +
                "operationType='" + operationType + '\'' +
                ", operationArgument='" + operationArgument + '\'' +
                "} " + super.toString();
    }
}
