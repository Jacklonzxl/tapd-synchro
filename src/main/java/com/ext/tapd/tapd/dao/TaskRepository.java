package com.ext.tapd.tapd.dao;

import com.ext.tapd.tapd.pojo.Task;
import com.ext.tapd.tapd.pojo.TaskTypeEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

// 继承CrudRepository接口，<实体类, 主键类型>
// JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Transactional
    @Modifying
    @Query(value = "truncate table t_tasks", nativeQuery = true)
    public void truncateTable();

    @Query(value = "SELECT iteration_name,count(id) as num FROM t_tasks GROUP BY iteration_name",  nativeQuery = true)
    public List<Map> findIterationName();

    @Modifying()
    @Query(value = "select custom_field_one as name,count(id) as totalnum from t_tasks where iteration_name = :iterationname GROUP BY custom_field_one", nativeQuery = true)
    public List<Map> findCountTaskType(@Param("iterationname") String iterationname);

    @Modifying()
    @Query(value = "SELECT count(id) FROM t_tasks where iteration_name = :iterationname and custom_field_one = :name AND `status` = '已完成' GROUP BY `status`", nativeQuery = true)
    public List<Integer> CountFinishNum(@Param("iterationname") String iterationname,@Param("name") String name);

    @Query(value = "SELECT count(id) as num from t_tasks where iteration_name = :iterationName", nativeQuery = true)
    BigInteger countByIterationName(String iterationName);
}