package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.IterationRepository;
import com.ext.tapd.tapd.dao.StoryPlanRepository;
import com.ext.tapd.tapd.dao.StoryRepository;
import com.ext.tapd.tapd.dao.TaskRepository;
import com.ext.tapd.tapd.pojo.Iteration;
import com.ext.tapd.tapd.pojo.StoryPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/storiePlan")
public class StoriePlanController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StoryPlanRepository storyPlanRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private IterationRepository iterationRepository;

    @RequestMapping(value = "/initStoriePlan", method = RequestMethod.GET)
    public String initTask() {
        Iterable<Iteration> iterable = iterationRepository.findAll();
        Iterator<Iteration> iterator =  iterable.iterator();
        while (iterator.hasNext()){
            Iteration iteration = iterator.next();
            String iterationName = iteration.getName();
            BigInteger totalTaskNum = taskRepository.countByIterationName(iterationName);
            List<StoryPlan> planList = storyPlanRepository.findByIterationName(iterationName);
            StoryPlan storyPlan = new StoryPlan();
            if (!CollectionUtils.isEmpty(planList) && planList.size() > 0) { storyPlan.setId(planList.get(0).getId()); }
            storyPlan.setIterationName(iterationName);
            Integer storynum = storyRepository.countByIterationName(iterationName);
            if(storynum==null) storynum = 0;
            if (!StringUtils.isEmpty(iterationName)) {
                String[] types = iterationName.split("-");
                String[] etypes = iterationName.split("【");
                if (types != null && types.length == 2) {
                    storyPlan.setStoryType(types[0]);
                    storyPlan.setTaskType(types[1]);
                } else if (etypes != null && etypes.length == 2) {
                    storyPlan.setStoryType(etypes[0]);
                    storyPlan.setTaskType("【"+etypes[1]);
                }
            }
            storyPlan.setStory_num(storynum);
            storyPlan.setTask_num(totalTaskNum);
            List<Map> typeEntities = taskRepository.findCountTaskType(iterationName);
            int emptynum = 0;
            int emptytotalnum = 0;
            for (Map map1 : typeEntities) {
                String name = map1.get("name") == null ? "" : (String) map1.get("name");
                BigInteger totalnum = (BigInteger) map1.get("totalnum");
                List<Integer> numlist = taskRepository.CountFinishNum(iterationName, name);
                int num = numlist.size() > 0 ? numlist.get(0) : 0;
                switch (name) {
                    case "开发":
                        storyPlan.setDevelopment(getPresent(totalnum, num));
                        break;
                    case "需求评审":
                        storyPlan.setRequirements_review(getPresent(totalnum, num));
                        break;
                    case "技术架构设计":
                        storyPlan.setTechnical_architecture_design(getPresent(totalnum, num));
                        break;
                    case "技术方案设计":
                        storyPlan.setTechnical_scheme_design(getPresent(totalnum, num));
                        break;
                    case "接口设计":
                        storyPlan.setInterface_design(getPresent(totalnum, num));
                        break;
                    case "API设计":
                        storyPlan.setApi_design(getPresent(totalnum, num));
                        break;
                    case "UI设计":
                        storyPlan.setUi_design(getPresent(totalnum, num));
                        break;
                    case "视觉设计":
                        storyPlan.setView_design(getPresent(totalnum, num));
                        break;
                    case "其他":
                        storyPlan.setOther(getPresent(totalnum, num));
                        break;
                    default:
                        emptynum += num;
                        emptytotalnum += totalnum.intValue();
                        storyPlan.setEmpty(getPresent(BigInteger.valueOf(emptytotalnum), emptynum));
                        break;
                }
            }
            Integer finishnum = taskRepository.countByIterationNameFinishNum(iterationName);
            if(finishnum==null) finishnum = 0;
            storyPlan.setTotal_finish(getPresent(totalTaskNum, finishnum));
            storyPlanRepository.save(storyPlan);
        }
        return "更新数据成功";
    }

    private String getPresent(BigInteger totalnum, int num) {
        if (totalnum.intValue() <= 0) {
            return "0";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num / (float) totalnum.intValue() * 100);
//        return num + "/" + totalnum;
        Float f = Float.valueOf(result);
        return String.valueOf(f/100);
    }
}
