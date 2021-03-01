package adressprojekt.app;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class AdressProjekt {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
		context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("file:src/main/resources/data/inboxtwo?noop=true")
				.choice()
				.when(header("CamelFileName").endsWith(".csv"))
				.unmarshal().csv()
				.split(body())
				.to("jms:queue:incomingAddress");
				
				from("jms:queue:incomingAddress")
				.marshal().csv()
				.to("file:src/main/resources/data/outbox");
			}
		});

		context.start();
		Thread.sleep(10000);

		// stop the CamelContext
		context.stop();
	}

}
