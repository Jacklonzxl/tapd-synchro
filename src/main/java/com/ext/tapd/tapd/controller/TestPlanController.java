package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.*;
import com.ext.tapd.tapd.pojo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/testPlan")
public class TestPlanController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TestPlanRepository testPlanRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private TestStatisticsRepository statisticsRepository;
    @Autowired
    private TestPlanStoryTcaseRepository tcaseRepository;

    private static Logger logger = LoggerFactory.getLogger(TestPlanController.class);

    @Value("${tapd.account}")
    private String account;

    @RequestMapping(value = "/initTestPlan", method = RequestMethod.GET)
    public String initTestPlan(){
        initTask();
        initRelaxtion();
        try {
            updateTestPlan();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "测试计划更新成功";
    }

    private String initTask() {
        testPlanRepository.truncateTable();
        List<Workspace> workspaces = (List<Workspace>) workspaceRepository.findAll();
        for (Workspace workspace : workspaces) {
            String url = "https://api.tapd.cn/test_plans?workspace_id=" + workspace.getId();
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspace.getId() + "");
            int totalPage = 0;
            if (count > 200) {
                totalPage = (count / 200) + 1;
                for (int i = 1; i <= totalPage; i++) {
                    url = url + "&limit=200&page=" + i;
                    //发送请求
                    HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                            new HttpEntity<>(null, headers),   //加入headers
                            String.class);  //body响应数据接收类型
                    String gson = ans.getBody();
                    Gson g = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();
                    ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                    if (vo.getData().size() > 0) {
                        vo.getData().stream().map(map -> g.toJson(map.get("TestPlan"))).forEach(gsonStr -> {
                            logger.info(gsonStr);
                            TestPlan testPlan = g.fromJson(gsonStr, TestPlan.class);
                            testPlanRepository.save(testPlan);
                        });
                    }
                }
            } else {
                url = url + "&limit=200";
                HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                        new HttpEntity<>(null, headers),   //加入headers
                        String.class);  //body响应数据接收类型
                String gson = ans.getBody();
                Gson g = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                if (vo.getData().size() > 0) {
                    vo.getData().stream().map(map -> g.toJson(map.get("TestPlan"))).forEach(gsonStr -> {
                        logger.info(gsonStr);
                        TestPlan testPlan = g.fromJson(gsonStr, TestPlan.class);
                        testPlanRepository.save(testPlan);
                    });
                }
            }
        }
        return "执行成功";
    }

    private int getCount(final String workspaceId) {
        String url = "https://api.tapd.cn/test_plans/count?workspace_id=" + workspaceId;
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        //发送请求
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                new HttpEntity<>(null, headers),   //加入headers
                String.class);  //body响应数据接收类型
        String gson = ans.getBody();
        logger.info(gson);
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        Map map = vo.getData();
        int count = new Double((Double) map.get("count")).intValue();
        return count;
    }

    private String updateTestPlan() throws ParseException {
        cleanStatistics();
        updateWorkSpaceName();
        Iterable<TestPlan> testPlans = testPlanRepository.findAll();
        Iterator<TestPlan> iterator = testPlans.iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date());
        Date today = sdf.parse(s);
        while (iterator.hasNext()) {
            TestPlan testPlan = (TestPlan) iterator.next();
            Date startDate = testPlan.getStart_date();
            Date endDate = testPlan.getEnd_date();
            Optional<Workspace> workspace = workspaceRepository.findById(testPlan.getWorkspace_id());
            String workspaceName = "";
            if(workspace.isPresent()) {
                workspaceName = workspace.get().getName();
            }

            //修改了计划时间，重新修改数据
            List<TestStatistics> statisticsList = statisticsRepository.findByName(testPlan.getName());
            if(!CollectionUtils.isEmpty(statisticsList)){
                for(TestStatistics testStatistics : statisticsList){
                    if(!isEffectiveDate(testStatistics.getPlanDate(),startDate,endDate)){
                        statisticsRepository.delete(testStatistics);
                        logger.info("[delete]:"+testStatistics.getName()+"--"+testStatistics.getPlanDate());
                    }
                }
            }

            while (startDate.compareTo(endDate) < 0) {
                TestStatistics statistics = new TestStatistics();
                statistics.setName(testPlan.getName());
                statistics.setPlanDate(startDate);
                statistics.setWorkspace_name(workspaceName);
                TestStatistics temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                if (startDate.compareTo(today) == 0 && Objects.nonNull(temp)) {
                    temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                    Implementation implementation = getRate(testPlan.getId(), testPlan.getWorkspace_id());
                    temp.setCoverage(transaleFloat(getCoverage(testPlan.getId(), implementation.getStory_count())));
                    temp.setImplementRate(transaleFloat(implementation.getExecuted_rate()));
                    temp.setPassRate(transaleFloat(getPassRate(implementation)));
                    temp.setWorkspace_name(workspaceName);
                    statisticsRepository.save(temp);
                }
                if (temp == null) {
                    statisticsRepository.save(statistics);
                }
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, 1);
                startDate = calendar.getTime();
            }
            if (startDate.compareTo(endDate) == 0) {
                TestStatistics statistics = new TestStatistics();
                statistics.setName(testPlan.getName());
                statistics.setPlanDate(startDate);
                statistics.setWorkspace_name(workspaceName);
                TestStatistics temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                if (temp == null) {
                    statisticsRepository.save(statistics);
                } else if (startDate.compareTo(today) == 0) {
                    temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                    Implementation implementation = getRate(testPlan.getId(), testPlan.getWorkspace_id());
                    temp.setCoverage(transaleFloat(getCoverage(testPlan.getId(), implementation.getStory_count())));
                    temp.setImplementRate(transaleFloat(implementation.getExecuted_rate()));
                    temp.setPassRate(transaleFloat(getPassRate(implementation)));
                    temp.setWorkspace_name(workspaceName);
                    statisticsRepository.save(temp);
                }
            }
        }
        return "更新测试计划表";
    }

    private Implementation getRate(Long testPlanId, Long workspaceId) {
        String url = "https://api.tapd.cn/test_plans/progress?id=" + testPlanId + "&workspace_id=" + workspaceId;
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        //发送请求
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                new HttpEntity<>(null, headers),   //加入headers
                String.class);  //body响应数据接收类型
        String gson = ans.getBody();
        logger.info(gson);
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        ResultImplementionEntity vo = g.fromJson(gson, ResultImplementionEntity.class);
        Implementation implementation = vo.getData();
        logger.info("===>" + implementation.getExecuted_rate());
        return implementation;
    }

    private String initRelaxtion() {
        tcaseRepository.truncateTable();
        List<Story> stories = (List<Story>) storyRepository.findAll();
        for (Story story : stories) {
            String url = "https://api.tapd.cn/stories/get_story_tcase?workspace_id=" + story.getWorkspace_id() + "&story_id=" + story.getId();
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            //发送请求
            HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                    new HttpEntity<>(null, headers),   //加入headers
                    String.class);  //body响应数据接收类型
            String gson = ans.getBody();
            logger.info(gson);
            Gson g = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            logger.info(gson);
            ResultTestPlanStoryTcaseRelationEntity vo = g.fromJson(gson, ResultTestPlanStoryTcaseRelationEntity.class);
            List<TcaseRelationEntity> tcaseRelations = vo.getData();
            if (!CollectionUtils.isEmpty(tcaseRelations)) {
                if (tcaseRelations.size() > 0) {
                    for (TcaseRelationEntity tcaseRelation : tcaseRelations) {
                        tcaseRepository.save(tcaseRelation.getTestPlanStoryTcaseRelation());
                    }
                }
            }

        }
        return "执行完成";
    }

    private String getCoverage(Long testPlanId, int storyCount) {
        if (storyCount == 0) {
            return "0%";
        }
        int storynum = tcaseRepository.countByTestPlanId(testPlanId);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) storynum / (float) storyCount * 100);
        return result + "%";
    }

    private String getPassRate(Implementation implementation) {
        if ((implementation.getTcase_count() - implementation.getStatus_counter().getUnexecuted()) <= 0) {
            return "0%";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) implementation.getStatus_counter().getPass() / (float) (implementation.getTcase_count() - implementation.getStatus_counter().getUnexecuted()) * 100);
        return result + "%";
    }

    private String transaleFloat(String present) {
        if (present.contains("%")) {
            present = present.replaceAll("%", "");
            Float f = Float.valueOf(present);
            return String.valueOf(f/100);
        }
       return "0";
    }

    /**
     *
     * @param nowTime   当前时间
     * @param startTime    开始时间
     * @param endTime   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    private boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 清理不存在的计划
     */
    private void cleanStatistics(){
        List<String> testPlans = testPlanRepository.findName();
        if(!CollectionUtils.isEmpty(testPlans)){
            List<TestStatistics> testStatistics =  statisticsRepository.findByNames(testPlans);
            if(!CollectionUtils.isEmpty(testStatistics)){
                for(TestStatistics statistics : testStatistics){
                    statisticsRepository.delete(statistics);
                }
            }
        }
    }

    /**
     * 更新项目名称
     */
    private void updateWorkSpaceName(){
        Iterable<TestPlan> testPlans = testPlanRepository.findAll();
        Iterator<TestPlan> iterator = testPlans.iterator();
        while (iterator.hasNext()) {
            TestPlan testPlan = (TestPlan) iterator.next();
            Optional<Workspace> workspace = workspaceRepository.findById(testPlan.getWorkspace_id());
            String workspaceName = "";
            if(workspace.isPresent()) {
                workspaceName = workspace.get().getName();
            }
            List<TestStatistics> statisticsList = statisticsRepository.findByName(testPlan.getName());
            if(!CollectionUtils.isEmpty(statisticsList)){
                for(TestStatistics testStatistics : statisticsList){
                    testStatistics.setWorkspace_name(workspaceName);
                    statisticsRepository.save(testStatistics);
                }
            }
        }
    }
}
