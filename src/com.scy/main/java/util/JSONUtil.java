package util;



import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Map;

/**
 * Created by SCY on 16/10/20.
 * java常用的JSON序列化JACKSON的Util
 * 单例模式
 * 其他特殊设置在static block中对objectMapper完成
 *
 */


public class JSONUtil {
    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();

        //允许空对象
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //允许映射不上的列存在
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    private JSONUtil() {
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    public static Map<String, Object> getMapFromJSONString(String JSON) {
        Map<String, Object> res;
        try {
            res = JSONUtil.getInstance().readValue(JSON, Map.class);
        } catch (Exception e) {
            throw new Error("JSON格式转换错误");
        }
        if (res == null) {
            throw new Error("JSON格式转换错误");
        }
        return res;
    }
}
