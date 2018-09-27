package com.kong.firststorm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * @Program: stormtest
 * @Description: 启动storm
 * @Author: LIANGHAIKUN
 * @Create: 2018/9/20 16:57
 **/
public class FirstStorm {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new RandomSpout());
        builder.setBolt("bolt", new SenqueceBolt()).shuffleGrouping("spout");
        Config config = new Config();
        config.setDebug(true);

        if(args != null && args.length > 0) {
            config.setNumWorkers(3);

            try {
                StormSubmitter.submitTopology(args[0], config, builder.createTopology());
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            }
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("firststorm", config, builder.createTopology());
            Utils.sleep(10000);
            cluster.killTopology("firststorm");
            cluster.shutdown();
        }
    }
}
