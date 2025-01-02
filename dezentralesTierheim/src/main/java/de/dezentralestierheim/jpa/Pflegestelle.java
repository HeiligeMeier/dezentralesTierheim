package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
public class Pflegestelle extends PanacheEntity {

    public enum Tierart{
        HUND,
        KATZE
    }

    @Enumerated(EnumType.STRING)
    private Tierart tierart;
    private String name;
    private String email;
    private String adresse;
    private boolean nurGesund;
    private LocalDate freiSeit;


    public Long getId() {
        return id;
    }

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

    public Tierart getTierart() {
        return tierart;
    }

    public void setTierart(Tierart tierart) {
        this.tierart = tierart;
    }
}
