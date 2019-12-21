package com.bai.cloud_user.config;


import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@Component
//@Primary
//@Slf4j
public class InitDataSource {

    //@Bean     //声明其为Bean实例
    public static void createDataSource(String url,
                                 String username,
                                 String password,
                                 String filePath,
                                 String packName){

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //generatorConfiguration
        Configuration config = new Configuration();

        //   ... fill out the config object as appropriate...
        //创建上下文
        Context context = new Context(ModelType.FLAT);
        //对应的xml文件配置<context id="DB2Tables" targetRuntime="MyBatis3">
        context.setId("user");
        context.setTargetRuntime("MyBatis3");

        //jdbc连接配置  <jdbcConnection>
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(url);
        jdbcConnectionConfiguration.setDriverClass("com.mysql.cj.jdbc.Driver");
        jdbcConnectionConfiguration.setUserId(username);
        jdbcConnectionConfiguration.setPassword(password);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        //javaTypeResolver

        //引入其他插件配置
        /*PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.ToStringPlugin");
        pluginConfiguration.setConfigurationType("com.bai.cloud_user.config.DefaultPlugin");
        context.addPluginConfiguration(pluginConfiguration);*/

        //javaModelGenerator
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        //生成model的包路径
        javaModelGeneratorConfiguration.setTargetPackage(packName + ".dao.model");
        //生成model的项目路径
        javaModelGeneratorConfiguration.setTargetPackage(filePath + "/src/main/java");
        javaModelGeneratorConfiguration.addProperty("constructorBased", "true");
        javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        javaModelGeneratorConfiguration.addProperty("enableSubPackages", "true");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        //sqlMapGenerator
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage("mapper.generator");
        sqlMapGeneratorConfiguration.setTargetProject(filePath + "/src/main/resources");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        //javaClientGenerator
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage(packName + ".dao.mapper");
        javaClientGeneratorConfiguration.setTargetProject(filePath + "/src/main/java");
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        //table
        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName("%");
        tableConfiguration.addProperty("useActualColumnNames", "false");

        config.addContext(context);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = null;
        try {
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //直接创建数据库
        /*DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        try {
            Class.forName(driverClassName);
            String url01 = datasourceUrl.substring(0,datasourceUrl.indexOf("?"));
            String url02 = url01.substring(0,url01.lastIndexOf("/"));
            String datasourceName = url01.substring(url01.lastIndexOf("/")+1);
            // 连接已经存在的数据库，如：mysql
            Connection connection = DriverManager.getConnection(url02, username, password);
            Statement statement = connection.createStatement();
            // 创建数据库
            statement.executeUpdate("create database if not exists `" + datasourceName + "` default character set utf8 COLLATE utf8_general_ci");
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasource;*/
    }


}
