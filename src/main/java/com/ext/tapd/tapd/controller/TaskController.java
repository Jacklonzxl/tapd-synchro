package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.common.status.SPriorityEnum;
import com.ext.tapd.tapd.common.status.StatusEnum;
import com.ext.tapd.tapd.dao.IterationRepository;
import com.ext.tapd.tapd.dao.StoryRepository;
import com.ext.tapd.tapd.dao.TaskRepository;
import com.ext.tapd.tapd.dao.WorkspaceRepository;
import com.ext.tapd.tapd.pojo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Value("${workspace.ids}")
    private String ids;
    @Value("${tapd.account}")
    private String account;
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    //初始化task
    @RequestMapping(value = "/initTask", method = RequestMethod.GET)
    public String initTask() {
        String[] idsStr = ids.split(",");
        for (String workspaceId : idsStr) {
            String url = "https://api.tapd.cn/tasks?workspace_id=" + workspaceId;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspaceId);
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
                        for (LinkedTreeMap map : vo.getData()) {
                            String gsonStr = g.toJson(map.get("Task"));
                            System.out.println(gsonStr);
                            Task task = g.fromJson(gsonStr, Task.class);
                            task.setStatus(StatusEnum.getValue(task.getStatus()));
                            task.setPriority(SPriorityEnum.getValue(task.getPriority()));
                            task.setProgress(task.getProgress() + "%");
                            Optional<Iteration> iteration = iterationRepository.findById(task.getIteration_id());
                            if (iteration.isPresent()) {
                                task.setIteration_name(iteration.get().getName());
                            }
                            Optional<Story> story = storyRepository.findById(task.getStory_id());
                            if (story.isPresent()) {
                                task.setStory_name(story.get().getName());
                            }
                            Optional<Workspace> workspace = workspaceRepository.findById(task.getWorkspace_id());
                            if (workspace.isPresent()) {
                                task.setWorkspace_name(workspace.get().getName());
                            }
                            taskRepository.save(task);
                        }
                    }
                }
            } else {
                url = url + "&limit=200";
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
                    for (LinkedTreeMap map : vo.getData()) {
                        String gsonStr = g.toJson(map.get("Task"));
                        System.out.println(gsonStr);
                        Task task = g.fromJson(gsonStr, Task.class);
                        task.setStatus(StatusEnum.getValue(task.getStatus()));
                        task.setPriority(SPriorityEnum.getValue(task.getPriority()));
                        task.setProgress(task.getProgress() + "%");
                        Optional<Iteration> iteration = iterationRepository.findById(task.getIteration_id());
                        if (iteration.isPresent()) {
                            task.setIteration_name(iteration.get().getName());
                        }
                        Optional<Story> story = storyRepository.findById(task.getStory_id());
                        if (story.isPresent()) {
                            task.setStory_name(story.get().getName());
                        }
                        Optional<Workspace> workspace = workspaceRepository.findById(task.getWorkspace_id());
                        if (workspace.isPresent()) {
                            task.setWorkspace_name(workspace.get().getName());
                        }
                        taskRepository.save(task);
                    }
                }
            }
        }
        return "执行成功";
    }

    @GetMapping("/log")
    public String getlog() {
        logger.debug("==============打印日志111===================");
        return "执行完成";
    }

    private int getCount(final String workspaceId) {
        String url = "https://api.tapd.cn/tasks/count?workspace_id=" + workspaceId;
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        //发送请求
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                new HttpEntity<>(null, headers),   //加入headers
                String.class);  //body响应数据接收类型
        System.out.println(ans);
        String gson = ans.getBody();
        System.out.println(gson);
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        Map map = vo.getData();
        int count = new Double((Double) map.get("count")).intValue();
        return count;
    }
}
