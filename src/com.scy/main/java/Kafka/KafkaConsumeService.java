package Kafka;

import kafka.message.MessageAndMetadata;

/**
 * Created by SCY on 16/8/11.
 *
 * 需要在消费消息时调用的函数的类,只需要实现此接口,然后用KafkaConsumer调用consume方法,将相应的方法开启一个新线程即可
 *
 */
public interface KafkaConsumeService {
    public void doKafkaService(MessageAndMetadata msg);
}
