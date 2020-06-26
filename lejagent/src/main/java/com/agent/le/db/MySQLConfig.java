package com.agent.le.db;

import com.agent.le.pojo.dto.ConfigInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: TODO
 */
public class MySQLConfig {
    private static final Logger logger = LoggerFactory.getLogger(MySQLConfig.class);
    /**
     * connection
     */
    private static Connection con;

    static {
        try {
            Properties properties = getProperties();
            String driverClassName = properties.getProperty("driverClassName");
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            Class.forName(driverClassName);
            con = DriverManager.getConnection(url, username, password);
            System.out.println("properties info  username:" + username + "  password:" + password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * get properties
     *
     * @return
     * @throws IOException
     */
    private static Properties getProperties() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    /**
     * 配置信息获取
     *
     * @return
     * @throws SQLException
     */
    public static Map<String, List<ConfigInfoDTO>> getConfInfo() throws SQLException {

        Map<String, List<ConfigInfoDTO>> configInfoMap = new HashMap<>(8);
        // 这里使用mysql作为配置持久化数据库，类名、方法名、添加类型、添加内容
        String sql = "select id, class_name,method_name,new_method_name,add_type,method_content,default_value from jagent_config  " +
                "where deleted=0";
        logger.info("查询SQL:{}", sql);

        //物理资源
        PreparedStatement pre = null;
        ResultSet res = null;
        try {
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();
            List<ConfigInfoDTO> configInfoList = new ArrayList<>();
            while (res.next()) {
                long id = res.getLong("id");
                String className = res.getString("class_name");
                String methodName = res.getString("method_name");
                String newMethodName = res.getString("new_method_name");
                Integer addType = res.getInt("add_type");
                String methodContent = res.getString("method_content");
                Integer defaultValue = res.getInt("default_value");
                ConfigInfoDTO configInfo = new ConfigInfoDTO();
                configInfo.setId(id);
                configInfo.setClassName(className);
                configInfo.setMethodName(methodName);
                configInfo.setNewMethodName(newMethodName);
                configInfo.setAddType(addType);
                configInfo.setMethodContent(methodContent);
                configInfo.setDefaultValue(defaultValue);
                configInfoList.add(configInfo);
            }
            //按照类纬度进行匹配
            configInfoMap = configInfoList.stream().collect(Collectors.groupingBy
                    (ConfigInfoDTO::getClassName));
            return configInfoMap;
        } catch (Exception e) {
            logger.error("mysql db query exception e", e);
        } finally {
            // release resource
            res.close();
            pre.close();
        }
        return configInfoMap;
    }
}
