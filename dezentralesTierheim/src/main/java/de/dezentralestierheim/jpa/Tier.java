package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Tier extends PanacheEntity {

    public enum Tierart {
        HUND, KATZE
    }

    public enum Status {
        GESUND, KRANK, TOT
    }

    private String name;
    private Long pflegestellenID;
    private String inseratText;
    private Tierart tierart;
    private String rasse;
    private Boolean istKinderfreundlich;
    private Boolean verträgtSichMitAnderenTieren;
    private Status status;
    private LocalDate geburtsdatum;
    private Boolean istAdoptiert;
    //Wie machen wir das mit dem Bild?

    public Long getId() {
        return id;
    }

    public Long getPflegestellenID() {
        return pflegestellenID;
    }

    public void setPflegestellenID(Long pflegestellenID) {
        this.pflegestellenID = pflegestellenID;
    }

    public String getInseratText() {
        return inseratText;
    }

    public void setInseratText(String inseratText) {
        this.inseratText = inseratText;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Tierart getTierart() {
        return tierart;
    }

    public void setTierart(Tierart tierart) {
        this.tierart = tierart;
    }

    public String getRasse() {
        return rasse;
    }

    public void setRasse(String rasse) {
        this.rasse = rasse;
    }

    public Boolean getGoodWithOtherAnimals() {
        return verträgtSichMitAnderenTieren;
    }

    public void setGoodWithOtherAnimals(Boolean goodWithOtherAnimals) {
        verträgtSichMitAnderenTieren = goodWithOtherAnimals;
    }

    public Boolean getChildFriendly() {
        return istKinderfreundlich;
    }

    public void setChildFriendly(Boolean childFriendly) {
        istKinderfreundlich = childFriendly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public Boolean getAdopted() {
        return istAdoptiert;
    }

    public void setAdopted(Boolean adopted) {
        istAdoptiert = adopted;
    }

    public Status getStatus() {
        return status;
    }


}