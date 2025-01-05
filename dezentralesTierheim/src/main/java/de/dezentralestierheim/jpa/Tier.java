package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Entity
public class Tier extends PanacheEntity {

    public enum Tierart {
        HUND, KATZE
    }

    public enum Status {
        GESUND, KRANK, TOT
    }

    public enum AufnahmeNichtMoeglich {
        FALSE, RUECKZIEHER_VOM_BESITZER, KEINE_KAPAZITAET
    }

    private String name;

    private Long pflegestellenID;

    private String inseratText;

    @Enumerated(EnumType.STRING)
    private Tierart tierart;

    private String rasse;
    private Boolean istKinderfreundlich;
    private Boolean vertraegtSichMitAnderenTieren;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate geburtsdatum;
    private Boolean istAdoptiert;

    @Enumerated(EnumType.STRING)
    private AufnahmeNichtMoeglich aufnahmeNichtMoeglich; //Wenn Pflegestellen keine Kapazität haben oder wenn Aufnahme von Tierbesitzer abgebrochen wurde

    public Long getId() {
        return id;
    }

    public Long getPflegestellenID() {
        return pflegestellenID;
    }

    public void setPflegestellenID(Long pflegestellenID) {
        this.pflegestellenID = pflegestellenID;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getIstKinderfreundlich() {
        return istKinderfreundlich;
    }

    public void setIstKinderfreundlich(Boolean istKinderfreundlich) {
        this.istKinderfreundlich = istKinderfreundlich;
    }

    public AufnahmeNichtMoeglich getAufnahmeNichtMoeglich() {
        return aufnahmeNichtMoeglich;
    }

    public void setAufnahmeNichtMoeglich(AufnahmeNichtMoeglich aufnahmeNichtMoeglich) {
        this.aufnahmeNichtMoeglich = aufnahmeNichtMoeglich;
    }

    public Boolean getIstAdoptiert() {
        return istAdoptiert;
    }

    public void setIstAdoptiert(Boolean istAdoptiert) {
        this.istAdoptiert = istAdoptiert;
    }

    public Boolean getVertraegtSichMitAnderenTieren() {
        return vertraegtSichMitAnderenTieren;
    }

    public void setVertraegtSichMitAnderenTieren(Boolean vertraegtSichMitAnderenTieren) {
        this.vertraegtSichMitAnderenTieren = vertraegtSichMitAnderenTieren;
    }

}