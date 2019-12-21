package com.bai.cloud_user.config;

import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

public class DefaultPlugin extends PluginAdapter {


    @Override
    public boolean validate(List<String> list) {
        return true;
    }
}
