package com.kong.firststorm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * @Program: stormtest
 * @Description: 产生随机单词
 * @Author: LIANGHAIKUN
 * @Create: 2018/9/20 16:49
 **/
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;

    private String[] words = {"Hadoop","Storm","Apache","Linux","Nginx","Tomcat","Spark"};

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        Random random = new Random();
        String word = words[random.nextInt(words.length)];
        collector.emit(new Values(word));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("randomString"));
    }
}
