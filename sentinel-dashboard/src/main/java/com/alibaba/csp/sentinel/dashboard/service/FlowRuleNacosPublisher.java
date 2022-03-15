package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.NacosConstants;
import com.alibaba.csp.sentinel.dashboard.rule.NacosPropertiesConfiguration;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 52361
 * @Description
 * @create 2022-03-15 11:19
 */
@Service
public class FlowRuleNacosPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

    private final Logger LOGGER = LoggerFactory.getLogger(FlowRuleNacosPublisher.class);

    @Autowired
    private NacosPropertiesConfiguration nacosPropertiesConfiguration;
    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<FlowRuleEntity>,String> converter;

    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app,"app cannot be empty");
        if(rules==null){
            return;
        }
        String dataID = new StringBuilder(app).append(NacosConstants.DATA_ID_POSTFIX).toString();
        configService.publishConfig(dataID,nacosPropertiesConfiguration.getGroupId(),converter.convert(rules));
    }
}
