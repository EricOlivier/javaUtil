package util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SCY on 16/10/19.
 * 主要包含发送Http get和post请求
 * 根据实际情况,调整Http head的部分属性即可
 *
 * 如果不需要LOG, 把LOG相关的干掉就行,需要LOG4J
 */

public class HttpUtil {
    private static final int READ_TIME_OUT = 5000;
    private static final int CONNECT_TIME_OUT = 2000;


    private static final Logger LOG = Logger.getLogger(HttpUtil.class);

    public static String sendGet(String inURL) {
        return sendGet(inURL, true);
    }

    public static String sendGet(String inURL, boolean isLog) {
        String result = sendGet(inURL, isLog, READ_TIME_OUT);
        return result;
    }

    public static String sendGet(String inURL, boolean isLog, int readTimeOut){
        LOG.info("Call Data From: " + inURL);
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL obj = new URL(inURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setConnectTimeout(CONNECT_TIME_OUT);
            con.setReadTimeout(readTimeOut);
            // optional default is GET
            con.setRequestMethod("GET");
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();

        } catch (Exception e) {
            LOG.error("发送GET请求出现异常", e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (isLog) {
            LOG.info("Get Data: " + result.toString());
        }
        return result.toString();
    }


    public static String sendPost(String url, String param) {
        return sendPost(url, param, true);
    }

    public static String sendPostNew(String url, String param, boolean isLog) {
        LOG.info("Call Data From: " + url);
        if (isLog) {
            LOG.info("Post Body: " + param);
        }
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(READ_TIME_OUT);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
            conn.setRequestProperty("Content-Length", "" + param.length());
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (isLog) {
            LOG.info("Get Data: " + result);
        }
        return result;
    }

    public static String sendPost(String url, String param, boolean isLog) {
        LOG.info("Call Data From: " + url);
        if (isLog) {
            LOG.info("Post Body: " + param);
        }
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(READ_TIME_OUT);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (isLog) {
            LOG.info("Get Data: " + result);
        }
        return result;
    }




















}
