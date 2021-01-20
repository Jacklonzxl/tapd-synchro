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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 全量初始化数据表
 * @author lx
 */
@Controller
@RequestMapping("/init")
public class InitController {
    private static Logger logger = LoggerFactory.getLogger(InitController.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StoryCategoriesRepository storyCategoriesRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private StatusMapRepository statusMapRepository;

    @Value("${company.id}")
    private String companyId;
    @Value("${project.ids}")
    private String projectIds;
    @Value("${tapd.account}")
    private String account;

    @RequestMapping(value = "/initTable", method = RequestMethod.GET)
    public String initTable() {
        return "index";
    }

    @RequestMapping(value = "/initData", method = RequestMethod.GET)
    @ResponseBody
    @Transactional(rollbackOn = {Exception.class})
    public String initData() {
        logger.info("=========================>初始化数据表开始");
        logger.info("=========================>清空项目表");
        workspaceRepository.truncateTable();
        initWorkspace();
        logger.info("=========================>清空需求分类表");
        storyCategoriesRepository.truncateTable();
        initStoryCategories();
        logger.info("=========================>清空状态表");
        statusMapRepository.truncateStatusMap();
        initStatusMap();
        logger.info("=========================>初始化数据表结束");
        return "基础数据更新完成";
    }

    public void initStatusMap() {
        List<Workspace> workspaces;
        workspaces = (List<Workspace>) workspaceRepository.findAll();
        String[] systems;
        systems = new String[]{"story", "bug"};
        workspaces.forEach(workspace -> {
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            Arrays.stream(systems).forEach(system -> {
                AtomicReference<String> url = new AtomicReference<>("https://api.tapd.cn/workflows/status_map?system=" + system + "&workspace_id=" + workspace.getId());
                HttpHeaders headers = new HttpHeaders();
                headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
                /* url 发送请求地址 GET请求 加入headers body响应数据接收类型*/
                HttpEntity<String> ans = restTemplate.exchange(url.get(), HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                String gson = ans.getBody();
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ResultStatusEntity vo = g.fromJson(gson, ResultStatusEntity.class);
                logger.info("=========================>初始化状态表条数:"+vo.getData().size());
                if (vo.getData().size() > 0) {
                    for (Object key : vo.getData().keySet()) {
                        StatusMap statusMap = new StatusMap();
                        statusMap.setCode((String) key);
                        statusMap.setName((String) vo.getData().get(key));
                        statusMap.setSystem(system);
                        statusMap.setWorkspaceId(workspace.getId());
                        statusMapRepository.save(statusMap);
                    }
                }
            });
        });
        logger.info("=========================>初始化状态表完成");
    }

    public void initWorkspace() {
        String url = String.format("https://api.tapd.cn/workspaces/projects?company_id=%s", companyId);
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        /* url 发送请求地址 GET请求 加入headers body响应数据接收类型*/
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
        String gson = ans.getBody();
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ResultEntity vo = g.fromJson(gson, ResultEntity.class);
        logger.info("=========================>初始化项目表条数:"+vo.getData().size());
        if (vo.getData().size() > 0) {
            vo.getData().stream().map(map -> g.toJson(map.get("Workspace"))).forEach(gsonStr -> {
                Workspace workspace = g.fromJson(gsonStr, Workspace.class);
                List<String> list = Arrays.asList(projectIds.split(","));
                if (list.contains(String.valueOf(workspace.getId()))){ workspaceRepository.save(workspace);}
            });
        }
        logger.info("=========================>初始化项目表完成");
    }

    public void initStoryCategories() {
        String[] idsStr = projectIds.split(",");
        for (String workspaceId : idsStr) {
            String url = "https://api.tapd.cn/story_categories?workspace_id=" + workspaceId;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspaceId);
            logger.info("=========================>初始化需求分类表条数:"+count);
            int totalPage;
            if (count > 200) {
                totalPage = (count / 200) + 1;
                for (int i = 1; i <= totalPage; i++) {
                    url = url + "&limit=200&page=" + i;
                    /* url 发送请求地址 GET请求 加入headers body响应数据接收类型*/
                    HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                    String gson = ans.getBody();
                    Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                    if (vo.getData().size() > 0) {
                        vo.getData().stream().map(map -> g.toJson(map.get("Category"))).forEach(gsonStr -> {
                            StoryCategories storyCategories = g.fromJson(gsonStr, StoryCategories.class);
                            storyCategoriesRepository.save(storyCategories);
                        });
                    }
                }
            } else {
                url = url + "&limit=200";
                /* url 发送请求地址 GET请求 加入headers body响应数据接收类型*/
                HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                String gson = ans.getBody();
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                if (vo.getData().size() > 0) {
                    vo.getData().stream().map(map -> g.toJson(map.get("Category"))).forEachOrdered(gsonStr -> {
                        StoryCategories storyCategories = g.fromJson(gsonStr, StoryCategories.class);
                        storyCategoriesRepository.save(storyCategories);
                    });
                }
            }
        }
        logger.info("=========================>初始化需求分类表完成");
    }

    private int getCount(final String workspaceId) {
        AtomicReference<String> url = new AtomicReference<>("https://api.tapd.cn/story_categories/count?workspace_id=" + workspaceId);
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        /* url 发送请求地址 GET请求 加入headers body响应数据接收类型*/
        HttpEntity<String> ans = restTemplate.exchange(url.get(), HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
        String gson = ans.getBody();
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        return ((Double) vo.getData().get("count")).intValue();
    }
}
