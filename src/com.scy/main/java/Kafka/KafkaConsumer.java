package Kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ExceptionAndError.ErrorCode;
import ExceptionAndError.MyException;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;


/**
 * Created by SCY on 16/8/9.
 *
 * 当你不需要立即获得结果, 但并发量又不能无限大时,差不多就是你需要使用消息队列的时候,蛤蛤蛤!
 *
 *
 * kafka消费端
 * 可以重写自己需要的consume操作(这里因为业务需求,我把consume的参数接口化了), 但是这里的一个KafkaConsumer的是一个实例,暂时只能对应一个consume操作,consume实际是去操作ConsumerIterator, 所以
 * 要是一个实例开了两个consume线程一起操作ConsumerIterator就需要考虑枷锁了
 *
 */
public class KafkaConsumer {
    private String topic;
    Configuration config;
    ConsumerConfig consumerConfig;
    ConsumerConnector kafkaConnector;
    ExecutorService executor;
    ConsumerIterator<String, String>consumerIterator;
    Map<String,List<KafkaStream<String,String>>> streams;

    private static final Logger LOG = Logger.getLogger(KafkaConsumer.class);


    public void construct (Configuration config, String topic){
        this.topic = topic;
        Properties props = new Properties();

        /**----读取kafka配置---------
         * zookeeper.host: 从config文件中得到host地址, 如果是集群,在properties文件中用逗号隔开如:127.0.0.1:9100,127.0.0.2:9101,127.0.0.3:9102
         * gourp.id 是consumer的group id
         * zookeeper.session.timeout: zookeeper的最大超时时间, 若是没反应, 就认为挂了
         * zookeeper.sync.time: 集群中leader和follower之间的同步时间
         * auto.commit.enable:  是否自动commit一个group id下消费消息的offset, 默认是true, 如果要手动commit,给false就好
         * auto.commit.interval.ms:   如果上一个设成自动,这个就是设置commit的间隔时间
         * fetch.size & fetch.message.max.bytes:   message.max.bytes规定consumer接受的一条消息最大的size, 需要和fetch.size同步,不然fetch.size小于message.max的话, consume并不能接到这个对应的消息,
         * 需要注意的是:  在broker端也有message.max.bytes这一选项,broker端的message.max.bytes要小于consume端的message.max.bytes,,不然broker接受到了超大小的消息 也并不能传给consume
         *
         * auto.offset.reset: 如果当前consume的group id在此topic下没有offset的时候, largest表示从最新消息消费,smallest从第一个消息开始消费
         *
        **/

        List<Object>kafkaHost = config.getList(ZK_HOST);
        String hostAll="";
        for(Object eachHost : kafkaHost){
            hostAll = hostAll + eachHost.toString() + ",";
        }
        hostAll = hostAll.substring(0,hostAll.length()-1);
        props.put("zookeeper.connect",hostAll);
        props.put("group.id", config.getString(CONSUMER_GROUP));
        props.put("zookeeper.session.timeout.ms", "1000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("fetch.size", "10000000");
        props.put("fetch.message.max.bytes", "10000000");
        props.put("rebalance.backoff.ms", "15000");
        props.put("rebalance.max.retries", "4");
        props.put("auto.offset.reset", "largest");

        consumerConfig = new kafka.consumer.ConsumerConfig(props);
        kafkaConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String,Integer>topicMap = new HashMap<String,Integer>();
        topicMap.put(topic,1);

        streams = kafkaConnector.createMessageStreams(topicMap,new StringDecoder(null), new StringDecoder(null));
        if(streams.get(topic).get(0) == null){
            throw new MyException(ErrorCode.GeneralError, "no stream found for topic:" + topic);
        }
        consumerIterator = streams.get(topic).get(0).iterator();
    }

    public void consume(final KafkaConsumeService kafkaConsumeService){
        executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {
            public void run() {
                while(true){
                    try{
                        while(consumerIterator.hasNext()){
                            kafkaConsumeService.doKafkaService(consumerIterator.next());
                        }
                    }
                    catch (Exception e){
                        LOG.error(e.getMessage());
                    }
                }
            }
        });
    }

    public void destroy(){
        executor.shutdown();
        kafkaConnector.commitOffsets(); //存储gourp id的offset
        kafkaConnector.shutdown();
    }




    public static String ZK_HOST = "kafka.zk.host";
    public static String ZK_PORT = "kafka.zk.port";
    public static String CONSUMER_GROUP = "kafka.consumer.group";
    public static String CONSUMER_TIMEOUT = "kafka.consumer.timeout.ms";




}
