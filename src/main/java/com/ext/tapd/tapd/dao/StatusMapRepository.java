package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.StatusMap;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
/**
 * @author lx
 * 继承CrudRepository接口，<实体类, 主键类型>
 * JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
 */
public interface StatusMapRepository extends CrudRepository<StatusMap, Integer> {

    /**
     * 查找状态关系表
     * @param code        编码
     * @param system      系统
     * @param workspace   组
     * @return  状态
     */
    StatusMap findByCodeAndSystemAndWorkspaceId(String code, String system, Long workspace);

    /**
     * 清空状态关系表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_status_map", nativeQuery = true)
    void truncateStatusMap();
}