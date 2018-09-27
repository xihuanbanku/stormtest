package com.kong.kafka;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Program: stormtest
 * @Description: storm消费kafka
 * @Author: LIANGHAIKUN
 * @Create: 2018/9/26 17:01
 **/
public class MyKafkaBolt extends BaseRichBolt {

    private OutputCollector collector;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            byte[] bytes = tuple.getBinaryByField("bytes");
            String string = new String(bytes);
            System.out.println(string);
            String[] strings = string.split("\t");
            String[] geo = strings[1].split(",");
            collector.emit(new Values( Double.parseDouble(geo[0]), Double.parseDouble(geo[1]), sdf.parse(strings[2]).getTime()));
            collector.ack(tuple);
        } catch (Exception e) {
            e.printStackTrace();
            collector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("lat", "lng", "atime"));

    }
}
