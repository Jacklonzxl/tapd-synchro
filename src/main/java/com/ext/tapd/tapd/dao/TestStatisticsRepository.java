package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.TestStatistics;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;

// 继承CrudRepository接口，<实体类, 主键类型>
// JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
public interface TestStatisticsRepository extends CrudRepository<TestStatistics, Long> {

    @Transactional
    @Modifying
    @Query(value = "truncate table t_test_statistics", nativeQuery = true)
    public void truncateTable();

    public TestStatistics findByNameAndPlanDate(String name, Date planDate);
}