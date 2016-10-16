package util;

/**
 * Created by SCY on 16/10/16.
 */

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.util.Properties;


/**
 * JDBCPOOL的实现类
 */
public class JDBCPoolDataSourceFactory implements DataSourceFactory {
    private DataSource dataSource;

    /**
     * 构造器
     */
    public JDBCPoolDataSourceFactory() {
        dataSource = new DataSource();
    }

    /**
     * 获取JDBCPOOL数据源
     */
    public DataSource getDataSource() {
        return dataSource;
    }


    /**
     * 对JDBCPOOL属性值进行设置
     */
    public void setProperties(Properties properties) {
        PoolProperties p = new PoolProperties();

        p.setUrl(properties.getProperty("url"));
        p.setDriverClassName(properties.getProperty("driver"));
        p.setUsername(properties.getProperty("username"));
        p.setPassword(properties.getProperty("password"));

        if (properties.getProperty("JmxEnabled") != null) {
            if (properties.getProperty("JmxEnabled").equals("true")) {
                p.setJmxEnabled(true);
            } else {
                p.setJmxEnabled(false);
            }
        }


        if (properties.getProperty("testWhileIdle") != null) {
            if (properties.getProperty("testWhileIdle").equals("true")) {
                p.setTestWhileIdle(true);
            } else if (properties.getProperty("testWhileIdle").equals("false")) {
                p.setTestWhileIdle(false);
            }
        }

        if (properties.getProperty("testOnBorrow") != null) {
            if (properties.getProperty("testOnBorrow").equals("false")) {
                p.setTestOnBorrow(false);
            } else {
                p.setTestOnBorrow(true);
            }
        }

        if (properties.getProperty("validationQuery") != null) {
            p.setValidationQuery(properties.getProperty("validationQuery"));
        }

        if (properties.getProperty("testOnReturn") != null) {
            if (properties.getProperty("testOnReturn").equals("true")) {
                p.setTestOnReturn(true);
            } else if (properties.getProperty("testOnReturn").equals("false")) {
                p.setTestOnReturn(false);
            }
        }
        if (properties.getProperty("validationInterval") != null) {
            p.setValidationInterval(Long.valueOf(properties.getProperty("validationInterval")));
        }
        if (properties.getProperty("timeBetweenEvictionRunsMillis") != null) {
            p.setTimeBetweenEvictionRunsMillis(Integer.valueOf(properties.getProperty("timeBetweenEvictionRunsMillis")));
        }
        if (properties.getProperty("maxActive") != null) {
            p.setMaxActive(Integer.valueOf(properties.getProperty("maxActive")));
        }
        if (properties.getProperty("initialSize") != null) {
            p.setInitialSize(Integer.valueOf(properties.getProperty("initialSize")));
        }
        if (properties.getProperty("maxWait") != null) {
            p.setMaxWait(Integer.valueOf(properties.getProperty("maxWait")));
        }
        if (properties.getProperty("removeAbandonedTimeout") != null) {
            p.setRemoveAbandonedTimeout(Integer.valueOf(properties.getProperty("removeAbandonedTimeout")));
        }
        if (properties.getProperty("minEvictableIdleTimeMillis") != null) {
            p.setMinEvictableIdleTimeMillis(Integer.valueOf(properties.getProperty("minEvictableIdleTimeMillis")));
        }
        if (properties.getProperty("minIdle") != null) {
            p.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
        }

        if (properties.getProperty("validationQuery") != null) {
            p.setValidationQuery(properties.getProperty("validationQuery"));
        }

        if (properties.getProperty("logAbandoned") != null) {
            if (properties.getProperty("logAbandoned").equals("true")) {
                p.setLogAbandoned(true);
            } else if (properties.getProperty("logAbandoned").equals("false")) {
                p.setLogAbandoned(false);
            }
        }
        if (properties.getProperty("removeAbandoned") != null) {
            if (properties.getProperty("removeAbandoned").equals("true")) {
                p.setRemoveAbandoned(true);
            } else if (properties.getProperty("removeAbandoned").equals("false")) {
                p.setRemoveAbandoned(false);
            }
        }
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        p.setConnectionProperties("connectTimeout=10000;socketTimeout=10000");

        this.dataSource.setPoolProperties(p);
    }

}