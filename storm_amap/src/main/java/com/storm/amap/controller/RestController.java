package com.storm.amap.controller;

import com.storm.amap.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Program: storm_amap
 * @Description: 控制器
 * @Author: LIANGHAIKUN
 * @Create: 2018/9/26 19:48
 **/
@Controller
@RequestMapping("/rest")
public class RestController {

    @Autowired
    RestService restService;

    @RequestMapping("/get_all")
    @ResponseBody
    public List<Map> getAll() {
        return restService.getAll();
    }
}
