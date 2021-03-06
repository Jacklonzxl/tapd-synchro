package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.TestPlanStoryTcaseRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @author lx
 * 继承CrudRepository接口，<实体类, 主键类型>
 * JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
 */
public interface TestPlanStoryTcaseRepository extends CrudRepository<TestPlanStoryTcaseRelation, Long> {

    /**
     * 清空测试计划需求关系表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_test_plan_story_tcase", nativeQuery = true)
    void truncateTable();

    /**
     * 通过ID统计测试计划需求关系
     * @param id ID
     * @return 测试计划需求数
     */
    @Query(value = "select count(id) from (select id from t_test_plan_story_tcase b where test_plan_id=?1 GROUP BY story_id) as tt", nativeQuery = true)
    int countByTestPlanId(@Param("id") Long id);

}