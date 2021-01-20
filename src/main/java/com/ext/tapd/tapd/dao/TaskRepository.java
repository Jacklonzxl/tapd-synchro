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

/**
 * @author lx
 * 继承CrudRepository接口，<实体类, 主键类型>
 * JPA根据实体类的类名去对应表名（可以使用@Entity的name属性？@Table进行修改）
 */
public interface TaskRepository extends CrudRepository<Task, Integer> {

    /**
     * 清空任务表
     */
    @Transactional(rollbackOn = {Exception.class})
    @Modifying
    @Query(value = "truncate table t_tasks", nativeQuery = true)
    void truncateTable();

    /**
     * 根据迭代名称分类查找任务类型
     * @param iterationname 迭代名称
     * @return 任务类型和统计
     */
    @Query(value = "select custom_field_one as name,count(id) as totalnum from t_tasks where iteration_name = :iterationname GROUP BY custom_field_one", nativeQuery = true)
    List<Map> findCountTaskType(@Param("iterationname") String iterationname);

    /**
     * 根据迭代名称统计任务完成数
     * @param iterationname 迭代名称
     * @param name 任务类型
     * @return 任务完成数
     */
    @Query(value = "SELECT count(id) FROM t_tasks where iteration_name = :iterationname and custom_field_one = :name AND `status` = '已完成' GROUP BY `status`", nativeQuery = true)
    List<Integer> CountFinishNum(@Param("iterationname") String iterationname,@Param("name") String name);

    /**
     * 根据迭代名称统计任务数
     * @param iterationName 迭代名称
     * @return 任务数
     */
    @Query(value = "SELECT count(id) as num from t_tasks where iteration_name = :iterationName", nativeQuery = true)
    BigInteger countByIterationName(@Param("iterationName") String iterationName);

    /**
     * 根据迭代名称统计完成数
     * @param iterationname 迭代名称
     * @return 完成数
     */
    @Query(value = "SELECT count(id) FROM t_tasks where iteration_name = :iterationname AND `status` = '已完成' GROUP BY `status`", nativeQuery = true)
    Integer countByIterationNameFinishNum(@Param("iterationname") String iterationname);
}