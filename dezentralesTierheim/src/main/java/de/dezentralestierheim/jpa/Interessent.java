package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

// Melanie
@Entity
public class Interessent extends PanacheEntity {

    public enum Wohnlage {
        SEHR_RUHIG,//("Ländliche Gegend, keine Hauptstraße im Umkreis von 2km"),
        RUHIG,//("Ländliche Gegend, keine Hauptstraße im Umkreis von 1km")
        NORMAL,//("Größeres Dorf, wenig Verkehr, keine Hauptstrasse in unmittelbarer Nähe")
        VERKEHRSNAH,//("Größeres Dorf oder Stadt, wenig bis mittlerer Verkehr. Hauptstrasse ist in der Nähe")
        STARKER_VERKEHR//("Stadt, viel Verkehr oder Hauptstraße direkt am Wohnort.")
    }

    public enum OtherAnimals {
        HUND,
        KATZE,
        NAGETIERE,
        SONSTIGES
    }

    private String name;

    private String email;

    private LocalDate geburtsdatum;

    @Enumerated(EnumType.STRING)
    private Wohnlage wohnlage;

    private Boolean gartenOderBalkon;
    private int quadratmeter;
    private Boolean hatKinder;

    @Enumerated(EnumType.STRING)
    private OtherAnimals andereTiere;

    private Long interessiertAnTierID;

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

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public Boolean getGartenOderBalkon() {
        return gartenOderBalkon;
    }

    public void setGartenOderBalkon(Boolean gartenOderBalkon) {
        this.gartenOderBalkon = gartenOderBalkon;
    }

    public int getQuadratmeter() {
        return quadratmeter;
    }

    public void setQuadratmeter(int quadratmeter) {
        this.quadratmeter = quadratmeter;
    }

    public Boolean getHatKinder() {
        return hatKinder;
    }

    public void setHatKinder(Boolean hatKinder) {
        this.hatKinder = hatKinder;
    }
    
    public OtherAnimals getAndereTiere() {
        return andereTiere;
    }

    public void setAndereTiere(OtherAnimals andereTiere) {
        this.andereTiere = andereTiere;
    }

    public Wohnlage getWohnlage() {
        return wohnlage;
    }

    public void setWohnlage(Wohnlage wohnlage) {
        this.wohnlage = wohnlage;
    }

    public Long getInteressiertAnTierID() {
        return interessiertAnTierID;
    }

    public void setInteressiertAnTierID(Long interessiertAnTierID) {
        this.interessiertAnTierID = interessiertAnTierID;
    }

}