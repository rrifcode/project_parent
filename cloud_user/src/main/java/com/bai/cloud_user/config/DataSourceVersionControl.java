package com.bai.cloud_user.config;

import com.netflix.discovery.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceVersionControl {

    @Value("${spring.datasource.url}")
    private static String URL;
    private static String DB_NAME = "mysql";
    private static String HOST = "localhost:3306";
    @Value("${spring.datasource.driver-class-name}")
    private static String DRIVER_CLASS_NAME;
    @Value("${spring.datasource.username}")
    private static String USERNAME;
    @Value("${spring.datasource.password}")
    private static String PASSWORD;

    private static void init(String filePath,
                             String packName){
        Connection connection = null;
        Statement statement = null;
        String dropSql = null;
        try {
            Class.forName(DRIVER_CLASS_NAME);
            /*Class.forName("com.mysql.jdbc.Driver");*/
            System.out.println(Class.forName(DRIVER_CLASS_NAME).getName());
            connection = DriverManager.getConnection(getUrl(HOST, DB_NAME), USERNAME, PASSWORD);
            statement = connection.createStatement();

            String datasourceName = StringUtils.substringBetween(URL,"/", "?");
            /*String url01 = URL.substring(0,URL.indexOf("?"));
            String url02 = url01.substring(0,url01.lastIndexOf("/"));
            String datasourceName = url01.substring(url01.lastIndexOf("/")+1);*/

            String createSql ="CREATE DATABASE IF NOT EXISTS `" + datasourceName + "` default character set utf8 COLLATE utf8_general_ci";
            dropSql = "DROP DATABASE IF EXISTS `" + datasourceName + "`";
            statement.execute(createSql);
            flywayMigrate(URL, USERNAME, PASSWORD);
            InitDataSource.createDataSource(URL, USERNAME, PASSWORD, filePath, packName);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                statement.execute(dropSql);
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private static String getUrl(String host,String dbName){
        return "jdbc:mysql://" + host + "/" + dbName + "?useSSL=false&serverTimezone=UTC&nullCatalogMeansCurrent=true";
    }


    private static void flywayMigrate(String url, String username, String password){
        Flyway flyway = Flyway.configure().dataSource(url, username, password).load();
        flyway.migrate();
    }

    public static void main(String[] args) {
        DataSourceVersionControl.init(args[0], args[1]);
    }

}
