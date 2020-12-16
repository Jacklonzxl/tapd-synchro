package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.StoryCategoriesRepository;
import com.ext.tapd.tapd.pojo.ResultCountEntity;
import com.ext.tapd.tapd.pojo.ResultEntity;
import com.ext.tapd.tapd.pojo.StoryCategories;
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
import java.util.Map;

@RestController
@RequestMapping("/storycategory")
public class StoryCategoriesController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StoryCategoriesRepository storyCategoriesRepository;

    @Value("${workspace.ids}")
    private String ids;

    //初始化task
    @RequestMapping(value = "/initStoryCategory", method = RequestMethod.GET)
    public String initTask() {
        String[] idsStr = ids.split(",");
        for (String workspaceId : idsStr) {
            String url = "https://api.tapd.cn/story_categories?workspace_id=" + workspaceId;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString("XFzFJy1k:1BF133BB-0B17-E7C1-A04A-067C761B353C".getBytes()));
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
                            String gsonStr = g.toJson(map.get("Category"));
                            System.out.println(gsonStr);
                            StoryCategories storyCategories = g.fromJson(gsonStr, StoryCategories.class);
                            storyCategoriesRepository.save(storyCategories);
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
                        String gsonStr = g.toJson(map.get("Category"));
                        System.out.println(gsonStr);
                        StoryCategories storyCategories = g.fromJson(gsonStr, StoryCategories.class);
                        storyCategoriesRepository.save(storyCategories);
                    }
                }
            }
        }
        return "执行成功";
    }

    private int getCount(final String workspaceId) {
        String url = "https://api.tapd.cn/story_categories/count?workspace_id=" + workspaceId;
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString("XFzFJy1k:1BF133BB-0B17-E7C1-A04A-067C761B353C".getBytes()));
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
