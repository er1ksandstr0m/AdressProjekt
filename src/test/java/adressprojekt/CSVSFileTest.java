package adressprojekt;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;

import org.junit.Test;


public class CSVSFileTest extends CamelTestSupport {

	public static final String inbox = "file://src/test/resources/data/inbox?noop=true&fileName=addresses.csv";
	public static final String inbox2 = "file://src/test/resources/data/inbox2?noop=true&fileName=addresses.csv";
	public static final String outbox = "file://src/test/resources/data/outbox";

	@Test
	public void testMock() throws Exception {
//        getMockEndpoint("mock:result").expectedBodiesReceived("Hello World");
		MockEndpoint mock = getMockEndpoint("mock:result");

		mock.expectedMessageCount(2);

		assertMockEndpointsSatisfied();

		@SuppressWarnings("rawtypes")
		List line = mock.getExchanges().get(0).getIn().getBody(List.class);
		assertEquals("Emil", line.get(0));
		assertEquals("Rydberg", line.get(1));
		assertEquals("990311", line.get(2));
		assertEquals("Bisittarevagen 20", line.get(3));
		assertEquals("43345", line.get(4));
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(inbox).unmarshal().csv().split(body()).log("${body[0]}").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						Object body = exchange.getIn().getBody();
						if (body instanceof ArrayList) {
							@SuppressWarnings("rawtypes")
							ArrayList a = (ArrayList) body;
							System.out.println("test: " + a.get(0));
						} else {
							System.out.println("test: " + body);
						}
					}
				}).to("mock:result");
			}
		};
	}

}

//				from("mock:queue.csv")
//				.setHeader("username", constant("root"))
//				.setHeader("password", constant("Mysecretpw!1"))
//				.transform()
//				.simple("BEGIN;"
//						+ "INSERT INTO person VALUES('$body[Name]', '$body[Surname]', '$body[PersonNr]');"
//						+ "INSERT INTO address VALUES('$body[StAddress]', '$body[PostNr]');"
//						+ "COMMIT;")
//				.to("jdbc:mysql://localhost:3306/mysql?useHeadersAsParameters=true");
