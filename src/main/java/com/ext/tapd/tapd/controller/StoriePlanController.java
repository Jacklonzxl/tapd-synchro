package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.StoryPlanRepository;
import com.ext.tapd.tapd.dao.TaskRepository;
import com.ext.tapd.tapd.pojo.Implementation;
import com.ext.tapd.tapd.pojo.StoryPlan;
import com.ext.tapd.tapd.pojo.Task;
import com.ext.tapd.tapd.pojo.TaskTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/storiePlan")
public class StoriePlanController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StoryPlanRepository storyPlanRepository;

    @RequestMapping(value = "/initTestPlan", method = RequestMethod.GET)
    public String initTask() {
        List<Map> list = taskRepository.findIterationName();
        for(Map map : list){
            StoryPlan oldPlan =storyPlanRepository.findByIterationName((String) map.get("iteration_name")).get(0);
            StoryPlan storyPlan = new StoryPlan();
            if(Objects.nonNull(oldPlan)){
                storyPlan.setId(oldPlan.getId());
            }
            storyPlan.setIterationName((String) map.get("iteration_name"));
            storyPlan.setTask_num((BigInteger) map.get("num"));
            String iterationname = (String) map.get("iteration_name");
            List<Map> typeEntities = taskRepository.findCountTaskType(iterationname);

            for (Map map1 : typeEntities){
                System.out.println(map1.get("name")+":"+map1.get("totalnum"));
                String name = (String) map1.get("name")==null?"":(String) map1.get("name");
                BigInteger totalnum = (BigInteger) map1.get("totalnum");
                List<Integer> numlist = taskRepository.CountFinishNum(iterationname,name);
                int num = numlist.size()>0?numlist.get(0):0;
                switch (name){
                    case "开发":
                        storyPlan.setDevelopment(getPresent(totalnum,num));
                        break;
                    case "需求评审":
                        storyPlan.setRequirements_review(getPresent(totalnum,num));
                        break;
                    case "技术架构设计":
                        storyPlan.setTechnical_architecture_design(getPresent(totalnum,num));
                        break;
                    case "技术方案设计":
                        storyPlan.setTechnical_scheme_design(getPresent(totalnum,num));
                        break;
                    case "接口设计":
                        storyPlan.setInterface_design(getPresent(totalnum,num));
                        break;
                    case "API设计":
                        storyPlan.setApi_design(getPresent(totalnum,num));
                        break;
                    case "UI设计":
                        storyPlan.setUi_design(getPresent(totalnum,num));
                        break;
                    case "视觉设计":
                        storyPlan.setView_design(getPresent(totalnum,num));
                        break;
                    case "其他":
                        storyPlan.setOther(getPresent(totalnum,num));
                        break;
                    default:
                        storyPlan.setEmpty(getPresent(totalnum,num));
                        break;
                }
            }
            storyPlanRepository.save(storyPlan);
        }
        return "";
    }

    private String getPresent(BigInteger totalnum, int num) {
        if (totalnum.intValue() <= 0) {
            return "0%";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num / (float) totalnum.intValue() * 100);
        return result + "%";
    }
}
