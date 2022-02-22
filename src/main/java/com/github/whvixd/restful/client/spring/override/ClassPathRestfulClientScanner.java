package com.github.whvixd.restful.client.spring.override;

import com.github.whvixd.restful.client.RestfulClientFactoryBean;
import com.github.whvixd.restful.client.annotation.RestfulClientScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * {@link RestfulClientScan} 扫表器
 * Created by whvixd on 2022/02/19.
 */
@Slf4j
public class ClassPathRestfulClientScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends RestfulClientFactoryBean> restfulClientFactoryBeanClass = RestfulClientFactoryBean.class;
    // Copy of FactoryBean#OBJECT_TYPE_ATTRIBUTE which was added in Spring 5.2
    private static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";

    public ClassPathRestfulClientScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        // 根据过滤器扫表包下的class，转成BeanDefinitionHolder
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            log.warn("No Restful client was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        AbstractBeanDefinition definition;
        BeanDefinitionRegistry registry = getRegistry();
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (AbstractBeanDefinition) holder.getBeanDefinition();
            if (ScopedProxyFactoryBean.class.getName().equals(definition.getBeanClassName())) {
                definition = (AbstractBeanDefinition) Optional
                        .ofNullable(((RootBeanDefinition) definition).getDecoratedDefinition())
                        .map(BeanDefinitionHolder::getBeanDefinition).orElseThrow(() -> new IllegalStateException(
                                "The target bean definition of scoped proxy bean not found. Root bean definition[" + holder + "]"));
            }
            String beanClassName = definition.getBeanClassName();
            if (beanClassName == null) {
                log.error("beanClassName is null");
                throw new BeanCreationException("beanClassName is null");
            }
            log.debug("Creating RestfulClientFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' restfulClientInterface");

            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);

            // FactoryBean添加进去，实例化bean
            definition.setBeanClass(this.restfulClientFactoryBeanClass);
            definition.setAttribute(FACTORY_BEAN_OBJECT_TYPE, beanClassName);


            if (!definition.isSingleton()) {
                BeanDefinitionHolder proxyHolder = ScopedProxyUtils.createScopedProxy(holder, registry, true);
                if (registry.containsBeanDefinition(proxyHolder.getBeanName())) {
                    registry.removeBeanDefinition(proxyHolder.getBeanName());
                }
                registry.registerBeanDefinition(proxyHolder.getBeanName(), proxyHolder.getBeanDefinition());
            }

        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            log.warn("Skipping RestfulClientFactoryBean with name '" + beanName + "' and '"
                    + beanDefinition.getBeanClassName() + "' restfulClientInterface" + ". Bean already defined with the same name!");
            return false;
        }
    }

    public void registerFilters() {
        // 可以添加过滤器，实现的某个接口的类
        addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }


}
