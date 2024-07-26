package kr.co.epicit._supports.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class FullBeanNameGenerator implements org.springframework.beans.factory.support.BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		return definition.getBeanClassName();
	}
}
/*
 * spring annotation-driven 시 bean name 중복(충돌,conflicts) 해결
 *  - https://itnp.kr/post/spring-annotation-driven-bean-name-conflicts
 *  - <context:component-scan base-package="com.samples.model" name-generator="partners.inspire._supports.spring.FullBeanNameGenerator"/>
 */

