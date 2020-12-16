package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.StatusMapRepository;
import com.ext.tapd.tapd.dao.StoryCategoriesRepository;
import com.ext.tapd.tapd.dao.WorkspaceRepository;
import com.ext.tapd.tapd.pojo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statusmap")
public class StatusMapController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StatusMapRepository statusMapRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    //初始化task
    @RequestMapping(value = "/initStatusmap",method = RequestMethod.GET)
    public String initTask(){
        List<Workspace> workspaces = (List<Workspace>) workspaceRepository.findAll();
        String[] systems = {"story","bug"};
        for(Workspace workspace : workspaces) {
            for(String system: systems){
                String url = "https://api.tapd.cn/workflows/status_map?system="+system+"&workspace_id=" + workspace.getId();
                //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
                HttpHeaders headers = new HttpHeaders();
                headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString("XFzFJy1k:1BF133BB-0B17-E7C1-A04A-067C761B353C".getBytes()));
                //发送请求
                HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
                        new HttpEntity<>(null, headers),   //加入headers
                        String.class);  //body响应数据接收类型
                String gson = ans.getBody();
                Gson g =new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                ResultStatusEntity vo = g.fromJson(gson, ResultStatusEntity.class);
                if (vo.getData().size() > 0) {
                    for(Object key : vo.getData().keySet()){
                        StatusMap statusMap = new StatusMap();
                        statusMap.setCode((String) key);
                        statusMap.setName((String)vo.getData().get((String)key));
                        statusMap.setSystem(system);
                        statusMap.setWorkspaceId(workspace.getId());
                        statusMapRepository.save(statusMap);
                    }
                }
            }
        }
        return "执行成功";
    }
}
