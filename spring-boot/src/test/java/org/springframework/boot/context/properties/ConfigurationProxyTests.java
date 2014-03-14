package org.springframework.boot.context.properties;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.Assert.assertEquals;

/**
 * Created by pmaj on 3/13/14.
 */
public class ConfigurationProxyTests {

	private AnnotationConfigApplicationContext context;

	@Test
	public void testSettingPropertiesOnProxy() {
		MockEnvironment env = new MockEnvironment();
		env.setProperty("name", "test");
		this.context = new AnnotationConfigApplicationContext();
		this.context.setEnvironment(env);
		this.context.register(ParentBean.class);
		this.context.refresh();
		ParentBean parentBean = this.context.getBean(ParentBean.class);
		assertEquals("test", ((TargetBean) parentBean.getTargetConfigurationBean()).getName());
	}

	@ConfigurationProperties
	@EnableConfigurationProperties
	protected static class ParentBean implements ConfigurationProxy {

		private TargetBean bean = new TargetBean();

		@Override
		public Object getTargetConfigurationBean() {
			return bean;
		}
	}

	public static class TargetBean {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
