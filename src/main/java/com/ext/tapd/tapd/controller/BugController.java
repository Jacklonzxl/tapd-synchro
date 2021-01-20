package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.common.status.PriorityEnum;
import com.ext.tapd.tapd.common.status.ResolutionEnum;
import com.ext.tapd.tapd.common.status.SeverityEnum;
import com.ext.tapd.tapd.dao.BugRepository;
import com.ext.tapd.tapd.dao.IterationRepository;
import com.ext.tapd.tapd.dao.StatusMapRepository;
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

import javax.transaction.Transactional;
import java.util.*;

/**
 * 更新缺陷表
 * @author lx
 */
@RestController
@RequestMapping("/bugs")
public class BugController {
    private static Logger logger = LoggerFactory.getLogger(BugController.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BugRepository bugRepository;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private StatusMapRepository statusMapRepository;

    @Value("${project.ids}")
    private String ids;
    @Value("${workspace.ids}")
    private String workspaceIds;
    @Value("${tapd.account}")
    private String account;


    /**
     * 初始化BUG表
     */
    @RequestMapping(value = "/initBugs", method = RequestMethod.GET)
    @Transactional(rollbackOn = {Exception.class})
    public String initBugs() {
        logger.info("=========================>初始化缺陷表开始");
        bugRepository.truncateBugs();
        String[] idsStr = ids.split(",");
        String[] workspaceIdsStr = workspaceIds.split(",");
        for (String workspaceId : idsStr) {
            String search;
            if (Arrays.asList(workspaceIdsStr).contains(workspaceId)) {
                search = "&status=resolved|suspended|new|in_progress|postponed|rejected|reopened|unconfirmed|closed";
            } else {
                search = "&status=resolved|suspended|new|in_progress|postponed|rejected|reopened|unconfirmed";
            }
            String url = "https://api.tapd.cn/bugs?workspace_id=" + workspaceId + search;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspaceId, search);
            logger.info("=========================>初始化缺陷表条数:"+count);
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
                url = url + "&limit=200";
                /* url 发送请求地址 GET请求 加入headers body响应数据接收类型*/
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
        logger.info("=========================>初始化缺陷表结束");
        return "执行成功";
    }

    private int getCount(final String workspaceId, final String search) {
        String url = "https://api.tapd.cn/bugs/count?workspace_id=" + workspaceId + search;
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        /* url 发送请求地址 GET请求 加入headers body响应数据接收类型*/
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
        String gson = ans.getBody();
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        return ((Double) vo.getData().get("count")).intValue();
    }
}
