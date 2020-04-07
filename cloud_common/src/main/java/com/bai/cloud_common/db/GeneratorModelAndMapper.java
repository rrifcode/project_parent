package com.bai.cloud_common.db;


import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author bai
 * @description 逆向生成model和mapper
 */
public class GeneratorModelAndMapper {

    public static void create(String url,
                              String username,
                              String password,
                              String filePath,
                              String packName,
                              String useActualName,
                              String pluginPath,
                              String env){

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //generatorConfiguration
        Configuration config = new Configuration();

        //   ... fill out the config object as appropriate...
        //创建上下文
        Context context = new Context(ModelType.FLAT);
        //对应的xml文件配置<context id="DB2Tables" targetRuntime="MyBatis3">
        context.setId("cloud");
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
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.ToStringPlugin");

        //自定义插件路径
        if(!ObjectUtils.isEmpty(pluginPath)){
            pluginConfiguration.setConfigurationType(pluginPath);
        }
        pluginConfiguration.addProperty("env", env);

        context.addPluginConfiguration(pluginConfiguration);

        //javaModelGenerator
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        //生成model的包路径
        javaModelGeneratorConfiguration.setTargetPackage(packName + ".dao.model");
        //生成model的项目路径
        javaModelGeneratorConfiguration.setTargetProject(filePath + "/src/main/java");
        javaModelGeneratorConfiguration.addProperty("constructorBased", "true");
        javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        javaModelGeneratorConfiguration.addProperty("enableSubPackages", "true");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        //javaClientGenerator
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage(packName + ".dao.mapper");
        javaClientGeneratorConfiguration.setTargetProject(filePath + "/src/main/java");
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        //sqlMapGenerator
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage("mapper.generator");
        sqlMapGeneratorConfiguration.setTargetProject(filePath + "/src/main/resources");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);


        //table
        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName("%");
        tableConfiguration.addProperty("useActualColumnNames", useActualName);
        context.addTableConfiguration(tableConfiguration);

        config.addContext(context);

        DefaultShellCallback callback = new DefaultShellCallback(true);
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

    }
}
