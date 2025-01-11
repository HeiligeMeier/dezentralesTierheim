package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

// Stefan
@Entity
public class Inserat extends PanacheEntity {

    private String bild;

    private String inseratText;

    private boolean istAktiv;

    private Long tierId;

    public Long getId() {
        return id;
    }

    public String getBild() {
        return bild;
    }

    public void setBild(String bild) {
        this.bild = bild;
    }

    public String getInseratText() {
        return inseratText;
    }

    public void setInseratText(String inseratText) {
        this.inseratText = inseratText;
    }

    public boolean isIstAktiv() {
        return istAktiv;
    }

    public void setIstAktiv(boolean istAktiv) {
        this.istAktiv = istAktiv;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

}
