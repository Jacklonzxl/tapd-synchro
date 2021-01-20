package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.TestStatistics;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
/**
 * @author lx
 * 继承CrudRepository接口，<实体类, 主键类型>
 * JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
 */
public interface TestStatisticsRepository extends CrudRepository<TestStatistics, Long> {

    /**
     * 清空测试计划统计表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_test_statistics", nativeQuery = true)
    void truncateTable();

    /**
     * 根据测试计划名称和日期查找测试计划统计
     * @param name 测试计划名称
     * @param planDate 日期
     * @return 测试计划统计
     */
    TestStatistics findByNameAndPlanDate(String name, Date planDate);

    /**
     * 根据测试计划名称查找测试计划统计
     * @param name 测试计划名称
     * @return 测试计划统计
     */
    List<TestStatistics> findByName(String name);

    /**
     * 通过测试计划名称查找已改变的测试计划
     * @param names 测试计划名称
     * @return 测试计划统计
     */
    @Query(value = "select * from t_test_statistics where name not in (?1)", nativeQuery = true)
    List<TestStatistics> findByNames(@Param("names") List<String> names);
}