package adressprojekt.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class MessageRouteTest extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("file:src/test/resources/inbox?noop=true")//.choice()
//		.when(header("CamelFileName").endsWith(".csv"))
		.to("jms:queue:incomingAddress");
//		.when(header("CamelFileName").endsWith(".json"))
//		.to("jms:queue:incomingRequest");
		
		from("jms:queue:incomingAddress")
		.log("receiving message with body: ${body}")
		.to("mock:output");
		//from("jms:queue:incomingAddress").to("jms:queue:addresses")
		
		//from("jms:addresses").to("mock:output");
		//from("mock:midpoint").to("mock:endpoint");
//		from("jms:queue:addresses")
//		.to("log:DEBUG?showBody=true&showHeaders=true");
//		.log("this is the tokenized csv body: ${body}")
//		.setHeader("username", constant("root"))
//		.setHeader("password", constant("Mysecretpw!1"))
//		.split(body().tokenize("\n")).streaming()
//		.unmarshal().csv()
//		.transform()
//		.simple("BEGIN;"
//				+ "INSERT INTO person VALUES('$body[Name]', '$body[Surname]', '$body[PersonNr]');"
//				+ "INSERT INTO address VALUES('$body[StAddress]', '$body[PostNr]');"
//				+ "COMMIT;")
//		.to("jdbc:mysql://localhost:3306/mysql?useHeadersAsParameters=true");

	}

}
