package adressprojekt;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcTest extends CamelTestSupport {

	@Override
	protected CamelContext createCamelContext() throws Exception {
		CamelContext camelContext = super.createCamelContext();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		camelContext.addComponent("jms", jmsComponentClientAcknowledge(connectionFactory));

		return camelContext;
	}

	private static DataSource setupDataSource(String connectURI) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		ds.setUsername("root");
		ds.setPassword("Mysecretpw!1");
		ds.setUrl(connectURI);
		return ds;
	}
	
	@Test
	public void jdbcTimerTest() throws Exception {
		String connectURI = "jdbc:derby:memory:order;create=true";
		DataSource datasource = setupDataSource(connectURI);
		
		DefaultRegistry reg = new DefaultRegistry();
		reg.bind("myDataSource", null, datasource);
		context.start();
		Thread.sleep(5000);
		context.stop();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("timer://foo?period=2000").process(new Processor() {

					public void process(Exchange exchange) throws Exception {

					}
				})
				.to("jdbc:myDataSource").to("log:output");
			}
		};
	}
}
