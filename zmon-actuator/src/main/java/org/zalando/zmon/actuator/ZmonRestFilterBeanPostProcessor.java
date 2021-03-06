/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.zmon.actuator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.client.RestTemplate;

public class ZmonRestFilterBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private static final Log logger = LogFactory.getLog(ZmonRestFilterBeanPostProcessor.class);

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName)
            throws BeansException {

        if (bean instanceof RestTemplate) {

            final RestTemplate restTemplateBean = (RestTemplate) bean;

            restTemplateBean.getInterceptors().add(beanFactory.getBean(ZmonRestResponseBackendMetricsInterceptor.class));
            logger.info("Added " + ZmonRestFilterBeanPostProcessor.class.getCanonicalName() + " instance to "
                    + beanName);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object o, final String s) throws BeansException {
        return o;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
