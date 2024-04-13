package site.patrickshao.admin.biz.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
@Mapper
public interface AuthorizationInfoMapper {
    Integer selectApplicationIdByTableName(Long id, String tableName);
}
