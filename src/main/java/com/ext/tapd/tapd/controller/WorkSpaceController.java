//package com.ext.tapd.tapd.controller;
//
//import com.ext.tapd.tapd.dao.WorkspaceRepository;
//import com.ext.tapd.tapd.pojo.ResultEntity;
//import com.ext.tapd.tapd.pojo.Workspace;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.internal.LinkedTreeMap;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.List;
//
//@RestController
//@RequestMapping("/workspace")
//public class WorkSpaceController {
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private WorkspaceRepository workspaceRepository;
//
//    @Value("${company.id}")
//    private String companyId;
//    @Value("${workspace.ids}")
//    private String ids;
//    private final Logger logger = LoggerFactory.getLogger(WorkSpaceController.class);
//
//    //初始化task
//    @RequestMapping(value = "/initWorkspace", method = RequestMethod.GET)
//    public String initWorkspace() {
//        String url = String.format("https://api.tapd.cn/workspaces/projects?company_id=%s", companyId);
//        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString("XFzFJy1k:1BF133BB-0B17-E7C1-A04A-067C761B353C".getBytes()));
//        //发送请求
//        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,   //GET请求
//                new HttpEntity<>(null, headers),   //加入headers
//                String.class);  //body响应数据接收类型
//        String gson = ans.getBody();
//        Gson g = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .create();
//        ResultEntity vo = g.fromJson(gson, ResultEntity.class);
//        if (vo.getData().size() > 0) {
//            vo.getData().stream().map(map -> g.toJson(map.get("Workspace"))).forEach(gsonStr -> {
//                logger.debug("[workspace:]" + gsonStr);
//                Workspace workspace = g.fromJson(gsonStr, Workspace.class);
//                List<String> list = Arrays.asList(ids.split(","));
//                if(list.contains(String.valueOf(workspace.getId())))
//                workspaceRepository.save(workspace);
//            });
//        }
//        return "执行成功";
//    }
//}
