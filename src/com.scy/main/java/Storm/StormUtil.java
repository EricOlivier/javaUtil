package Storm;

import backtype.storm.Constants;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Tuple;
import storm.kafka.BrokerHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.Map;

/**
 * Created by SCY on 16/12/1.
 */
public class StormUtil {

    public static SpoutConfig getKafkaSpout(Map<String, String> conf, String topicName) {


        final String clientId = topicName + conf.get("data-in-kafka-client-id");

        BrokerHosts hosts = new ZkHosts(conf.get("data-in-kafka-broker"));
        /*
            SpoutConfig 构造函数,第一个para其实是zookeeper的地址, 不知道为啥类名是brokerhost
            第二个如名
            第三个是zookeeper下的路径
            第四个是clientID 就是kafka consumer的group id
        */
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/violet/consumers/test/" + topicName, clientId);
        spoutConfig.fetchSizeBytes = Integer.parseInt(conf.get("kafka-fetch-size"));
        spoutConfig.forceFromStart = false;
        spoutConfig.zkPort = 2181;
        //这个scheme就是设置kafka spout的发出去的tuple的declare,  StringScheme就是key = "str", V是把从外接到的消息打成字符串的一个字符串
        //那么接这个kafka spout的bolt 取得tuple的值就如:
        //String inData = tuple.getStringByField("str")
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());


        return spoutConfig;
    }

    //判断是不是tick tuple
    public static boolean isTickTuple(Tuple tuple) {
        if (tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) &&
                tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID)) {
            return true;
        } else {
            return false;
        }
    }


}
