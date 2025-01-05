package de.dezentralestierheim.routes;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.dezentralestierheim.dto.MailDto;
import de.dezentralestierheim.dto.MailRequestDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

@ApplicationScoped
public class Route extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        JacksonDataFormat jsonFormat = new JacksonDataFormat(MailRequestDto.class);
        jsonFormat.addModule(new JavaTimeModule());

        // Nachrichten an die Pflegestelle
        from("activemq:queue:pflegestelle-benachrichtigen")
                .unmarshal(jsonFormat)
                .process(exchange -> {
                    MailRequestDto mailRequestDto = exchange.getMessage().getBody(MailRequestDto.class);

                    // E-Mail erstellen
                    MailDto mail = new MailDto();
                    mail.setFrom("tierheim@gmail.com");
                    mail.setTo(mailRequestDto.getPflegestelle().getEmail());

                    mail.setSubject(mailRequestDto.getSubject());
                    String msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", ";

                    msg += mailRequestDto.getBody();
                    mail.setBody(msg);
                    exchange.getMessage().setBody(mail);

                    String fileName = mailRequestDto.getPflegestelle().getName() + "-" + mailRequestDto.getSubject() + ".xml";
                    exchange.getIn().setHeader(Exchange.FILE_NAME, fileName);
                })
                .marshal().jacksonXml(MailDto.class)
                .to("file:messages/pflegestelle?noop=true");

        // Nachrichten an dem Interessenten
        from("activemq:queue:interessent-benachrichtigen")
                .unmarshal(jsonFormat)
                .process(exchange -> {
                    MailRequestDto mailRequestDto = exchange.getMessage().getBody(MailRequestDto.class);

                    // E-Mail erstellen
                    MailDto mail = new MailDto();
                    mail.setFrom("tierheim@gmail.com");
                    mail.setTo(mailRequestDto.getInteressent().getEmail());
                    mail.setSubject(mailRequestDto.getSubject());

                    String msg = "Guten Tag " + mailRequestDto.getInteressent().getName() + ", ";

                    msg += mailRequestDto.getBody();
                    mail.setBody(msg);
                    exchange.getMessage().setBody(mail);

                    String fileName = mailRequestDto.getInteressent().getName() + "-" + mailRequestDto.getSubject() + ".xml";
                    exchange.getIn().setHeader(Exchange.FILE_NAME, fileName);
                })
                .marshal().jacksonXml(MailDto.class)
                .to("file:messages/interessent?noop=true");

        // Nachrichten an dem Tierbesitzer
        from("activemq:queue:tierbesitzer-benachrichtigen")
                .unmarshal(jsonFormat)
                .process(exchange -> {
                    MailRequestDto mailRequestDto = exchange.getMessage().getBody(MailRequestDto.class);

                    // E-Mail erstellen
                    MailDto mail = new MailDto();
                    mail.setFrom("tierheim@gmail.com");
                    mail.setTo(mailRequestDto.getTier().getTierBesitzerEmail());
                    mail.setSubject(mailRequestDto.getSubject());

                    String msg = "Guten Tag, ";

                    msg += mailRequestDto.getBody();
                    mail.setBody(msg);
                    exchange.getMessage().setBody(mail);

                    String fileName = mailRequestDto.getTier().getTierBesitzerEmail() + "-" + mailRequestDto.getSubject() + ".xml";
                    exchange.getIn().setHeader(Exchange.FILE_NAME, fileName);
                })
                .marshal().jacksonXml(MailDto.class)
                .to("file:messages/tierbesitzer?noop=true");
    }
}
