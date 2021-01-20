package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.Story;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * @author lx
 * 继承CrudRepository接口，<实体类, 主键类型>
 * JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
 */
public interface StoryRepository extends CrudRepository<Story, Long> {

    /**
     * 清空需求表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_stories", nativeQuery = true)
    void truncateStories();

    /**
     * 根据迭代名称统计需求数量
     * @param iterationName 迭代名称
     * @return 统计需求数量
     */
    @Query(value = "SELECT count(id) FROM t_stories WHERE iteration_name = :iterationName AND `status` not in ('删除','已拒绝')", nativeQuery = true)
    Integer countByIterationName(@Param("iterationName") String iterationName);
}