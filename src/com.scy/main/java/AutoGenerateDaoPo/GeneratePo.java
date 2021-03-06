package AutoGenerateDaoPo;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SCY on 16/10/16.
 *
 * 根据DB table schema 产生对应的Po文件, 产生的Po文件需要用到JackSon来处理JSON包装
 * JACKSON的maven依赖
 *  <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
        <version>2.3.0</version>
    </dependency>
 *
 * 对内变量命名均是驼峰
 * 对外变量命名方式和DB一致
 *
 * @URL 是db的url
 * @NAME 链接db的用户名
 * @PASS 链接db的密码
 *
 *
 * 如果使用的sql JAR(JDBC 4.0之前)包是比较老的版本
 * 需要在使用前手动将DB的driver注册到DriverManager里,需要class.forName("对应的driver的class name,比如"com.mysql.jdbc.Driver"),执行里面的静态块去注册,或者干脆new一个对象也行
 * 4.0以后,DriverManager的getConnection方法直接自动注册了(md,看源码好费劲!),就不用显示调用了,不过调用了也没事,向前兼容的
 *
 */


public class GeneratePo {
    private static final String URL ="jdbc:mysql://10.101.1.140:3306/hahaha";
    private static final String NAME = "user_name";
    private static final String PASS = "password";
    private static final String DRIVER ="com.mysql.jdbc.Driver";

    private static final String tableName = "media_bprofile_schedule";

    private static final String outputFile = "/Users/a1/weMediaLog/testPo.java";

    private static final String className = "testPo";

    public GeneratePo(){
        Connection con;
        try{
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            con = DriverManager.getConnection(URL,NAME,PASS);
            DatabaseMetaData databaseMetaData = con.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null,null,tableName,null);
            Map<String,String>allCol = new HashMap<String, String>();
            while(resultSet.next()){
                String type = resultSet.getString("TYPE_NAME");
                if(type.equals("BIGINT")){
                    allCol.put(resultSet.getString("COLUMN_NAME"),BIGINT);
                }
                if(type.equals("VARCHAR")){
                    allCol.put(resultSet.getString("COLUMN_NAME"),VARCHAR);
                }
                if(type.equals("INT")){
                    allCol.put(resultSet.getString("COLUMN_NAME"),INT);
                }
                if(type.equals("TINYINT")){
                    allCol.put(resultSet.getString("COLUMN_NAME"),TINYINT);
                }
                if(type.equals("TEXT")){
                    allCol.put(resultSet.getString("COLUMN_NAME"),TEXT);
                }
                System.out.print(resultSet.getString("COLUMN_NAME") + "    ");
                System.out.println(resultSet.getString("TYPE_NAME"));
            }
            StringBuffer classHeadSB = new StringBuffer();
            classHeadSB.append("import com.fasterxml.jackson.annotation.JsonProperty; \n \n \n");
            classHeadSB.append("/**\n" +
                      " * Created by SCY\n"+
                      "**/ \n \n \n");
            outputStream.write(classHeadSB.toString().getBytes());
            outputStream.write(generateContent(allCol,className).getBytes());

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String generateContent( Map<String,String>allCol, String className){
        StringBuffer functionStart = new StringBuffer();
        functionStart.append("public class " + className + "{\n \n");


        StringBuffer fields = new StringBuffer();
        StringBuffer funcs = new StringBuffer();
        for(Map.Entry<String,String> entry : allCol.entrySet()){
            String varName = getCammelStr(entry.getKey(),"_");
            fields.append("@JsonProperty(\"" + entry.getKey() + "\") \n");
            fields.append("private " + entry.getValue() + " " + varName + "\n \n");

            funcs.append("public void " + "set"
                    +capitalFirstWord(varName) + "(" + entry.getValue() + " " + varName + "){ this." + varName + " = " + varName +"}\n \n");
            funcs.append("public " + entry.getValue() + " get" + capitalFirstWord(varName) + "(){return " + varName+ " }\n \n");
        }

        String output = functionStart.toString() + fields.toString() +"\n \n \n \n \n"+ funcs.toString() + "\n \n}";
        return output;
    }


    private String getCammelStr(String strIn, String splitStr){
        String output = "";
        String[] strList = strIn.split(splitStr);
        for(int count_x=0;count_x<strList.length;count_x++){
            if(count_x==0){
                output = strList[count_x];
            }
            else{
                output = output + capitalFirstWord(strList[count_x]);
            }
        }
        return output;

    }


    private String capitalFirstWord(String dataIn){
        String output="";
        for(int count_x=0;count_x<dataIn.length();count_x++){
            if(count_x==0){
                output =  dataIn.substring(0,1).toUpperCase();
            }
            else if(!dataIn.substring(count_x,count_x+1).equals("_")){
                output = output + dataIn.substring(count_x,count_x+1);
            }
        }
        return output;
    }




    private static String BIGINT = "Long";
    private static String VARCHAR = "String";
    private static String TEXT = "String";
    private static String TINYINT = "Integer";
    private static String INT = "Integer";


}
