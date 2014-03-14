/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.jdbc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Configuration for a Tomcat database pool. The Tomcat pool provides superior performance
 * and tends not to deadlock in high volume environments.
 *
 * @author Dave Syer
 * @see DataSourceAutoConfiguration
 */
@Configuration
public class TomcatDataSourceConfiguration extends AbstractDataSourceConfiguration
		implements ConfigurationProxy, InitializingBean {

	private org.apache.tomcat.jdbc.pool.DataSource pool = new org.apache.tomcat.jdbc.pool.DataSource();

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		return this.pool;
	}

	@Override
	public Object getTargetConfigurationBean() {
		return this.pool.getPoolProperties();
	}

	@Override
	protected void setPassword(String password) {
		this.pool.setPassword(password);
	}

	@Override
	protected void setUsername(String sa) {
		this.pool.setUsername(sa);
	}

	@Override
	protected void setUrl(String url) {
		this.pool.setUrl(url);
	}

	@Override
	protected String getUrl() {
		return this.pool.getUrl();
	}

	@Override
	protected void setDriverClassName(String driverClassName) {
		this.pool.setDriverClassName(driverClassName);
	}

	@Override
	protected String getDriverClassName() {
		return this.pool.getDriverClassName();
	}
}
