package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.common.status.SPriorityEnum;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

/**
 * 更新需求表
 * @author lx
 */
@RestController
@RequestMapping("/storie")
public class StorieController {
    private static Logger logger = LoggerFactory.getLogger(StorieController.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private StoryCategoriesRepository storyCategoriesRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private StatusMapRepository statusMapRepository;

    @Value("${workspace.ids}")
    private String ids;
    @Value("${tapd.account}")
    private String account;

    /**初始化需求表*/
    @RequestMapping(value = "/initStorie", method = RequestMethod.GET)
    @Transactional(rollbackOn = {Exception.class})
    public String initStorie() {
        logger.info("=========================>初始化需求表开始");
        storyRepository.truncateStories();
        String[] idsStr = ids.split(",");
        for (String workspaceId : idsStr) {
            String url = "https://api.tapd.cn/stories?workspace_id=" + workspaceId;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspaceId);
            logger.info("=========================>初始化需求表条数:"+count);
            int totalPage = 0;
            if (count > 200) {
                totalPage = (count / 200) + 1;
                for (int i = 1; i <= totalPage; i++) {
                    url = url + "&limit=200&page=" + i;
                    HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                    String gson = ans.getBody();
                    Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                    if (vo.getData().size() > 0) {
                        vo.getData().stream().map(map -> g.toJson(map.get("Story"))).forEach(gsonStr -> {
                            Story story = g.fromJson(gsonStr, Story.class);
                            story.setPriority(SPriorityEnum.getValue(story.getPriority()));
                            Optional<Iteration> iteration = iterationRepository.findById(story.getIteration_id());
                            iteration.ifPresent(value -> story.setIterationName(value.getName()));
                            Optional<StoryCategories> categories = storyCategoriesRepository.findById(story.getCategory_id());
                            categories.ifPresent(storyCategories -> story.setCategory_name(storyCategories.getName()));
                            Optional<Workspace> workspace = workspaceRepository.findById(story.getWorkspace_id());
                            workspace.ifPresent(value -> story.setWorkspace_name(value.getName()));
                            StatusMap statusMap = statusMapRepository.findByCodeAndSystemAndWorkspaceId(story.getStatus(), "story", story.getWorkspace_id());
                            if (Objects.nonNull(statusMap)) {
                                story.setStatus(statusMap.getName());
                            }
                            storyRepository.save(story);
                        });
                    }
                }
            } else {
                url = url + "&limit=200";
                HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                String gson = ans.getBody();
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                if (vo.getData().size() > 0) {
                    vo.getData().stream().map(map -> g.toJson(map.get("Story"))).forEach(gsonStr -> {
                        Story story = g.fromJson(gsonStr, Story.class);
                        story.setPriority(SPriorityEnum.getValue(story.getPriority()));
                        Optional<Iteration> iteration = iterationRepository.findById(story.getIteration_id());
                        iteration.ifPresent(value -> story.setIterationName(value.getName()));
                        Optional<StoryCategories> categories = storyCategoriesRepository.findById(story.getCategory_id());
                        categories.ifPresent(storyCategories -> story.setCategory_name(storyCategories.getName()));
                        Optional<Workspace> workspace = workspaceRepository.findById(story.getWorkspace_id());
                        workspace.ifPresent(value -> story.setWorkspace_name(value.getName()));
                        StatusMap statusMap = statusMapRepository.findByCodeAndSystemAndWorkspaceId(story.getStatus(), "story", story.getWorkspace_id());
                        if (Objects.nonNull(statusMap)) {
                            story.setStatus(statusMap.getName());
                        }
                        storyRepository.save(story);
                    });
                }
            }
        }
        logger.info("=========================>初始化迭代表结束");
        return "执行成功";
    }

    private int getCount(final String workspaceId) {
        String url = "https://api.tapd.cn/stories/count?workspace_id=" + workspaceId;
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
        String gson = ans.getBody();
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        return ((Double) vo.getData().get("count")).intValue();
    }
}
