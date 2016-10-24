package Kafka;


import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;

import java.util.Properties;



/**
 * Created by SCY on 16/10/21.
 *
 * 需要的maven依赖和在txt文件中
 *
 */

public class KafkaProducer {
    Logger LOG = Logger.getLogger(KafkaProducer.class);

    private static Producer realProducer;

    public KafkaProducer(){
        Properties props = new Properties();


        /** 参数解释
         *  metadata.broker.list(新版本的Kafka里叫bootstrap.servers)  kafka集群的位置,有多个的话,用逗号分割
         *  其实也可以只传一个broker的地址(原因这个答案很详细:http://stackoverflow.com/questions/38893915/apache-kafka-producer-broker-connection/38898019#38898019)
         *  producer.type: 是否允许kafka另起一线程异步的发送消息, 1. "async" : 可以异步发送     2. "sync": 同步发送,调用完kafka instance的send(),就发送
         *  request.required.acks:  控制发送消息是否要得到对应的broker的返回,  0:不等,只管发     1:只等leader broker的返回,    -1:不仅要等leader,还要等所有的followers的ack返回 (消息不重要,丢了就丢了就0就行,一般般重要就1)
         *  seiralizer.class : 消息的序列化方式, 如果用String就kafka.producer.StringEncoder就行, 也可以使用custom的序列化方式, 比如想用JSON序列化, 可以自己创建一个JSONEncode类
         *                      这个类需要实现Encoder<Object>接口, 同时需要重写toBytes类 具体参考: http://stackoverflow.com/questions/23755976/kafka-writing-custom-serializer/24024119#24024119
         *  compression.codec: 压缩方法,三个可选值: none,  gzip,   snappy
         *  partitioner.class: 把每个消息分到各个partition的方法, default的方法是根据message的key的hash值
         *
         */


        props.put("metadata.broker.list", metadata_broker_list);
        props.put("producer.type", producer_type);
        props.put("request.required.acks", required_acks);
        props.put("compression.codec", "snappy");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
        ProducerConfig config = new ProducerConfig(props);
        realProducer = new Producer(config);

    }

    public static Producer getInstance(){
        if (realProducer == null){
            new KafkaProducer();
        }
        return realProducer;
    };


    public String metadata_broker_list="10.103.16.44:9092";
    public String producer_type="sync";
    public String required_acks="0";
    public String topic="input_normal";
    public String url="10.103.16.44:9092";

}
