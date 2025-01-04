package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Entity
public class Pflegestelle extends PanacheEntity {
    @Enumerated(EnumType.STRING)
    private Tier.Tierart tierart;

    private String name;

    private String email;

    private String adresse;

    private boolean nurGesund; //Pflegestelle nimmt nur gesunde Tiere auf

    private LocalDate zuletztBelegtAm; //Datum, an dem Pflegestelle zuletzt ein Tier bekommen hat (wenn mindestens ein Tier, dann Datum = 9999-12-31)

    private int kapazitaet; //So viele Tiere sind aktuell auf Pflegestelle

    private int maxKapazitaet; //So viele Tiere möchte Pflegestelle maximal aufnehmen


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

    public LocalDate getZuletztBelegtAm() {
        return zuletztBelegtAm;
    }

    public void setZuletztBelegtAm(LocalDate zuletztBelegtAm) {
        this.zuletztBelegtAm = zuletztBelegtAm;
    }

    public Tier.Tierart getTierart() {
        return tierart;
    }

    public void setTierart(Tier.Tierart tierart) {
        this.tierart = tierart;
    }

    public int getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public int getMaxKapazitaet() {
        return maxKapazitaet;
    }

    public void setMaxKapazitaet(int maxKapazitaet) {
        this.maxKapazitaet = maxKapazitaet;
    }
}
