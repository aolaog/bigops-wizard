/*
 * Copyright 2018 www.yunweibang.com Inc. All rights reserved.
 */
package com.yunweibang.bigops.wizard.service.impl;

import com.yunweibang.bigops.common.JsonResponse;
import com.yunweibang.bigops.util.Constants;
import com.yunweibang.bigops.util.ExecUtil;
import com.yunweibang.bigops.wizard.model.WizardConfig;
import com.yunweibang.bigops.wizard.service.WizardService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author lpp
 */
@Service
public class WizardServiceImpl implements WizardService {
    private String path = "/opt/bigops/config/bigops.properties";
    @Override
    public JsonResponse generateConfig(WizardConfig config) {
        System.out.println(config);
        FileReader fileReader = null;
        JsonResponse response = new JsonResponse<Object>(null);
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            response = new JsonResponse<Object>(1005, "/opt/bigops/config/bigops.properties文件不存在", null);
            return response;
        }
        ExecUtil.execShell("chmod 777 /opt/bigops/config/bigops.properties");

        String ssoUrl = "sso.url";
        String homeUrl = "home.url";
        String dbUrl = "spring.datasource.url";
        String dbUrlPrefix = "jdbc:mysql://";
        String dbUrlSuffix = "?useSSL=false&useUnicode=true&autoReconnect=true&characterEncoding=UTF-8";
        String username = "spring.datasource.username";
        String password = "spring.datasource.password";
        String dataSourceUrl = dbUrlPrefix + config.getDbHost() + ":" + config.getDbPort() + "/" + config.getDbName() + dbUrlSuffix;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dataSourceUrl, config.getDbUser(), config.getDbPass());
        } catch (Exception e) {
            e.printStackTrace();
            response = new JsonResponse<Object>(1002, "连接设置错误或" + config.getDbName() + "数据库不存在", null);
            return response;
        }
        if (connection == null) {
            response = new JsonResponse<Object>(1002, "连接设置错误或" + config.getDbName() + "数据库不存在", null);
            return response;
        }
        ArrayList<String> tablesList = new ArrayList<>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = databaseMetaData.getTables(null, null, "%", null);
            while (tables.next()) {
                tablesList.add(tables.getString("TABLE_NAME"));
            }
            if (!tablesList.isEmpty()) {
                response = new JsonResponse<Object>(1003, config.getDbName() + "库已存在表，清空后再尝试安装。", tablesList);
                return response;
            } else {
                ScriptRunner runner = new ScriptRunner(connection);
                //设置字符集,不然中文乱码插入错误
                Resources.setCharset(Charset.forName("GBK"));
                //设置是否输出日志
                runner.setLogWriter(null);
                fileReader = null;
                try {
                    fileReader = new FileReader("/opt/bigops/install/mysql/bigops-1.0.0.sql");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    response = new JsonResponse<Object>(1005, "/opt/bigops/install/mysql/bigops-1.0.0.sql文件不存在", null);
                    return response;
                }
                runner.runScript(fileReader);
                runner.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> collect = Files.lines(Paths.get(path)).collect(Collectors.toList());
            collect.forEach(line -> {
                String str = line.trim();
                if (str.startsWith(ssoUrl)) {
                    stringBuilder.append(ssoUrl + Constants.STRING_EQUAL + config.getSsoUrl() + Constants.LINE_SEPARATOR);
                } else if (str.startsWith(homeUrl)) {
                    stringBuilder.append(homeUrl + Constants.STRING_EQUAL + config.getHomeUrl() + Constants.LINE_SEPARATOR);
                } else if (str.startsWith(username)) {
                    stringBuilder.append(username + Constants.STRING_EQUAL + config.getDbUser() + Constants.LINE_SEPARATOR);
                } else if (str.startsWith(password)) {
                    stringBuilder.append(password + Constants.STRING_EQUAL + config.getDbPass() + Constants.LINE_SEPARATOR);
                } else if (str.startsWith(dbUrl)) {
                    stringBuilder.append(dbUrl + Constants.STRING_EQUAL + dataSourceUrl + Constants.LINE_SEPARATOR);
                } else {
                    stringBuilder.append(str + Constants.LINE_SEPARATOR);
                }
            });
            Files.write(Paths.get(path), stringBuilder.toString().getBytes());
        } catch (Exception e) {
            response = new JsonResponse<Object>(1001, "修改" + path + "文件失败", null);
            return response;
        }

        return response;
    }

    @Override
    public String getHomeUrl() {
        try {
            Properties prop = new Properties();
            prop.load(new FileReader(path));
            return prop.getProperty("home.url").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
