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
 *@getSqlSession()  获得一个SqlSession实例, 在用完后要使用sqlSession.close()关闭,不然一旦连接池里的实例用完了就会获取失败
 *
 */

public class MyBatisUtil {

    public static SqlSessionFactory sessionFactory;

    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream iS = Resources.getResourceAsStream(resource);
            sessionFactory = new SqlSessionFactoryBuilder().build(iS, "development");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static SqlSession getSqlSession() {
        SqlSession core = getSessionFactory().openSession(true);
        return core;
    }


    public static SqlSession getTransactinSession() {
        return getSessionFactory().openSession(TransactionIsolationLevel.REPEATABLE_READ);
    }
}