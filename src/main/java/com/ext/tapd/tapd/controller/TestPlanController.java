package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.TestPlanRepository;
import com.ext.tapd.tapd.dao.TestStatisticsRepository;
import com.ext.tapd.tapd.dao.WorkspaceRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private TestStatisticsRepository statisticsRepository;

    private static Logger logger = LoggerFactory.getLogger(TestPlanController.class);

    @Value("${tapd.account}")
    private String account;

    //初始化task
    @RequestMapping(value = "/initTestPlan", method = RequestMethod.GET)
    public String initTask() {
        List<Workspace> workspaces = (List<Workspace>) workspaceRepository.findAll();
        for (Workspace workspace : workspaces) {
            String url = "https://api.tapd.cn/test_plans?workspace_id=" + workspace.getId();
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspace.getId()+"");
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

    //初始化task
    @RequestMapping(value = "/updateTestPlan", method = RequestMethod.GET)
    public String updateTestPlan(){
        Iterable<TestPlan> testPlans = testPlanRepository.findAll();
        Iterator<TestPlan> iterator = testPlans.iterator();
        while (iterator.hasNext()){
            TestPlan testPlan = (TestPlan) iterator.next();
            Date startDate = testPlan.getStart_date();
            Date endDate = testPlan.getEnd_date();
            while (startDate.compareTo(endDate)<0){
                TestStatistics statistics = new TestStatistics();
                statistics.setName(testPlan.getName());
                statistics.setPlanDate(startDate);
                TestStatistics temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(),startDate);
                if(temp==null){
                    statisticsRepository.save(statistics);
                }
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, 1);
                startDate = calendar.getTime();
            }
            if(startDate.compareTo(endDate) == 0){
                TestStatistics statistics = new TestStatistics();
                statistics.setName(testPlan.getName());
                statistics.setPlanDate(startDate);
                TestStatistics temp = statisticsRepository.findByNameAndPlanDate(testPlan.getName(),startDate);
                if(temp==null){
                    statisticsRepository.save(statistics);
                } else {
                    temp.setImplementRate(getRate(testPlan.getId(), testPlan.getWorkspace_id()));
                    statisticsRepository.save(temp);
                }
            }
        }
        return "更新测试计划表";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getRate(Long testPlanId, Long workspaceId){
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
        logger.info("===>"+implementation.getExecuted_rate());
        return implementation.getExecuted_rate();
    }


}
