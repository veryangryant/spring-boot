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

import java.sql.SQLException;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.StringUtils;

/**
 * Configuration for a Commons DBCP database pool. The DBCP pool is popular but not
 * recommended in high volume environments (the Tomcat DataSource is more reliable).
 *
 * @author Dave Syer
 * @see DataSourceAutoConfiguration
 */
@Configuration
public class CommonsDataSourceConfiguration extends AbstractDataSourceConfiguration implements ConfigurationProxy{

	private static Log logger = LogFactory.getLog(CommonsDataSourceConfiguration.class);

	private BasicDataSource pool = new BasicDataSource();

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		logger.info("Hint: using Commons DBCP BasicDataSource. It's going to work, "
				+ "but the Tomcat DataSource is more reliable.");
		return this.pool;
	}

	@Override
	public Object getTargetConfigurationBean() {
		return this.pool;
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
