package com.ext.tapd.tapd.task;

import com.ext.tapd.tapd.common.status.*;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lx
 */
@Component
@EnableScheduling
public class MultithreadScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(MultithreadScheduleTask.class);
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
    private final HttpHeaders headers = new HttpHeaders();


    /**
     * 每半小时执行一次
     */
    @Scheduled(cron = "${cron:0 0/30 * * * ?}")
    @Async
    public void task() {
        if(scheduleEnabled){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modified = sdf.format(now);
            excuteTask("tasks", modified);
        }
    }

    /**
     * 每半小时执行一次
     */
    @Scheduled(cron = "${cron:0 0/31 * * * ?}")
    @Async
    public void bug() {
        if(scheduleEnabled){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modified = sdf.format(now);
            excuteTask("bugs", modified);
        }
    }

    /**
     * 每半小时执行一次
     */
    @Scheduled(cron = "${cron:0 0/32 * * * ?}")
    @Async
    public void story() {
        if(scheduleEnabled){
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String modified = sdf.format(now);
            excuteTask("stories", modified);
        }
    }

    /**
     * 每半小时执行一次
     */
    @Scheduled(cron = "${cron:0 0/33 * * * ?}")
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
        if ("bugs".equals(type)) {
            idsStr = projectIds.split(",");
        }
        for (String workspaceId : idsStr) {
            AtomicReference<String> url = new AtomicReference<>("https://api.tapd.cn/" + type + "?workspace_id=" + workspaceId + "&modified=>" + modified);
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            logger.info("=========================>定时初始化"+type+"表开始");
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
            logger.info("=========================>定时初始化"+type+"表结束");
        }
    }

    private void saveBug(String workspaceId, String modified, String url) {
        String[] workspaceIdsStr = ids.split(",");
        String search;
        if (Arrays.asList(workspaceIdsStr).contains(workspaceId)) {
            search = "&status=resolved|suspended|new|in_progress|postponed|rejected|reopened|unconfirmed|closed";
        } else {
            search = "&status=resolved|suspended|new|in_progress|postponed|rejected|reopened|unconfirmed";
        }
        int count = getCount(workspaceId, "bugs", modified, search);
        logger.info("=========================>定时初始化缺陷表总数:"+count);
        int totalPage;
        if (count > 200) {
            totalPage = (count / 200) + 1;
            for (int i = 1; i <= totalPage; i++) {
                url = url + search + "&limit=200&page=" + i;
                HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                String gson = ans.getBody();
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                if (vo.getData().size() > 0) {
                    vo.getData().stream().map(map -> g.toJson(map.get("Bug"))).forEach(gsonStr -> {
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
            HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
            String gson = ans.getBody();
            Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            ResultEntity vo = g.fromJson(gson, ResultEntity.class);
            if (vo.getData().size() > 0) {
                vo.getData().stream().map(map -> g.toJson(map.get("Bug"))).forEach(gsonStr -> {
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
    }

    private void saveIteration(String workspaceId, String modified, String url) {
        int count = getCount(workspaceId, "iterations", modified, "");
        logger.info("=========================>定时初始化迭代表总数:"+count);
        int totalPage;
        if (count > 200) {
            totalPage = (count / 200) + 1;
            for (int i = 1; i <= totalPage; i++) {
                url = url + "&limit=200&page=" + i;
                HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                String gson = ans.getBody();
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                if (vo.getData().size() > 0) {
                    vo.getData().stream().map(map -> g.toJson(map.get("Iteration"))).forEach(gsonStr -> {
                        Iteration iteration = g.fromJson(gsonStr, Iteration.class);
                        Optional<Workspace> workspace = workspaceRepository.findById(iteration.getWorkspace_id());
                        workspace.ifPresent(value -> iteration.setWorkspace_name(value.getName()));
                        iterationRepository.save(iteration);
                    });
                }
            }
        } else {
            url = url + "&limit=200";
            //发送请求
            HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
            String gson = ans.getBody();
            Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            ResultEntity vo = g.fromJson(gson, ResultEntity.class);
            if (vo.getData().size() > 0) {
                vo.getData().stream().map(map -> g.toJson(map.get("Iteration"))).forEach(gsonStr -> {
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
        logger.info("=========================>定时初始化任务表总数:"+count);
        AtomicInteger totalPage = new AtomicInteger();
        if (count > 200) {
            totalPage.set((count / 200) + 1);
            for (int i = 1; i <= totalPage.get(); i++) {
                url = url + "&limit=200&page=" + i;
                HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                String gson = ans.getBody();
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ResultEntity vo = g.fromJson(gson, ResultEntity.class);
                if (vo.getData().size() > 0) {
                    vo.getData().stream().map(map -> g.toJson(map.get("Task"))).forEach(gsonStr -> {
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
            HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
            String gson = ans.getBody();
            Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            ResultEntity vo = g.fromJson(gson, ResultEntity.class);
            if (vo.getData().size() > 0) {
                vo.getData().stream().map(map -> g.toJson(map.get("Task"))).forEach(gsonStr -> {
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
    }

    private void saveStory(String workspaceId, String modified, String url) {
        int count = getCount(workspaceId, "stories", modified, "");
        logger.info("=========================>定时初始化需求表总数:"+count);
        int totalPage;
        if (count > 200) {
            totalPage = (count / 200) + 1;
            for (int i = 1; i <= totalPage; i++) {
                url = url + "&limit=200&page=" + i;
                //发送请求
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
            //发送请求
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

    private int getCount(final String workspaceId, final String type, String modified, String search) {
        String url = "https://api.tapd.cn/" + type + "/count?workspace_id=" + workspaceId + "&modified=>" + modified;
        if ("bugs".equals(type)){ url = url + search;}
        //发送请求
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
        String gson = ans.getBody();
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        return ((Double) vo.getData().get("count")).intValue();
    }
}
