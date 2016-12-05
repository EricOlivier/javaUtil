package Storm;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicBoltExecutor;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by SCY on 16/12/5.
 *
 * 继承自BaseBasicBolt, BaseBasicBolt继承BaseComponent并实现IBasicBolt, 这一套体系不需要手动ack
 * 执行完excute之后,就自动ack掉收到的tuple
 *
 */
public class StormBoltFromBaseBasicBolt extends BaseBasicBolt{

    //storm起一个topology的这个bolt时进行的初始化操作
    @Override
    public void prepare(Map stormConf, TopologyContext context){

    }

    //这个bolt每接到一个tuple后,都会调用这个execute
    public void execute(Tuple input, BasicOutputCollector collector) {
        String inputData = input.getStringByField("declare_str");
        /*
        各种自己的处理逻辑
        注意,继承了BaseBasicBolt的bolt在storm里面会被放在BasicBoltExecutor里面执行,
        BasicBoltExecutor里的emit自动完成了ack().
        的因为每个execute完后,BaseBasciBolt会自动ack掉这个tuple, 所以即便处理失败了,也会ack掉
        所以如果想处理失败后重新处理,需要手动throw FailedException 手动fail来用于重新处理
         */


        //如果这个bolt需要再往外传数据,使用emit发送,emit的这个Values里的东西要和declare里的Fields对应
        collector.emit(new Values("value1","value2","value3"));
    }




    //设置这个bolt的emit出去的的格式
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("key1","key2","key3"));
    }








}
