package com.storm.amap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Program: storm_amap
 * @Description: 获取内容
 * @Author: LIANGHAIKUN
 * @Create: 2018/9/26 19:35
 **/
@Service
public class RestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map> getAll() {

        return jdbcTemplate.query("select  lat, lng, count(0) from ( select lat, lng from user order by atime desc limit 50) x group by lat, lng ", (ResultSet resultSet, int i) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("lat", resultSet.getDouble(1));
            map.put("lng", resultSet.getDouble(2));
            map.put("count", resultSet.getInt(3));
            return map;
        });
    }
}
