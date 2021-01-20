package com.ext.tapd.tapd.controller;

import com.ext.tapd.tapd.dao.IterationRepository;
import com.ext.tapd.tapd.dao.WorkspaceRepository;
import com.ext.tapd.tapd.pojo.Iteration;
import com.ext.tapd.tapd.pojo.ResultCountEntity;
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

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.Optional;

/**
 * 更新迭代表
 * @author lx
 */
@RestController
@RequestMapping("/iterations")
public class IterationController {
    private static Logger logger = LoggerFactory.getLogger(IterationController.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Value("${workspace.ids}")
    private String ids;
    @Value("${tapd.account}")
    private String account;

    /**初始化迭代表*/
    @RequestMapping(value = "/initIterations", method = RequestMethod.GET)
    @Transactional(rollbackOn = {Exception.class})
    public String initIterations() {
        logger.info("=========================>初始化迭代表开始");
        iterationRepository.truncateIteration();
        String[] idsStr = ids.split(",");
        for (String workspaceId : idsStr) {
            String url = "https://api.tapd.cn/iterations?workspace_id=" + workspaceId;
            //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
            HttpHeaders headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
            int count = getCount(workspaceId);
            logger.info("=========================>初始化迭代表条数:"+count);
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
        logger.info("=========================>初始化迭代表结束");
        return "迭代表更新成功";
    }

    private int getCount(final String workspaceId) {
        String url = "https://api.tapd.cn/iterations/count?workspace_id=" + workspaceId;
        //在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString(account.getBytes()));
        HttpEntity<String> ans = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null, headers),String.class);
        String gson = ans.getBody();
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ResultCountEntity vo = g.fromJson(gson, ResultCountEntity.class);
        return ((Double) vo.getData().get("count")).intValue();
    }
}
