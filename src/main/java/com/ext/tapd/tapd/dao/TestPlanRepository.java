package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.TestPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author lx
 * 继承CrudRepository接口，<实体类, 主键类型>
 * JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
 */
public interface TestPlanRepository extends CrudRepository<TestPlan, Long> {

    /**
     * 清空测试计划表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_test_plan", nativeQuery = true)
    void truncateTable();

    /**
     * 查找测试计划名称
     * @return 测试计划名称
     */
    @Query(value ="SELECT name from t_test_plan", nativeQuery = true)
    List<String> findName();

    List<TestPlan> findByWorkspaceId(Long workspaceId);
}