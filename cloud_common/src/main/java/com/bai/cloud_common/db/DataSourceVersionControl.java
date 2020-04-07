package com.bai.cloud_common.db;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author bai
 */
public class DataSourceVersionControl {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST = "localhost:3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private static void init(String filePath,
                             String packName,
                             String useActualName,
                             String pluginPath,
                             String env) {
        Connection connection = null;
        Statement statement = null;
        String dropSql = null;
        try {
            String url = getUrl(HOST, "mysql");
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            statement = connection.createStatement();
            String tempDB = "temp_" + System.currentTimeMillis();
            String createSql = "CREATE DATABASE " + tempDB;
            dropSql = "DROP DATABASE " + tempDB;
            statement.execute(createSql);
            String tempDBUrl = getUrl(HOST, tempDB);
            flywayMigrate(tempDBUrl, USERNAME, PASSWORD);
            GeneratorModelAndMapper.create(tempDBUrl, USERNAME, PASSWORD, filePath, packName, useActualName, pluginPath, env);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
        DataSourceVersionControl.init(args[0], args[1], args[2], args[3], args[4]);
    }

}
