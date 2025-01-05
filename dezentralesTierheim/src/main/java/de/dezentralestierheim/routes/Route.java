package de.dezentralestierheim.routes;

import de.dezentralestierheim.dto.MailDto;
import de.dezentralestierheim.jpa.Tier;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;

@ApplicationScoped
public class Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        JacksonDataFormat jsonFormat = new JacksonDataFormat(Tier.class);

        from("activemq:queue:pflegestelle-benachrichtigen")
                .unmarshal(jsonFormat)
                .process(exchange -> {
                    Tier tier = exchange.getMessage().getBody(Tier.class);
                    MailDto mail = new MailDto();
                    mail.setFrom("dezentralestierheim@gmail.com");
                    mail.setTo("dezentralestierheim@gmail.com");
                    mail.setSubject("Benachrichtigen");
                    String msg = "Guten Tag, ";
                    msg += tier.getName();
                    mail.setBody(msg);
                    exchange.getMessage().setBody(mail);
                })
                .marshal().jacksonXml(MailDto.class)
                .to("file:messages/out?fileName=test.xml&noop=true");
    }
}
