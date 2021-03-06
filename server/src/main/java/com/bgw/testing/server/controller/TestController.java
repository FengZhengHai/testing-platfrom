package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.dao.integration.pojo.TCase;
import com.bgw.testing.dao.usercenter.pojo.UserBasic888888;
import com.bgw.testing.server.service.*;
import com.bgw.testing.server.util.BaseJsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@Api(description = AppConst.SERVICE_NAME_CN)
@RequestMapping(value = AppConst.BASE_PATH)
public class TestController {

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private RedisTestService redisTestService;
    @Autowired
    private JdbcTemplateService jdbcTemplateService;
    @Autowired
    private RestTemplateService restTemplateService;

    @ApiOperation(value = "获取测试用例")
    @RequestMapping(value = "/testCase", method = RequestMethod.GET)
    public TCase getTestCase(@RequestParam String id) {
        return integrationService.getTCase(id);
    }

    @ApiOperation(value = "获取用户基本信息")
    @RequestMapping(value = "/userBasicInfo", method = RequestMethod.GET)
    public UserBasic888888 getUserBasicInfo(@RequestParam String id) {
        return userCenterService.getUserBasicInfo(id);
    }

    @ApiOperation(value = "删除Redis指定key")
    @RequestMapping(value = "/redisInfo", method = RequestMethod.DELETE)
    public Boolean deleteRedisInfo(@RequestParam String key) {
        return redisTestService.deleteRedisInfo(key);
    }

    @ApiOperation(value = "获取测试用例JdbcTemplate")
    @RequestMapping(value = "/testCase2", method = RequestMethod.GET)
    public TCase getTestCase2(@RequestParam String sql, @RequestParam Object[] params) {
        return jdbcTemplateService.getTestCase(sql, params);
    }

    @ApiOperation(value = "自定义接口请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "http请求的完整URL", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "httpMethod", value = "请求方式", required = true, paramType = "query"),
            @ApiImplicitParam(name = "params", value = "json格式的请求参数，示例：{\"page_num\":1,\"page_size\":1}", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "headers", value = "json格式的header，示例：{\"page_num\":1,\"page_size\":1}", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "body", value = "json格式的body", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "responseType", value = "响应类型，默认String.class", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/requestTest", method = RequestMethod.POST)
    public ResponseEntity getTaskInfo(@RequestParam String url,
                                      @RequestParam HttpMethod httpMethod,
                                      @RequestParam(required = false) String params,
                                      @RequestParam(required = false) String headers,
                                      @RequestParam(required = false) String body,
                                      @RequestParam(required = false) Class responseType) {
        return restTemplateService.exchange(url, httpMethod,
                params != null ? BaseJsonUtils.readValue(params, Map.class) : null,
                headers != null ? BaseJsonUtils.readValue(params, Map.class) : null,
                body != null ? body : null,
                responseType != null ? responseType : null);
    }

}
