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
    public String initTask() {
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

    @RequestMapping(value = "/updateTestPlan", method = RequestMethod.GET)
    public String updateTestPlan() throws ParseException {
        Iterable<TestPlan> testPlans = testPlanRepository.findAll();
        Iterator<TestPlan> iterator = testPlans.iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date());
        Date today = sdf.parse(s);
        while (iterator.hasNext()) {
            TestPlan testPlan = (TestPlan) iterator.next();
            Date startDate = testPlan.getStart_date();
            Date endDate = testPlan.getEnd_date();
            while (startDate.compareTo(endDate) < 0) {
                TestStatistics statistics = new TestStatistics();
                statistics.setName(testPlan.getName());
                statistics.setPlanDate(startDate);
                TestStatistics temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                if (startDate.compareTo(today) == 0 && Objects.nonNull(temp)) {
                    temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                    Implementation implementation = getRate(testPlan.getId(), testPlan.getWorkspace_id());
                    temp.setCoverage(getCoverage(testPlan.getId(), implementation.getStory_count()));
                    temp.setImplementRate(implementation.getExecuted_rate());
                    temp.setPassRate(getPassRate(implementation));
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
                TestStatistics temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                if (temp == null) {
                    statisticsRepository.save(statistics);
                } else if (startDate.compareTo(today) == 0) {
                    temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(), startDate);
                    Implementation implementation = getRate(testPlan.getId(), testPlan.getWorkspace_id());
                    temp.setCoverage(getCoverage(testPlan.getId(), implementation.getStory_count()));
                    temp.setImplementRate(implementation.getExecuted_rate());
                    temp.setPassRate(getPassRate(implementation));
                    statisticsRepository.save(temp);
                }
            }
        }
        return "更新测试计划表";
    }


    public Implementation getRate(Long testPlanId, Long workspaceId) {
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


    @RequestMapping(value = "/getRelaxtion", method = RequestMethod.GET)
    public String getRelaxtion() {
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
        if(storyCount==0){
            return "0%";
        }
        int storynum = tcaseRepository.countByTestPlanId(testPlanId);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) storynum / (float) storyCount * 100);
        return result + "%";
    }

    private String getPassRate(Implementation implementation) {
        if((implementation.getTcase_count()-implementation.getStatus_counter().getUnexecuted())<=0){
            return "0%";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) implementation.getStatus_counter().getPass() / (float) (implementation.getTcase_count()-implementation.getStatus_counter().getUnexecuted()) * 100);
        return result + "%";
    }
}
