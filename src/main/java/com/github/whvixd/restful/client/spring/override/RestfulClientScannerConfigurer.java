package com.github.whvixd.restful.client.spring.override;

import com.github.whvixd.restful.client.annotation.RestfulClientScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static org.springframework.util.Assert.notNull;

/**
 * {@link RestfulClientScan} 配置器
 * Created by whvixd on 2022/02/19.
 */
public class RestfulClientScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
    private ApplicationContext applicationContext;

    private String basePackage;

    private String beanName;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class,false, false);
        if (!prcs.isEmpty() && applicationContext instanceof ConfigurableApplicationContext) {
            BeanDefinition restfulClientScannerBean = ((ConfigurableApplicationContext) applicationContext).getBeanFactory().getBeanDefinition(beanName);

            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(beanName, restfulClientScannerBean);

            for (PropertyResourceConfigurer prc : prcs.values()) {
                prc.postProcessBeanFactory(factory);
            }

            PropertyValues values = restfulClientScannerBean.getPropertyValues();
            this.basePackage = getPropertyValue("basePackage", values);
        }
        this.basePackage = Optional.ofNullable(this.basePackage).map(this.applicationContext.getEnvironment()::resolvePlaceholders).orElse(null);


        ClassPathRestfulClientScanner scanner = new ClassPathRestfulClientScanner(registry);
        scanner.setResourceLoader(this.applicationContext);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // left intentionally blank
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private String getPropertyValue(String propertyName, PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else {
            return null;
        }
    }
}
