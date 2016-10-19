package util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SCY on 16/10/16.
 *
 * 静态工厂实现sqlSession
 * 此Util需要和JDBC dataSource配合使用
 * 读取mybatis-config.xml配置信息,其中的 <dataSource>标签中选择JDBCPoolDataSourceFactory
 * mybatis-config.xml中也读取配置文件db.properties中关于sql的url, password等信息
 *
 * 如果需要链接两套数据库,可以使用code中的注释部分, 但是需要在mybatis-config.xml中多添加一套<enviroment></enviroment>标签,
 *
 *@getSqlSession()  获得一个SqlSession实例, 在用完后要使用sqlSession.close()关闭,不然一旦连接池里的实例用完了就会获取失败
 *
 */

public class MyBatisUtil {

    public static SqlSessionFactory sessionFactory;

    //public static SqlSessionFactory sessionFactorySec;

    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream iS = Resources.getResourceAsStream(resource);
            //第二个参数development 对应mybatis-config.xml中environment标签的id
            sessionFactory = new SqlSessionFactoryBuilder().build(iS, "development");

            //sessionFactorySec = new SqlSessionFactoryBuilder().build(iS, "dddd");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    //public static SqlSessionFactory getSessionFactorySec() {return sessionFactorySec; }

    public static SqlSession getSqlSession() {
        SqlSession core = getSessionFactory().openSession(true);
        return core;
    }


    /*
    public static SqlSession getSqlSessionSec(){
        SqlSession core = getSessionFactorySec().openSession(true);
    }

    */
    public static SqlSession getTransactinSession() {
        return getSessionFactory().openSession(TransactionIsolationLevel.REPEATABLE_READ);
    }
}