package adressprojekt;


import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import adressprojekt.route.MessageRouteTest;

public class ActiveMQTest extends CamelTestSupport {

	static final String inbox = "C:\\Users\\Erik\\Camel\\adressprojekt\\src\\test\\resources\\data\\inbox";
	static final String outbox = "C:\\Users\\Erik\\Camel\\adressprojekt\\src\\test\\resources\\data\\outbox";
	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQTest.class);

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new MessageRouteTest();
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {

		CamelContext camelContext = super.createCamelContext();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
		camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		return camelContext;
	}

	@Test
	public void testReadAddress() throws Exception {
		getMockEndpoint("mock:output").expectedMessageCount(1);
		LOGGER.info("Checking if message was received");
		assertMockEndpointsSatisfied();
	}

}
