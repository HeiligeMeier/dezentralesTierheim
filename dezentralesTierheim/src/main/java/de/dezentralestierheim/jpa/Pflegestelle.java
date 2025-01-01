package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Pflegestelle extends PanacheEntity {
    private String name;
    private String email;
    private String[] tierart;
    private String adresse;
    private boolean nurGesund;
    private LocalDate freiSeit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getTierart() {
        return tierart;
    }

    public void setTierart(String[] tierart) {
        this.tierart = tierart;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isNurGesund() {
        return nurGesund;
    }

    public void setNurGesund(boolean nurGesund) {
        this.nurGesund = nurGesund;
    }

    public LocalDate getFreiSeit() {
        return freiSeit;
    }

    public void setFreiSeit(LocalDate freiSeit) {
        this.freiSeit = freiSeit;
    }
}
