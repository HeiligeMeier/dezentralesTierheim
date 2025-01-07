package de.dezentralestierheim.routes;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.dezentralestierheim.dto.MailDto;
import de.dezentralestierheim.dto.MailRequestDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;

@ApplicationScoped
public class Route extends RouteBuilder {

    private MailDto setMailByMessageType(MailRequestDto mailRequestDto) {
        MailDto mail = new MailDto();
        String msg;
        mail.setFrom("tierheim@gmail.com");

        switch (mailRequestDto.getMessageType()) {
            case "AnfrageTieraufnahme":
                mail.setSubject("Anfrage Tieraufnahme");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n " +
                        "unser Tierschutzverein hat eine Anfrage zur Aufnahme eines Tieres erhalten. \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Tierbesitzer-Kontakt: " + mailRequestDto.getTier().getTierBesitzerEmail() + "\n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob Sie das Tier aufnehmen können. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "ErinnerungAnfrageTieraufnahme":
                mail.setSubject("Erinnerung: Anfrage Tieraufnahme");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n" +
                        "dies ist eine Erinnerung an die zuvor gesendete Anfrage zur Aufnahme eines Tieres.  \n" +
                        "Anschließend wird die Anfrage wiedergegeben.  \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Tierbesitzer-Kontakt: " + mailRequestDto.getTier().getTierBesitzerEmail() + "\n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob Sie das Tier aufnehmen können. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "NachfrageBereitschaftAufnahme":
                mail.setSubject("Nachfrage zur Bereitschaft zur Tieraufnahme aufgrund von nicht-Rückmeldung");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n" +
                        "unser Tierschutzverein hat eine Anfrage zur Aufnahme eines Tieres erhalten. \n" +
                        "Sie wurden von unserem System aufgrund Ihrer Angaben als potentielle Pflegestelle ermittelt \n" +
                        "Jedoch haben Sie sich nicht zurückgemeldet. Aus diesem Grund hat unser System Sie vorübergehend als nicht aufnahmebereit abgespeichert. \n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob dies der Fall ist, oder ob Sie weiterhin bereit sind Tiere aufzunehmen. \n" +
                        "Wir bedanken uns für Ihr Engagement. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "PflegestelleInteressentInformieren":

                mail.setSubject("Anfrage zur Tieraufnahme");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getInteressent() + ", \n \n" +
                        "unser Tierschutzverein hat eine Anfrage zur Aufnahme eines Tieres erhalten. \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob Sie das Tier aufnehmen können. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "TierbesitzerInformieren":
                mail.setSubject("Ablehnung der Tieraufnahme");

                msg = "Guten Tag, \n \n" +
                        "wir bedauern es sehr, Ihnen mitteilen zu müssen, dass wir Ihr Tier zurzeit leider nicht aufnehmen können. \n" +
                        "Derzeit sind unsere Kapazitäten erschöpft und es ist uns nicht möglich, weitere Tiere unterzubringen. \n" +
                        "Bitte wenden Sie sich an eines unserer Nachbartierheime: TSV Beispielhausen e.V. oder Tierheim Wunschkirchen e.V.. \n" +
                        "Wir bitten um Ihr Verständnis. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "Rueckzieher":
                mail.setSubject("Tieraufnahme abgebrochen");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n" +
                        "leider müssen wir Ihnen mitteilen, dass wir das Tier" + mailRequestDto.getTier().getName() + " nicht aufnehmen werden, da der Tierbesitzer die Abgabe abgebrochen hat. \n" +
                        "Wir bitten um Ihr Verständnis und bitten von weiteren Nachfragen abzusehen. \n" +
                        "Sie sind von unserem System weiterhin priorisiert und werden daher bei der nächsten Tieraufnahme, die zu Ihren Anforderungen passt, kontaktiert. \n" +
                        "Wir bedanken uns für Ihr Engagement. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            case "AbsageAdoptionsanfrage":
                mail.setSubject("");

                msg = "Guten Tag " + mailRequestDto.getInteressent().getName() + ", \n \n" +
                        "leider müssen wir Ihnen mitteilen, dass das Tier " + mailRequestDto.getInteressent().getInteressiertAnTierID() + " bereits adoptiert wurde. \n" +
                        "Schauen Sie sich gerne unsere offenen Inserate an und teilen Sie uns mit, ob Sie ein Tier finden, um das Sie sich kümmern möchten. \n" +
                        "Falls kein Tier zu Ihren Anforderungen passt, können Sie zu einem späteren Zeitpunkt erneut unsere Inserate überprüfen. \n" +
                        "Sie können ebenfalls unseren Newsletter abonnieren, um über neue Tieraufnahmen, informiert zu werden. \n" +
                        "Wir bitten um Ihr Verständnis. \n \n" +
                        "Ihr Tierschutzverein Musterstadt";
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

        from("file:messages/pflegestelle").unmarshal().jacksonXml(MailDto.class).marshal().json().to("activemq:queue:backend");

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

        from("file:messages/interessent").unmarshal().jacksonXml(MailDto.class).marshal().json().to("activemq:queue:backend");

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

        from("file:messages/tierbesitzer").unmarshal().jacksonXml(MailDto.class).marshal().json().to("activemq:queue:backend");

    }
}
