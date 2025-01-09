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
            // Aufnahmeprozess: Pflegestelle über Tieraufnahme benachrichtigen
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
            // Aufnahmeprozess: Erinnerung an Pflegestelle senden
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
            // Aufnahmeprozess: Bei Pflegestelle Aufnahmebreitschaft für Zukunft abfragen
            case "NachfrageBereitschaftAufnahme":
                mail.setSubject("Nachfrage der Bereitschaft zur Tieraufnahme aufgrund von nicht-Rückmeldung");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n" +
                        "unser Tierschutzverein hat eine Anfrage zur Aufnahme eines Tieres erhalten. \n" +
                        "Sie wurden von unserem System aufgrund Ihrer Angaben als potentielle Pflegestelle ermittelt. \n" +
                        "Jedoch haben Sie sich nicht zurückgemeldet. Aus diesem Grund hat unser System Sie vorübergehend als nicht aufnahmebereit abgespeichert. \n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob dies der Fall ist, oder ob Sie weiterhin bereit sind Tiere aufzunehmen. \n" +
                        "Wir bedanken uns für Ihr Engagement. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            // Adoptionsprozess: Pflegestelle über Interessenten informieren
            case "PflegestelleInteressentInformieren":

                mail.setSubject("Interessent gefunden");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n" +
                        "unser Tierschutzverein hat eine Anfrage zur Aufnahme Ihres Tieres erhalten. \n" +
                        "Der Interessent " + mailRequestDto.getInteressent().getName() + "hat eine Adoptionsanfrage für das folgende Tier gestellt. \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Sie können den Interessenten unter folgender E-Mail erreichen " + mailRequestDto.getInteressent().getEmail() + "\n" +
                        "Bitte geben Sie uns baldmöglichst Bescheid, ob das Tier für die Adoption bereit ist. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            // Adoptionsprozess: Erinnerung senden (1)
            case "FeedbackErinnerung":

                mail.setSubject("Erinnerung: Feedback zu Interessenten benötigt");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n" +
                        "wie Ihnen bereits mitgeteilt wurde, hat unser Tierschutzverein eine Anfrage zur Aufnahme eines Ihrer Tieres erhalten. \n" +
                        "Um den Abwicklung des Adoptionsprozesses durchführen zu können benötigen wir Ihr Feedback bezüglich des Interessenten. \n" +
                        "Aus diesem Grund würden wir uns erkenntlich zeigen, wenn Sie uns dieses baldmöglichst mitteilen. \n" +
                        "Anschließend werden der Interessent und das Tier wiedergegeben. \n" +
                        "Interessent: " + mailRequestDto.getInteressent().getName() + " \n" +
                        "Kontakt: " + mailRequestDto.getInteressent().getEmail() + " \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Mit freundlichen Grüßen \n \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            // Adoptionsprozess: Erinnerung senden (2)
            case "AdoptionsvertragErinnerung":

                mail.setSubject("Erinnerung: Adoptionsvertrag benötigt");

                msg = "Guten Tag Pflegestelle " + mailRequestDto.getPflegestelle().getName() + ", \n \n" +
                        "wie Ihnen bereits mitgeteilt wurde, hat unser Tierschutzverein eine Anfrage zur Aufnahme eines Ihrer Tiere erhalten. \n" +
                        "Um die Abwicklung des Adoptionsprozesses durchführen zu können benötigen wir den von Ihnen ausgefüllten Adoptionsvertrag. \n" +
                        "Aus diesem Grund würden wir uns erkenntlich zeigen, wenn Sie uns dieses baldmöglichst senden. \n" +
                        "Anschließend werden der Interessent und das Tier wiedergegeben. \n" +
                        "Interessent: " + mailRequestDto.getInteressent().getName() + " \n" +
                        "Kontakt: " + mailRequestDto.getInteressent().getEmail() + " \n" +
                        "Tierart: " + mailRequestDto.getTier().getTierart() + "\n" +
                        "Name: " + mailRequestDto.getTier().getName() + "\n" +
                        "Geburtstag: " + mailRequestDto.getTier().getGeburtsdatum() + "\n" +
                        "Mit freundlichen Grüßen \n \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            // Aufnahmeprozess: Tierbesitzer informieren
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
            // Aufnahmeprozess: Pflegestelle über Rückzieher informieren
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
            // Adoptionsprozess: Nachricht, dass Tier bereits adoptiert wurde, senden
            // Adoptionsprozess: Tier nicht mehr verfügbar Nachricht senden
            case "AbsageAdoptionsanfrage":
                mail.setSubject("Absage der Adoptionsanfrage");

                msg = "Guten Tag " + mailRequestDto.getInteressent().getName() + ", \n \n" +
                        "leider müssen wir Ihnen mitteilen, dass das Tier " + mailRequestDto.getInteressent().getInteressiertAnTierID() + " bereits adoptiert wurde. \n" +
                        "Schauen Sie sich gerne unsere offenen Inserate an und teilen Sie uns mit, ob Sie ein Tier finden, um das Sie sich kümmern möchten. \n" +
                        "Falls kein Tier zu Ihren Anforderungen passt, können Sie zu einem späteren Zeitpunkt erneut unsere Inserate überprüfen. \n" +
                        "Wir bitten um Ihr Verständnis. \n \n" +
                        "Mit freundlichen Grüßen \n" +
                        "Ihr Tierschutzverein Musterstadt";
                break;
            // Adoptionsprozess: Absage zu Adoptionsanfrage senden
            case "AbsageInteressentAdoption":
                mail.setSubject("Absage der Adoptionsanfrage");

                msg = "Guten Tag " + mailRequestDto.getInteressent().getName() + ", \n \n" +
                        "leider müssen wir Ihnen mitteilen, dass wir Ihre Adoptionsanfrage nicht bestätigen können. \n" +
                        "Dies kann unter anderem an der Verkehrslage oder anderen Tieren in einem Haushalt liegen. \n" +
                        "Falls Sie mehr erfahren möchten, setzen Sie sich gerne mit uns in Verbindung. \n" +
                        "Wir bitten um Ihr Verständnis. \n \n" +
                        "Mit freundlichen Grüßen \n" +
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

        // Antwort verfassen und an die Queue schicken
        from("file:messages/pflegestelle")
                .unmarshal().jacksonXml(MailDto.class)
                .filter(simple("${body.subject} == 'Anfrage Tieraufnahme'"))
                .setBody(simple("confirmed"))
                .marshal().json().to("activemq:queue:antwort-tieraufnahme");

        // Antwort an dem Prozess weiterleiten
        from("activemq:queue:antwort-tieraufnahme")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader("Spiffworkflow-Api-Key", simple("61e73cfa-6a39-42ae-8a54-b86d016e6197"))
                .to("http://localhost:8000/v1.0/messages/Rueckmeldung-anfrage");

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
