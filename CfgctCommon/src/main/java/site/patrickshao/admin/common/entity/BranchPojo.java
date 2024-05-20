package site.patrickshao.admin.common.entity;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/14
 */
public interface BranchPojo extends PojoWithIdentifier{
    Long getPreviousId();
    Long getNextId();
    Long getBranchId();
}
