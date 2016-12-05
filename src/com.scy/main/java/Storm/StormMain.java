package Storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SCY on 16/12/1.
 */
public class StormMain {


    //创建一个storm的topology并且使用分两个大部分
    //第一部分build storm的topology, 即它的spout和bolt等等
    //第二部分,配置storm集群的信息
    static public void createStormTopology(){


        TopologyBuilder builder = new TopologyBuilder();

        Map<String,String>kafkaSpoutConfig = new HashMap<String, String>();


        //这里的第一个参数是构造kafka consumer的一些参数,但是也可以在Util里面直接写
        String spoutOneID = "spout-one-id";
        SpoutConfig spoutOneConfig = StormUtil.getKafkaSpout(kafkaSpoutConfig,"spout-one-topic");

        String spoutTwoID = "spout-two-id";
        SpoutConfig spoutTwoConfig = StormUtil.getKafkaSpout(kafkaSpoutConfig,"spout-two-topic");

        //第一个参数是这个spout在topology里的id, 接这个spout消息的bolt需要使用到, 最后一个参数是设置多少个这个spout
        builder.setSpout(spoutOneID, new KafkaSpout(spoutOneConfig), 2);
        builder.setSpout(spoutTwoID, new KafkaSpout(spoutTwoConfig), 3);







        Config config = new Config();
        config.put(Config.STORM_ZOOKEEPER_SERVERS,"host of storm zookeeper");
        //worker的数量即是这个storm集群要开多少个进程来做提交的topology的任务
        config.setNumWorkers(10);
        //设置这个topology任务里有多少个executor去处理acker的确认, 如果设置为0,那么当一个一个tuple从spout发出去后,
        //马上就会被ack掉,那么就不保证reliability了
        config.setNumAckers(2);
        //设置从spout发出去,但是下层的bolt还未处理的(包括新来的和fail的tuple), 假设是2,当累计了2个从这个spout穿出去的tuple
        //未被bolt处理时,spout会暂停从外界读数据
        config.setMaxSpoutPending(10);
        //设置这个topology产生tick tuple的时间间隔
        config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,60*5);


        //如果是本地测试storm,用他提供的localcuster启
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("topology name",config,builder.createTopology());




    }





}


