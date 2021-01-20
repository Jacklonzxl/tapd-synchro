package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.Workspace;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * @author lx
 * 继承CrudRepository接口，<实体类, 主键类型>
 * JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
 */
public interface WorkspaceRepository extends CrudRepository<Workspace, Long> {

    /**
     * 清空项目表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_workspace", nativeQuery = true)
    void truncateTable();
}