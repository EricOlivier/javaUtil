package util;

import ExceptionAndError.ErrorCode;
import ExceptionAndError.myException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by SCY on 16/10/19.
 *
 * 需要和ExceptionAndError中的自定义的myException和ErrorCode一起用
 * 也可以替换成jdk自带的
 */

public class HttpHelper {
    private static final Logger LOG = Logger.getLogger(HttpHelper.class);

    //正常返回用对象
    public static class SuccessPO<T> {
        public String status;
        public T result;
        public int code;

        SuccessPO(T inRes) {
            this.result = inRes;
            status = "success";
            code = 0;
        }
    }

    //异常返回用对象
    public static class FailPO {
        public String status;
        public String reason;
        public int code;

        FailPO(int code, String message) {
            this.reason = message;
            this.status = "failed";
            this.code = code;
        }
    }

    /**
     * 组装正常返回
     * 注意这里会进行JSON编码，所以要使用对象传递List之类的容器，否则String部分有可能二次编码
     *
     * @param response 返回对象
     * @param body     返回核心内容
     * @param <T>      核心部分类型
     */
    public static <T> void sendSuccess(HttpServletResponse response, T body) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            SuccessPO<T> success = new SuccessPO<T>(body);
            LOG.debug("OUTPUT:" + getStringForLog(success));
            response.getWriter().println(JSONUtil.getInstance().writeValueAsString(success));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            sentFiled(response, ErrorCode.GeneralError, e.getMessage());
        }
    }


    /**
        跨域返回,需要前后端对好,这里按照一般的格式写好
        特别注意,跨域返回的Http头要设置成text/javascript, 这样前端因为跨域请求的JS才能正确返回,,,,
        当然也特么有不需要的,但写成text/javascript肯定不会错
        返回的的body是一个前端需要的call back函数
     @callBack 这个参数是前端传来的call back函数名

     */

    public static <T> void sendSuccessCrossOrigin(HttpServletResponse response, T body, String callBack ){
        try{
            response.setContentType("text/javascript");
            response.setStatus(HttpServletResponse.SC_OK);
            SuccessPO<T> success = new SuccessPO<T>(body);
            String finalRes = callBack + "&&" + callBack +"(" + JSONUtil.getInstance().writeValueAsString(success) + ")";
            response.getWriter().println(finalRes);
        } catch(Exception e){
            LOG.error(e.getMessage(),e);
            sentFiledCrossOrigin(response,ErrorCode.GeneralError, e.getMessage(), callBack);
        }
    }

    /**
     * 组装异常返回部分
     *
     * @param response     返回对象
     * @param errorMessage 错误原因
     */
    public static void sentFiled(HttpServletResponse response, ErrorCode errorCode, String errorMessage) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            FailPO fail = new FailPO(errorCode.getCode(), errorMessage);
            LOG.error("OUTPUT: " + getStringForLog(fail));
            response.getWriter().println(JSONUtil.getInstance().writeValueAsString(fail));
        } catch (Exception e) {
            //严重异常回滚
            LOG.error(e.getMessage(), e);
        }
    }


    public static void sentFiledCrossOrigin(HttpServletResponse response, ErrorCode errorCode, String errorMessage, String callBack) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            FailPO fail = new FailPO(errorCode.getCode(), errorMessage);
            LOG.error("OUTPUT: " + getStringForLog(fail));
            String res = callBack + "&&" + callBack + "(" + JSONUtil.getInstance().writeValueAsString(fail) + ")";
            response.getWriter().println(res);
        } catch (Exception e) {
            //严重异常回滚
            LOG.error(e.getMessage(), e);
        }
    }


    public static String getJsonBody(InputStream in) {
        String str;
        try {
            String encode = "utf-8";
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuffer sb = new StringBuffer();

            while ((str = reader.readLine()) != null) {
                sb.append(str).append("\n");
            }
            reader.close();
            LOG.info("POST BODY: " + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new myException(ErrorCode.WrongParameters, "Cannot get the POST Body");
        }
    }

    private static String getStringForLog(Object jsonObject) {
        try {
            String str = JSONUtil.getInstance().writeValueAsString(jsonObject);
//            if (str.length() > 1000) {
//                return str.substring(0, 1000) + "...";
//            } else {
            return str;
//            }
        } catch (Exception e) {
            return null;
        }
    }


}

