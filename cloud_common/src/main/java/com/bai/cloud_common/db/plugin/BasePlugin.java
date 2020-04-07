package com.bai.cloud_common.db.plugin;

import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * @Author bai
 * @Date 2020/4/8 1:28
 */
public class BasePlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }
}
