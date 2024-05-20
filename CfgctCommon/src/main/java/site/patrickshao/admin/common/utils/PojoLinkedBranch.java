package site.patrickshao.admin.common.utils;

import site.patrickshao.admin.common.entity.LinkablePojo;

import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/14
 */
public class PojoLinkTable<T extends LinkablePojo> {

    public static <T extends LinkablePojo> PojoLinkTable<T> makePojoLinkTable(List<T> pojo) {

    }
    private PojoLinkTable() {
    }
}
