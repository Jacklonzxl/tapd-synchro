package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.StoryPlan;
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
public interface StoryPlanRepository extends CrudRepository<StoryPlan, Integer> {

    /**
     * 通过迭代名称查找需求计划
     * @param name 迭代名称
     * @return 相关的需求计划
     */
    List<StoryPlan> findByIterationName(String name);

    /**
     * 清空需求计划表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_story_plan", nativeQuery = true)
    void truncateTable();
}