package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.NacosConstants;
import com.alibaba.csp.sentinel.dashboard.rule.NacosPropertiesConfiguration;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 52361
 * @Description
 * @create 2022-03-15 11:13
 */
@Service
public class FlowRuleNacosProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {
    private final Logger LOGGER = LoggerFactory.getLogger(FlowRuleNacosProvider.class);

    @Autowired
    private NacosPropertiesConfiguration nacosPropertiesConfiguration;
    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String,List<FlowRuleEntity>> converter;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        String dataID = new StringBuilder(appName).append(NacosConstants.DATA_ID_POSTFIX).toString();
        String rules = configService.getConfig(dataID,nacosPropertiesConfiguration.getGroupId(),3000);
        LOGGER.info("pull FlowRule from Nacos config:{}",rules);
        if(StringUtils.isEmpty(rules)){
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
