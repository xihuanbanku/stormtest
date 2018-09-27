package com.kong.kafka;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Program: stormtest
 * @Description: 组装好的topo
 * @Author: LIANGHAIKUN
 * @Create: 2018/9/26 17:04
 **/
public class StormKafkaTopo {

    public static void main(String[] args) {


        TopologyBuilder builder = new TopologyBuilder();
        String zkConnString = "192.168.140.59:2181";
        BrokerHosts hosts = new ZkHosts(zkConnString);
        String topicName = "test_kafka";
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, UUID.randomUUID().toString());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        String spout_id = KafkaSpout.class.getSimpleName();
        builder.setSpout(spout_id, kafkaSpout);
        String bolt_kafka_id = MyKafkaBolt.class.getSimpleName();
        builder.setBolt(bolt_kafka_id, new MyKafkaBolt()).shuffleGrouping(spout_id);

        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://localhost/azkaban");
        hikariConfigMap.put("dataSource.user","root");
        hikariConfigMap.put("dataSource.password","123456");
        ConnectionProvider connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);


        List<Column> schemaColumns = Lists.newArrayList(new Column("lat",Types.DOUBLE),
                new Column("lng",Types.DOUBLE),new Column("atime", Types.TIMESTAMP));
        JdbcMapper simpleJdbcMapper = new SimpleJdbcMapper(schemaColumns);

        JdbcInsertBolt userPersistanceBolt = new JdbcInsertBolt(connectionProvider, simpleJdbcMapper)
                .withInsertQuery("insert into user(lat, lng, atime) values (?,?,?)")
                .withQueryTimeoutSecs(30);
        builder.setBolt("userPersistanceBolt", userPersistanceBolt).shuffleGrouping(bolt_kafka_id);

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology(StormKafkaTopo.class.getSimpleName(), new Config(), builder.createTopology());


    }
}
