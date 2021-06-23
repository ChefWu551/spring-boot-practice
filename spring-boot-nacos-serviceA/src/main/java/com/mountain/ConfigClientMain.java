package com.mountain;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

public class ConfigClientMain {

    public static void main(String[] args) throws NacosException {
//        ConfigClientMain.getDefaultConfig();

        ConfigClientMain.getNamespaceConfig();

    }

    public static void getDefaultConfig() throws NacosException{
        String serverAddress = "127.0.0.1:8848";
        /*String group = "DEFAULT_GROUP";
        String dataId = "example";*/

        String group = "test";
        String dataId = "testA";

        ConfigService configService = NacosFactory.createConfigService(serverAddress);
        String config = configService.getConfig(dataId, group, 5000);

        System.out.println(config);
    }

    public static void getNamespaceConfig() throws NacosException{
        String namespaceId = "b06c247b-9df3-4d9b-8270-6638c0766d71";
        String group = "test";
        String dataId = "exmaple-test";
        String serverAddress = "127.0.0.1:8848";

        Properties properties = new Properties();
        // property的key   (ConfigService) constructor.newInstance(properties);
        /*
        private ClientWorker worker;
        private String namespace;
        private String encode;
        private ConfigFilterChainManager configFilterChainManager = new ConfigFilterChainManager();

        public NacosConfigService(Properties properties) throws NacosException {
            ....
            initNamespace(properties);
            ...
        }
        */
        properties.put("serverAddr", serverAddress);
        properties.put("namespace", namespaceId);

        ConfigService configService = NacosFactory.createConfigService(properties);
        String config = configService.getConfig(dataId, group, 5000);
        System.out.println(config);
    }
}
