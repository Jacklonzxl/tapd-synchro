package com.ext.tapd.tapd.task;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
@EnableScheduling
public class MultithreadScheduleTask {

    private static Logger logger = LoggerFactory.getLogger(MultithreadScheduleTask.class);
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

    @Value("${workspace.ids}")
    private String ids;
    @Value("${project.ids}")
    private String projectIds;
    @Value("${tapd.account}")
    private String account;
    @Value("${task.schedule.enabled}")
    private boolean scheduleEnabled;
    private HttpHeaders headers = new HttpHeaders();


    @Scheduled(cron = "${cron:0 0/30 * * * ?}") //每半小时执行一次
    @Async
    public void task() {
        if(scheduleEnabled){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modified = sdf.format(now);
            excuteTask("tasks", modified);
        }
    }


    @Scheduled(cron = "${cron:0 0/30 * * * ?}") //每半小时执行一次
    @Async
    public void bug() {
        if(scheduleEnabled){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modified = sdf.format(now);
            excuteTask("bugs", modified);
        }
    }

    @Scheduled(cron = "${cron:0 0/30 * * * ?}") //每半小时执行一次
    @Async
    public void story() {
        if(scheduleEnabled){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modified = sdf.format(now);
            excuteTask("stories", modified);
        }
    }

    @Scheduled(cron = "${cron:0 0/30 * * * ?}") //每半小时执行一次
    @Async
    public void iteration() {
        if(scheduleEnabled){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modified = sdf.format(now);
            excuteTask("iterations", modified);
        }
    }

    private void excuteTask(String type, String modified) {
        String[] idsStr = ids.split(",");
        if (type.equals("bugs")) {
            idsStr = projectIds.split(",");
        }
        for (String workspaceId : idsStr) {
            AtomicReference<String> url = new AtomicReference<>("https://api.tapd.cn/" + type + "?workspace_id=" + workspaceId + "&modified=>" + modified);
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));

            switch (type) {
                case "bugs":
                    saveBug(workspaceId, modified, url.get());
                    break;
                case "tasks":
                    saveTask(workspaceId, modified, url.get());
                    break;
                case "stories":
                    saveStory(workspaceId, modified, url.get());
                    break;
                case "iterations":
                    saveIteration(workspaceId, modified, url.get());
                    break;
                default:
                    break;
            }
        }
    }

    private void saveBug(String workspaceId, String modified, String url) {
        String[] workspaceIdsStr = ids.split(",");
        String search = "";
        if (Arrays.asList(workspaceIdsStr).contains(workspaceId)) {
            search = "&status=resolved|suspended|new|in_progress|postponed|rejected|reopened|unconfirmed|closed";
        } else {
            search = "&status=resolved|suspended|new|in_progress|postponed|rejected|reopened|unconfirmed";
        }
        int count = getCount(workspaceId, "bugs", modified, search);
        int totalPage = 0;
        if (count > 200) {
            totalPage = (count / 200) + 1;
            for (int i = 1; i <= totalPage; i++) {
                url = url + search + "&limit=200&page=" + i;
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
                        if (Objects.nonNull(statusMap)) {
                            bug.setStatus(statusMap.getName());
                        }
                        bugRepository.save(bug);
                    });
                }
            }
        } else {
            url = url + search + "&limit=200";
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
                    String gsonStr = g.toJson(map.get("Bug"));
                    logger.info("[BUG:]" + gsonStr);
                    Bug bug = g.fromJson(gsonStr, Bug.class);
                    bug.setPriority(PriorityEnum.getValue(bug.getPriority()));
                    bug.setSeverity(SeverityEnum.getValue(bug.getSeverity()));
                    bug.setResolution(ResolutionEnum.getValue(bug.getResolution()));
                    Optional<Iteration> iteration = iterationRepository.findById(bug.getIteration_id());
                    if (iteration.isPresent()) {
                        bug.setIteration_name(iteration.get().getName());
                    }
                    Optional<Workspace> workspace = workspaceRepository.findById(bug.getWorkspace_id());
                    if (workspace.isPresent()) {
                        bug.setWorkspace_name(workspace.get().getName());
                    }
                    StatusMap statusMap = statusMapRepository.findByCodeAndSystemAndWorkspaceId(bug.getStatus(), "bug", bug.getWorkspace_id());
                    if (Objects.nonNull(statusMap)) {
                        bug.setStatus(statusMap.getName());
                    }
                    bugRepository.save(bug);
                }
            }
        }
    }

    private void saveIteration(String workspaceId, String modified, String url) {
        int count = getCount(workspaceId, "iterations", modified, "");
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
                        String gsonStr = g.toJson(map.get("Iteration"));
                        logger.info("[Iteration:]" + gsonStr);
                        Iteration iteration = g.fromJson(gsonStr, Iteration.class);
                        Optional<Workspace> workspace = workspaceRepository.findById(iteration.getWorkspace_id());
                        workspace.ifPresent(value -> iteration.setWorkspace_name(value.getName()));
                        iterationRepository.save(iteration);
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
                vo.getData().stream().map(map -> g.toJson(map.get("Iteration"))).forEach(gsonStr -> {
                    logger.info("[Iteration:]" + gsonStr);
                    Iteration iteration = g.fromJson(gsonStr, Iteration.class);
                    Optional<Workspace> workspace = workspaceRepository.findById(iteration.getWorkspace_id());
                    workspace.ifPresent(value -> iteration.setWorkspace_name(value.getName()));
                    iterationRepository.save(iteration);
                });
            }
        }
    }

    private void saveTask(String workspaceId, String modified, String url) {
        int count = getCount(workspaceId, "tasks", modified, "");
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

    private void saveStory(String workspaceId, String modified, String url) {
        int count = getCount(workspaceId, "stories", modified, "");
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
                        String gsonStr = g.toJson(map.get("Story"));
                        logger.info("[Story:]" + gsonStr);
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
                    String gsonStr = g.toJson(map.get("Story"));
                    logger.info("[Story:]" + gsonStr);
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
                }
            }
        }
    }

    private int getCount(final String workspaceId, final String type, String modified, String search) {
        String url = "https://api.tapd.cn/" + type + "/count?workspace_id=" + workspaceId + "&modified=>" + modified;
        if (type.equals("bugs"))
            url = url + search;
        //发送请求
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                new HttpEntity<>(null, headers),   //加入headers
                String.class);  //body响应数据接收类型
        String gson = ans.getBody();
        logger.debug(gson);
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        Map map = vo.getData();
        int count = new Double((Double) map.get("count")).intValue();
        return count;
    }
}
