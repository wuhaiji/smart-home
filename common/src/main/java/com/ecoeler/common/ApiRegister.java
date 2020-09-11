package com.ecoeler.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * API注册
 * @author tang
 * @since 2020/9/7
 */
public class ApiRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware , BeanFactoryAware {

    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;

    /**
     * 注册bean定义
     * @param annotationMetadata 被 import 的类的 注解
     * @param beanDefinitionRegistry bean定义 注册器
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
        scanner.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Configuration.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Repository.class));
        //scanner.addIncludeFilter(new AnnotationTypeFilter(Configuration.class));
        scanner.setResourceLoader(this.resourceLoader);

        List<Class<?>> annotationList = this.getAnnotations(annotationMetadata);
        Set<String> basePackages = new HashSet<>();

        for (Class type : annotationList) {
            if (type.isAnnotationPresent(EnableApi.class)) {
                EnableApi enableApi = AnnotatedElementUtils.findMergedAnnotation(type, EnableApi.class);
                if(enableApi!=null && enableApi.value().length!=0) {
                    basePackages.addAll(Arrays.asList(enableApi.value()));
                }
            }
        }

        scanner.scan((String[])basePackages.toArray(new String[0]));
    }

    private List<Class<?>> getAnnotations(AnnotationMetadata annotationMetadata) {
        try {

            Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
            List<Class<?>> classList = new ArrayList<>();
            for (String annotationType : annotationTypes) {
                classList.add(Class.forName(annotationType));
            }
            return classList;

        }catch (ClassNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader=resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }
}
