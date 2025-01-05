package de.dezentralestierheim.dto;

import de.dezentralestierheim.jpa.Interessent;
import de.dezentralestierheim.jpa.Pflegestelle;
import de.dezentralestierheim.jpa.Tier;

public class MailRequestDto {
    private Pflegestelle pflegestelle;
    private Tier tier;
    private Interessent interessent;

    private String subject;
    private String body;

    public Pflegestelle getPflegestelle() {
        return pflegestelle;
    }

    public void setPflegestelle(Pflegestelle pflegestelle) {
        this.pflegestelle = pflegestelle;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public Interessent getInteressent() {
        return interessent;
    }

    public void setInteressent(Interessent interessent) {
        this.interessent = interessent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
