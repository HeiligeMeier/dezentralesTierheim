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

    private MailDto setMailByMessageType(MailRequestDto mailRequestDto) {
        MailDto mail = new MailDto();
        String msg;
        mail.setFrom("tierheim@gmail.com");

        switch (mailRequestDto.getMessageType()) {
            case "AnfrageTieraufnahme":
                mail.setSubject("Anfrage Tieraufnahme");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n " +
                        "Unser Tierschutzverein hat eine Anfrage zur Aufnahme eines Tieres erhalten. \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Tierbesitzer-Kontakt: " + mailRequestDto.getTier().getTierBesitzerEmail() + "\n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob Sie das Tier aufnehmen können. \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "ErinnerungAnfrageTieraufnahme":
                mail.setSubject("Erinnerung: Anfrage Tieraufnahme");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n " +
                        "Unser Tierschutzverein hat eine Anfrage zur Aufnahme eines Tieres erhalten. \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Tierbesitzer-Kontakt: " + mailRequestDto.getTier().getTierBesitzerEmail() + "\n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob Sie das Tier aufnehmen können. \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "NachfrageBereitschaftAufnahme":
                mail.setSubject("Nachfrage zur Bereitschaft zur Tieraufnahme aufgrund von nicht-Rückmeldung");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n " +
                        "Unser Tierschutzverein hat eine Anfrage zur Aufnahme eines Tieres erhalten. \n" +
                        "Sie haben sich jedoch nicht zurückgemeldet. Aus diesem Grund hat unser System Sie vorübergehend als nicht aufnahmebereit abgespeichert. \n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob dies der Fall ist, oder ob Sie weiterhin bereit sind Tiere aufzunehmen. \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "TierbesitzerInformieren":
                mail.setSubject("Ablehnung der Tieraufnahme");

                msg = "Guten Tag, ";

                msg += "leider müssen wir Ihnen mitteilen, dass wir Ihr Tier nicht aufnehmen können, da wir aktuell keine freien Kapazitäten haben. \n" +
                        "Bitte wenden Sie sich an eines unserer Nachbartierheime: TSV Beispielhausen e.V. oder Tierheim Wunschkirchen e.V.. \n" +
                        "Wir bitten um Ihr Verständnis. \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                mail.setBody(msg);
                break;
            case "AntwortInteressent":
                mail.setSubject("");

                msg = "Guten Tag " + mailRequestDto.getInteressent().getName() + ", ";

                msg += "";
                mail.setBody(msg);
                break;
            default:
                msg = "";
        }

        mail.setBody(msg);

        return mail;
    }

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
                    MailDto mail = setMailByMessageType(mailRequestDto);
                    mail.setTo(mailRequestDto.getPflegestelle().getEmail());

                    exchange.getMessage().setBody(mail);

                    String fileName = mailRequestDto.getPflegestelle().getName() + "-" + mailRequestDto.getMessageType() + ".xml";
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
                    MailDto mail = setMailByMessageType(mailRequestDto);
                    mail.setTo(mailRequestDto.getInteressent().getEmail());
                    exchange.getMessage().setBody(mail);

                    String fileName = mailRequestDto.getInteressent().getName() + "-" + mailRequestDto.getMessageType() + ".xml";
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
                    MailDto mail = setMailByMessageType(mailRequestDto);
                    mail.setTo(mailRequestDto.getTier().getTierBesitzerEmail());
                    exchange.getMessage().setBody(mail);

                    String fileName = mailRequestDto.getTier().getTierBesitzerEmail() + "-" + mailRequestDto.getMessageType() + ".xml";
                    exchange.getIn().setHeader(Exchange.FILE_NAME, fileName);
                })
                .marshal().jacksonXml(MailDto.class)
                .to("file:messages/tierbesitzer?noop=true");
    }
}
