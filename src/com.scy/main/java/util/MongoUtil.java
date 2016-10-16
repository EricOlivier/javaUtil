package util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by SCY on 16/10/16.
 * 默认mongo有关配置存在server.properties文件中包含:
 * mongo.url: mongo的url
 * mongo.port : mongo的端口
 * Maven配置需要包含: <dependency>
                         <groupId>org.mongodb</groupId>
                         <artifactId>mongo-java-driver</artifactId>
                         <version>2.13.0-rc1</version>
                         </dependency>
 *
 */

public final class MongoUtil {

    private static MongoClient mongoClient = null;

    private static Integer MONGO_PORT = 27017;

    private static String MONGO_HOST = "10.103.16.66";

    //线程池连接数
    private static Integer CONNECTION_POOL = 300;

    //每个链接上可以排队的线程数量
    private static Integer THREADS_ALLOWED_TO_BLOCK_FOR_CONNECTION_MUTIPLIER = 50;

    //每个线程获得可用连接的等待时间,若超过此时间,抛exception
    private static Integer MAXWAITTIME = (1000*6*5);

    //与数据库建立连接的Time out
    private static Integer TIME_OUT = 15000;

    //初始化
    static{
        try{
            InputStream pLoad = ClassLoader.getSystemResourceAsStream("server.properties");
            Properties properties =  new Properties();
            properties.load(pLoad);
            MONGO_PORT = Integer.valueOf(properties.getProperty("mongo.port"));
            MONGO_HOST = properties.getProperty("mongo.url");
            MongoClientOptions.Builder mongoBuild = new MongoClientOptions.Builder();
            mongoBuild.connectionsPerHost(CONNECTION_POOL);
            mongoBuild.threadsAllowedToBlockForConnectionMultiplier(THREADS_ALLOWED_TO_BLOCK_FOR_CONNECTION_MUTIPLIER);
            mongoBuild.maxWaitTime(MAXWAITTIME);
            mongoBuild.connectTimeout(TIME_OUT);
            ServerAddress mongoAddress = new ServerAddress(MONGO_HOST,MONGO_PORT);
            MongoClientOptions mongoClientOptions = mongoBuild.build();
            mongoClient = new MongoClient(mongoAddress,mongoClientOptions);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static MongoClient getMongoClient(){
        return mongoClient;
    }
}




