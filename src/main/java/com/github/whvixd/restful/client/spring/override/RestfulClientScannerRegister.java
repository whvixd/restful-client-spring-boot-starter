package com.github.whvixd.restful.client.spring.override;

import com.github.whvixd.restful.client.annotation.RestfulClientScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link RestfulClientScan} 注册器
 * Created by whvixd on 2022/02/18.
 */
public class RestfulClientScannerRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes restfulClientScanAttrs = AnnotationAttributes
                .fromMap(annotationMetadata.getAnnotationAttributes(RestfulClientScan.class.getName()));
        if (restfulClientScanAttrs != null) {
            registerBeanDefinitions(annotationMetadata, restfulClientScanAttrs, registry,
                    generateBaseBeanName(annotationMetadata, 0));
        }
    }

    private void registerBeanDefinitions(AnnotationMetadata annotationMetadata, AnnotationAttributes annoAttrs,
                                         BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RestfulClientScannerConfigurer.class);
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
                .collect(Collectors.toList()));

        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
                .collect(Collectors.toList()));

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(annotationMetadata.getClassName()));
        }
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + RestfulClientScannerRegister.class.getSimpleName() + "#" + index;
    }
}
