//package com.surge.common.doc.config.properties;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.ExternalDocumentation;
//import io.swagger.v3.oas.models.Paths;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.License;
//import io.swagger.v3.oas.models.tags.Tag;
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.NestedConfigurationProperty;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * swagger 配置属性
// *
// * @author lichunqing
// */
//@Data
//@ConfigurationProperties(prefix = "springdoc")
//public class SpringDocProperties {
//
//    /**
//     * 文档基本信息
//     */
//    @NestedConfigurationProperty
//    private InfoProperties info = new InfoProperties();
//
//    /**
//     * 扩展文档地址
//     */
//    @NestedConfigurationProperty
//    private ExternalDocumentation externalDocs;
//
//    /**
//     * 标签
//     */
//    private List<Tag> tags = null;
//
//    /**
//     * 路径
//     */
//    @NestedConfigurationProperty
//    private Paths paths = null;
//
//    /**
//     * 组件
//     */
//    @NestedConfigurationProperty
//    private Components components = null;
//
//    /**
//     * 服务文档路径映射 参考 gateway router 配置
//     * 默认为服务名去除前缀转换为path 此处填特殊的配置
//     */
//    private Map<String, String> serviceMapping = null;
//
//    /**
//     * <p>
//     * 文档的基础属性信息
//     * </p>
//     *
//     * @see io.swagger.v3.oas.models.info.Info
//     *
//     * 为了 springboot 自动生产配置提示信息，所以这里复制一个类出来
//     */
//    @Data
//    public static class InfoProperties {
//
//        /**
//         * 标题
//         */
//        private String title = null;
//
//        /**
//         * 描述
//         */
//        private String description = null;
//
//        /**
//         * 联系人信息
//         */
//        @NestedConfigurationProperty
//        private Contact contact = null;
//
//        /**
//         * 许可证
//         */
//        @NestedConfigurationProperty
//        private License license = null;
//
//        /**
//         * 版本
//         */
//        private String version = null;
//
//    }
//
//}
