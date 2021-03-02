package adressprojekt;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;

import java.util.List;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.dbcp.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JdbcTest extends CamelTestSupport {

	@Test
	public void jdbcTimerTest() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:result");
		String url = "jdbc:mysql://localhost:3306/projects";
		DataSource dataSource = setupDataSource(url);

		DefaultRegistry reg = new DefaultRegistry();
		reg.bind("myDataSource", dataSource);

		CamelContext context = new DefaultCamelContext(reg);
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("timer://foo?period=2000").setBody(constant("select * from person;")).to("jdbc:myDataSource")
				.process(new Processor() {

					public void process(Exchange exchange) throws Exception {
						
					}
					
				})
						.to("log:output");
			}
		});
		context.start();
		Thread.sleep(5000);
		
		assertMockEndpointsSatisfied();
		System.out.println(mock.getExchanges().size());
//		assertEquals(1, line.get(0));
//		assertEquals("erik", line.get(1));
//		assertEquals("persson", line.get(2));
		context.stop();
		context.close();
	}

	private static DataSource setupDataSource(String connectURI) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("Mysecretpw!1");
		ds.setUrl(connectURI);
		return ds;
	}
}
