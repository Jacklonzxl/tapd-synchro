package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.TestStatistics;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

// 继承CrudRepository接口，<实体类, 主键类型>
// JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
public interface TestStatisticsRepository extends CrudRepository<TestStatistics, Long> {

    @Transactional
    @Modifying
    @Query(value = "truncate table t_test_statistics", nativeQuery = true)
    public void truncateTable();

    public TestStatistics findByNameAndPlanDate(String name, Date planDate);

    public List<TestStatistics> findByName(String name);

    @Query(value = "select * from t_test_statistics where name not in (?1)", nativeQuery = true)
    public List<TestStatistics> findByNames(@Param("names") List<String> names);
}