package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.common.status.*;
import com.ext.tapd.tapd.dao.*;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.nonNull;

/**
 * 全量初始化数据表
 */
@Controller
@RequestMapping("/init")
public class InitController {
    private static Logger logger = LoggerFactory.getLogger(InitController.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private BugRepository bugRepository;
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
    @Autowired
    private TestPlanRepository testPlanRepository;

    @Value("${company.id}")
    private String companyId;
    @Value("${workspace.ids}")
    private String ids;
    @Value("${tapd.account}")
    private String account;
    private final HttpHeaders headers = new HttpHeaders();

    @RequestMapping(value = "/initTable", method = RequestMethod.GET)
    public String initTable(Model model) {
        return "index";
    }

    @RequestMapping(value = "/initData", method = RequestMethod.GET)
    @ResponseBody
    public String initData() {
        logger.info("=======初始化数据表开始=======");
        logger.info("=========================>清空公司表======================");
        workspaceRepository.truncateTable();
        initWorkspace();
        logger.info("=========================>清空需求分类表======================");
        storyCategoriesRepository.truncateTable();
        initStoryCategories();
        logger.info("=========================>清空状态表======================");
        statusMapRepository.truncateStatusMap();
        initStatusMap();
//        logger.info("=========================>清空任务表======================");
//        taskRepository.truncateTable();
//        excuteTask("tasks");
//        logger.info("=========================>清空缺陷表======================");
//        bugRepository.truncateBugs();
//        excuteTask("bugs");
//        logger.info("=========================>清空需求表======================");
//        storyRepository.truncateStories();
//        excuteTask("stories");
//        logger.info("=========================>清空迭代表======================");
//        iterationRepository.truncateIteration();
//        excuteTask("iterations");
        logger.info("=======初始化数据表结束=======");
        return "基础数据更新完成";
    }

    public void initStatusMap() {
        List<Workspace> workspaces;
        workspaces = (List<Workspace>) workspaceRepository.findAll();
        String[] systems;
        systems = new String[]{"story", "bug"};
        workspaces.forEach(workspace -> {
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            //发送请求
            Arrays.stream(systems).forEach(system -> {
                AtomicReference<String> url = new AtomicReference<>("https://api.tapd.cn/workflows/status_map?system=" + system + "&workspace_id=" + workspace.getId());
                HttpHeaders headers = new HttpHeaders();
                headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
                HttpEntity<String> ans = restTemplate.exchange(url.get(), HttpMethod.GET,   //GET请求
                        new HttpEntity<>(null, headers),   //加入headers
                        String.class);  //body响应数据接收类型
                String gson = ans.getBody();
                logger.info(gson);
                Gson g = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                ResultStatusEntity vo = g.fromJson(gson, ResultStatusEntity.class);
                if (vo.getData().size() > 0) {
                    for (Object key : vo.getData().keySet()) {
                        StatusMap statusMap = new StatusMap();
                        statusMap.setCode((String) key);
                        statusMap.setName((String) vo.getData().get((String) key));
                        statusMap.setSystem(system);
                        statusMap.setWorkspaceId(workspace.getId());
                        statusMapRepository.save(statusMap);
                    }
                }
            });
        });
        logger.info("=========================>更新状态表完毕======================");
    }

    private void excuteTask(String type) {
        String[] idsStr = ids.split(",");
        for (String workspaceId : idsStr) {
            String url = "https://api.tapd.cn/" + type + "?workspace_id=" + workspaceId;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));

            switch (type) {
                case "bugs":
                    saveBug(workspaceId, url);
                    break;
                case "tasks":
                    saveTask(workspaceId, url);
                    break;
                case "stories":
                    saveStory(workspaceId, url);
                    break;
                case "iterations":
                    saveIteration(workspaceId, url);
                    break;
                default:
                    break;
            }
        }
    }

    private void saveBug(String workspaceId, String url) {
        int count = getCount(workspaceId, "bugs");
        logger.info("[BUG更新总数：]" + count);
        AtomicInteger totalPage = new AtomicInteger();
        if (count > 200) {
            totalPage.set((count / 200) + 1);
            for (int i = 1; i <= totalPage.get(); i++) {
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
                    vo.getData().stream().map(map -> g.toJson(map.get("Bug"))).forEach(gsonStr -> {
                        logger.info("[BUG:]" + gsonStr);
                        Bug bug = g.fromJson(gsonStr, Bug.class);
                        bug.setPriority(PriorityEnum.getValue(bug.getPriority()));
                        bug.setSeverity(SeverityEnum.getValue(bug.getSeverity()));
                        bug.setResolution(ResolutionEnum.getValue(bug.getResolution()));
                        Optional<Iteration> iteration = iterationRepository.findById(bug.getIteration_id());
                        iteration.ifPresent(value -> bug.setIteration_name(value.getName()));
                        Optional<Workspace> workspace = workspaceRepository.findById(bug.getWorkspace_id());
                        workspace.ifPresent(value -> bug.setWorkspace_name(value.getName()));
                        StatusMap statusMap = statusMapRepository.findByCodeAndSystemAndWorkspaceId(bug.getStatus(), "bug", bug.getWorkspace_id());
                        if (nonNull(statusMap)) {
                            bug.setStatus(statusMap.getName());
                        }
                        bugRepository.save(bug);
                    });
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
            if (vo.getData().size() <= 0) return;
            vo.getData().stream().map(map -> g.toJson(map.get("Bug"))).forEachOrdered(gsonStr -> {
                logger.info("[BUG:]" + gsonStr);
                Bug bug = g.fromJson(gsonStr, Bug.class);
                bug.setPriority(PriorityEnum.getValue(bug.getPriority()));
                bug.setSeverity(SeverityEnum.getValue(bug.getSeverity()));
                bug.setResolution(ResolutionEnum.getValue(bug.getResolution()));
                Optional<Iteration> iteration = iterationRepository.findById(bug.getIteration_id());
                iteration.ifPresent(value -> bug.setIteration_name(value.getName()));
                Optional<Workspace> workspace = workspaceRepository.findById(bug.getWorkspace_id());
                workspace.ifPresent(value -> bug.setWorkspace_name(value.getName()));
                StatusMap statusMap = statusMapRepository.findByCodeAndSystemAndWorkspaceId(bug.getStatus(), "bug", bug.getWorkspace_id());
                if (nonNull(statusMap)) bug.setStatus(statusMap.getName());
                bugRepository.save(bug);
            });
        }
    }

    private void saveIteration(String workspaceId, String url) {
        int count = getCount(workspaceId, "iterations");
        logger.info("[Iteration更新总数：]" + count);
        AtomicInteger totalPage = new AtomicInteger();
        if (count > 200) {
            totalPage.set((count / 200) + 1);
            for (int i = 1; i <= totalPage.get(); i++) {
                url += "&limit=200&page=" + i;
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
                    vo.getData().stream().map(map -> g.toJson(map.get("Iteration"))).forEach(gsonStr -> {
                        logger.info("[Iteration:]" + gsonStr);
                        Iteration iteration = g.fromJson(gsonStr, Iteration.class);
                        Optional<Workspace> workspace = workspaceRepository.findById(iteration.getWorkspace_id());
                        workspace.ifPresent(value -> iteration.setWorkspace_name(value.getName()));
                        iterationRepository.save(iteration);
                    });
                }
            }
        } else {
            url += "&limit=200";
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
                vo.getData().stream().map(map -> g.toJson(map.get("Iteration"))).forEach(gsonStr -> {
                    logger.info("[Iteration]" + gsonStr);
                    Iteration iteration = g.fromJson(gsonStr, Iteration.class);
                    Optional<Workspace> workspace = workspaceRepository.findById(iteration.getWorkspace_id());
                    workspace.ifPresent(value -> iteration.setWorkspace_name(value.getName()));
                    iterationRepository.save(iteration);
                });
            }
        }
    }

    private void saveTask(String workspaceId, String url) {
        int count = getCount(workspaceId, "tasks");
        logger.info("[Task更新总数：]" + count);
        AtomicInteger totalPage = new AtomicInteger();
        if (count > 200) {
            totalPage.set((count / 200) + 1);
            for (int i = 1; i <= totalPage.get(); i++) {
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
                    vo.getData().stream().map(map -> g.toJson(map.get("Task"))).forEach(gsonStr -> {
                        logger.info("[Task]" + gsonStr);
                        Task task = g.fromJson(gsonStr, Task.class);
                        task.setStatus(StatusEnum.getValue(task.getStatus()));
                        task.setPriority(SPriorityEnum.getValue(task.getPriority()));
                        task.setProgress(task.getProgress() + "%");
                        Optional<Iteration> iteration = iterationRepository.findById(task.getIteration_id());
                        iteration.ifPresent(value -> task.setIteration_name(value.getName()));
                        Optional<Story> story = storyRepository.findById(task.getStory_id());
                        story.ifPresent(value -> task.setStory_name(value.getName()));
                        Optional<Workspace> workspace = workspaceRepository.findById(task.getWorkspace_id());
                        workspace.ifPresent(value -> task.setWorkspace_name(value.getName()));
                        taskRepository.save(task);
                    });
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
            if (vo.getData().size() <= 0) {
                return;
            }
            vo.getData().stream().map(map -> g.toJson(map.get("Task"))).forEach(gsonStr -> {
                logger.info("[Task]" + gsonStr);
                Task task = g.fromJson(gsonStr, Task.class);
                task.setStatus(StatusEnum.getValue(task.getStatus()));
                task.setPriority(SPriorityEnum.getValue(task.getPriority()));
                task.setProgress(task.getProgress() + "%");
                Optional<Iteration> iteration = iterationRepository.findById(task.getIteration_id());
                iteration.ifPresent(value -> task.setIteration_name(value.getName()));
                Optional<Story> story = storyRepository.findById(task.getStory_id());
                story.ifPresent(value -> task.setStory_name(value.getName()));
                Optional<Workspace> workspace = workspaceRepository.findById(task.getWorkspace_id());
                workspace.ifPresent(value -> task.setWorkspace_name(value.getName()));
                taskRepository.save(task);
            });
        }
    }

    private void saveStory(String workspaceId, String url) {
        int count = getCount(workspaceId, "stories");
        logger.info("[Story更新总数：]" + count);
        int totalPage;
        if (count > 200) {
            totalPage = (count / 200) + 1;
            for (int i = 1; i <= totalPage; i++) {
                url += "&limit=200&page=" + i;
                //发送请求
                HttpEntity<String> ans;  //body响应数据接收类型
                ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                        new HttpEntity<>(null, headers),   //加入headers
                        String.class);
                String gson = ans.getBody();
                Gson g = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                if (vo.getData().size() > 0) {
                    for (LinkedTreeMap map : vo.getData()) {
                        String gsonStr;
                        gsonStr = g.toJson(map.get("Story"));
                        logger.info("[Story]" + gsonStr);
                        Story story = g.fromJson(gsonStr, Story.class);
                        story.setPriority(SPriorityEnum.getValue(story.getPriority()));

                        Optional<Iteration> iteration = iterationRepository.findById(story.getIteration_id());
                        iteration.ifPresent(value -> story.setIterationName(value.getName()));

                        Optional<StoryCategories> categories = storyCategoriesRepository.findById(story.getCategory_id());
                        categories.ifPresent(storyCategories -> story.setCategory_name(storyCategories.getName()));

                        Optional<Workspace> workspace = workspaceRepository.findById(story.getWorkspace_id());
                        workspace.ifPresent(value -> story.setWorkspace_name(value.getName()));

                        StatusMap statusMap = statusMapRepository.findByCodeAndSystemAndWorkspaceId(story.getStatus(), "story", story.getWorkspace_id());
                        if (nonNull(statusMap)) {
                            story.setStatus(statusMap.getName());
                        }
                        storyRepository.save(story);
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
                vo.getData().stream().map(map -> g.toJson(map.get("Story"))).forEach(gsonStr -> {
                    logger.info("[Story]" + gsonStr);
                    Story story = g.fromJson(gsonStr, Story.class);
                    story.setPriority(SPriorityEnum.getValue(story.getPriority()));
                    Optional<Iteration> iteration = iterationRepository.findById(story.getIteration_id());
                    iteration.ifPresent(value -> story.setIterationName(value.getName()));
                    Optional<StoryCategories> categories = storyCategoriesRepository.findById(story.getCategory_id());
                    categories.ifPresent(storyCategories -> story.setCategory_name(storyCategories.getName()));
                    Optional<Workspace> workspace = workspaceRepository.findById(story.getWorkspace_id());
                    workspace.ifPresent(value -> story.setWorkspace_name(value.getName()));
                    StatusMap statusMap = statusMapRepository.findByCodeAndSystemAndWorkspaceId(story.getStatus(), "story", story.getWorkspace_id());
                    if (nonNull(statusMap)) {
                        story.setStatus(statusMap.getName());
                    }
                    storyRepository.save(story);
                });
            }
        }
    }

    private int getCount(final String workspaceId, final String type) {
        String url = "https://api.tapd.cn/" + type + "/count?workspace_id=" + workspaceId;
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
        int count;
        count = ((Double) map.get("count")).intValue();
        return count;
    }

    public void initWorkspace() {
        String url = String.format("https://api.tapd.cn/workspaces/projects?company_id=%s", companyId);
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
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
            vo.getData().stream().map(map -> g.toJson(map.get("Workspace"))).forEach(gsonStr -> {
                logger.debug("[workspace:]" + gsonStr);
                Workspace workspace = g.fromJson(gsonStr, Workspace.class);
                List<String> list = Arrays.asList(ids.split(","));
                if (list.contains(String.valueOf(workspace.getId())))
                    workspaceRepository.save(workspace);
            });
        }
    }

    public void initStoryCategories() {
        String[] idsStr = ids.split(",");
        for (String workspaceId : idsStr) {
            String url = "https://api.tapd.cn/story_categories?workspace_id=" + workspaceId;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspaceId);
            int totalPage;
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
                        vo.getData().stream().map(map -> g.toJson(map.get("Category"))).forEach(gsonStr -> {
                            logger.info("[Category]:" + gsonStr);
                            StoryCategories storyCategories = g.fromJson(gsonStr, StoryCategories.class);
                            storyCategoriesRepository.save(storyCategories);
                        });
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
                    vo.getData().stream().map(map -> g.toJson(map.get("Category"))).forEachOrdered(gsonStr -> {
                        logger.info("[Category]:" + gsonStr);
                        StoryCategories storyCategories = g.fromJson(gsonStr, StoryCategories.class);
                        storyCategoriesRepository.save(storyCategories);
                    });
                }
            }
        }
    }

    private int getCount(final String workspaceId) {
        AtomicReference<String> url = new AtomicReference<>("https://api.tapd.cn/story_categories/count?workspace_id=" + workspaceId);
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        //发送请求
        HttpEntity<String> ans = restTemplate.exchange(url.get(), HttpMethod.GET,   //GET请求
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
        return ((Double) map.get("count")).intValue();
    }
}
