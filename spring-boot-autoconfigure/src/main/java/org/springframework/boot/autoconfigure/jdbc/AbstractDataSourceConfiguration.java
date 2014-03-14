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

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Base class for configuration of a database pool.
 *
 * @author Dave Syer
 */
@ConfigurationProperties(name = DataSourceAutoConfiguration.CONFIGURATION_PREFIX)
@EnableConfigurationProperties
public abstract class AbstractDataSourceConfiguration implements BeanClassLoaderAware,
		InitializingBean {

	private ClassLoader classLoader;

	protected EmbeddedDatabaseConnection embeddedDatabaseConnection = EmbeddedDatabaseConnection.NONE;

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.embeddedDatabaseConnection = EmbeddedDatabaseConnection.get(this.classLoader);
		String driverClassName = resolveDriverClassName(getDriverClassName());
		if (!StringUtils.hasText(driverClassName)) {
			throw new BeanCreationException(
					"Cannot determine embedded database driver class for database type "
							+ this.embeddedDatabaseConnection
							+ ". If you want an embedded "
							+ "database please put a supported one on the classpath.");
		}
		if (!driverClassName.equals(getDriverClassName())) {
			setDriverClassName(driverClassName);
		}
		String url = resolveUrl(getUrl());
		if (!StringUtils.hasText(url)) {
			throw new BeanCreationException(
					"Cannot determine embedded database url for database type "
							+ this.embeddedDatabaseConnection
							+ ". If you want an embedded "
							+ "database please put a supported one on the classpath.");
		}
		if (!url.equals(getUrl())) {
			setUrl(url);
		}
		if (EmbeddedDatabaseConnection.isEmbedded(driverClassName)) {
			setUsername("sa");
			setPassword("");
		}

	}

	protected abstract void setPassword(String password);
	protected abstract void setUsername(String sa);
	protected abstract void setUrl(String url);
	protected abstract String getUrl();
	protected abstract void setDriverClassName(String driverClassName);
	protected abstract String getDriverClassName();

	protected String resolveDriverClassName(String driverClassName) {
		if (StringUtils.hasText(driverClassName)) {
			return driverClassName;
		}
		return this.embeddedDatabaseConnection.getDriverClassName();
	}

	protected String resolveUrl(String url) {
		if (StringUtils.hasText(url)) {
			return url;
		}
		return this.embeddedDatabaseConnection.getUrl();
	}
}
