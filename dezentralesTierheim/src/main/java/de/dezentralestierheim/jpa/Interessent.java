package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
public class Interessent extends PanacheEntity {

    public enum ResidentialLocation{
        SEHR_RUHIG,//("Ländliche Gegend, keine Hauptstraße im Umkreis von 2km"),
        RUHIG,//("Ländliche Gegend, keine Hauptstraße im Umkreis von 1km")
        NORMAL,//("Größeres Dorf, wenig Verkehr, keine Hauptstrasse in unmittelbarer Nähe")
        VERKEHRSNAH,//("Größeres Dorf oder Stadt, wenig bis mittlerer Verkehr. Hauptstrasse ist in der Nähe")
        STARKER_VERKEHR//("Stadt, viel Verkehr oder Hauptstraße direkt am Wohnort.")
    }

    public enum OtherAnimals{
        HUND,
        KATZE,
        NAGETIERE,
        SONSTIGES
    }

    private String name;
    private LocalDate dateOfBirth;
    private ResidentialLocation location;
    private Boolean gardenOrBalcony;
    private int squareMeters;
    private Boolean hasKids;
    private OtherAnimals otherAnimals;

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getGardenOrBalcony() {
        return gardenOrBalcony;
    }

    public void setGardenOrBalcony(Boolean gardenOrBalcony) {
        this.gardenOrBalcony = gardenOrBalcony;
    }

    public int getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(int squareMeters) {
        this.squareMeters = squareMeters;
    }

    public Boolean getHasKids() {
        return hasKids;
    }

    public void setHasKids(Boolean hasKids) {
        this.hasKids = hasKids;
    }

    public ResidentialLocation getLocation() {
        return location;
    }

    public void setLocation(ResidentialLocation location) {
        this.location = location;
    }

    public OtherAnimals getOtherAnimals() {
        return otherAnimals;
    }

    public void setOtherAnimals(OtherAnimals otherAnimals) {
        this.otherAnimals = otherAnimals;
    }
}