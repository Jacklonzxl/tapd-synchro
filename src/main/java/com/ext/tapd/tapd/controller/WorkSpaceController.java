package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.WorkspaceRepository;
import com.ext.tapd.tapd.pojo.ResultEntity;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author lx
 */
@RestController
@RequestMapping("/workspace")
public class WorkSpaceController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Value("${company.id}")
    private String companyId;
    @Value("${workspace.ids}")
    private String ids;
    @Value("${project.ids}")
    private String projectIds;
    @Value("${tapd.account}")
    private String account;
    private final Logger logger = LoggerFactory.getLogger(WorkSpaceController.class);

    /**初始化项目表*/
    @RequestMapping(value = "/initWorkspace", method = RequestMethod.GET)
    public String initWorkspace() {
        workspaceRepository.truncateTable();
        String url = String.format("https://api.tapd.cn/workspaces/projects?company_id=%s", companyId);
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
        String gson = ans.getBody();
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ResultEntity vo = g.fromJson(gson, ResultEntity.class);
        if (vo.getData().size() > 0) {
            vo.getData().stream().map(map -> g.toJson(map.get("Workspace"))).forEach(gsonStr -> {
                logger.debug("[workspace:]" + gsonStr);
                Workspace workspace = g.fromJson(gsonStr, Workspace.class);
                List<String> list = Arrays.asList(projectIds.split(","));
                if(list.contains(String.valueOf(workspace.getId()))) { workspaceRepository.save(workspace); }
            });
        }
        return "执行成功";
    }
}
