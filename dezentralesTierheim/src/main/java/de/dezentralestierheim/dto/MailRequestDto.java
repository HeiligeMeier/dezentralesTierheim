package de.dezentralestierheim.dto;

import de.dezentralestierheim.jpa.Interessent;
import de.dezentralestierheim.jpa.Pflegestelle;
import de.dezentralestierheim.jpa.Tier;

// Raluca
public class MailRequestDto {
    private Pflegestelle pflegestelle;
    private Tier tier;
    private Interessent interessent;

    private String messageType;

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

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
