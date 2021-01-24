package com.ext.tapd.tapd.task;

import com.ext.tapd.tapd.dao.StatusMapRepository;
import com.ext.tapd.tapd.dao.WorkspaceRepository;
import com.ext.tapd.tapd.pojo.ResultStatusEntity;
import com.ext.tapd.tapd.pojo.StatusMap;
import com.ext.tapd.tapd.pojo.Workspace;
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

import java.util.Base64;
import java.util.List;

/**
 * @author lx
 */
@Component
@EnableScheduling
public class BaseDataScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(BaseDataScheduleTask.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StatusMapRepository statusMapRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Value("${tapd.account}")
    private String account;
    @Value("${task.schedule.enabled}")
    private boolean scheduleEnabled;

    @Scheduled(cron = "${cron:0 0/30 * * * ?}") //每1小时执行一次
    @Async
    public void task() {
        if(scheduleEnabled){
            logger.info("=========================>定时初始化基础表开始");
            statusMapRepository.truncateStatusMap();
            List<Workspace> workspaces = (List<Workspace>) workspaceRepository.findAll();
            String[] systems = {"story", "bug"};
            for (Workspace workspace : workspaces) {
                for (String system : systems) {
                    String url = String.format("https://api.tapd.cn/workflows/status_map?system=%s&workspace_id=%d", system, workspace.getId());
                    //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
                    HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
                    String gson = ans.getBody();
                    Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    ResultStatusEntity vo = g.fromJson(gson, ResultStatusEntity.class);
                    logger.info("=========================>初始化-t_status_map-["+workspace.getName()+"]-["+system+"] 总数:"+vo.getData().size());
                    if (vo.getData().size() > 0) {
                        vo.getData().keySet().forEach(key -> {
                            StatusMap statusMap = new StatusMap();
                            statusMap.setCode(key);
                            statusMap.setName((String) vo.getData().get(key));
                            statusMap.setSystem(system);
                            statusMap.setWorkspaceId(workspace.getId());
                            statusMapRepository.save(statusMap);
                        });
                    }
                }
            }
            logger.info("=========================>定时初始化基础表结束");
        }
    }
}
