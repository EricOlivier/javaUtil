package util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by SCY on 16/10/16.
 *
 * Redis Util, Maven管理需要<dependency>
                                <groupId>redis.clients</groupId>
                                <artifactId>jedis</artifactId>
                                <version>2.7.3</version>
                            </dependency>
                           版本2.7以上
 * 注意每次调用
 * @getJedis() 获取实例
 * @returnResource()    使用完Redis实例后,需要手动调用归还资源池
 * 有关Redis链接的配置写在server.properties中
 */
public final class RedisUtil {

    //Redis服务器IP
    //private static String ADDRESS = "127.0.0.1";
    private static String ADDRESS ;

    //Redis的端口号
    private static int PORT ;


    //访问密码
    private static String AUTH = null;

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;

    private static int TIMEOUT = 10000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            //Get property
            InputStream in = ClassLoader.getSystemResourceAsStream("server.properties");
            Properties p = new Properties();
            p.load(in);

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);

            //如果server.properties里没有设置,则默认0号db
            int dbIndex = Integer.valueOf(p.getProperty("redis.dbIndex","0"));

            jedisPool = new JedisPool(config, p.getProperty("redis.url"), Integer.valueOf(p.getProperty("redis.port")), TIMEOUT, AUTH, dbIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}