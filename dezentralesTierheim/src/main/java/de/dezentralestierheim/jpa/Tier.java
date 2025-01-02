package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
    private String breed; //Rasse
    private Boolean isChildFriendly;
    private Boolean isGoodWithOtherAnimals;
    private Status status;
    private LocalDate dayOfBirth;
    private Boolean isAdopted;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Boolean getGoodWithOtherAnimals() {
        return isGoodWithOtherAnimals;
    }

    public void setGoodWithOtherAnimals(Boolean goodWithOtherAnimals) {
        isGoodWithOtherAnimals = goodWithOtherAnimals;
    }

    public Boolean getChildFriendly() {
        return isChildFriendly;
    }

    public void setChildFriendly(Boolean childFriendly) {
        isChildFriendly = childFriendly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public Boolean getAdopted() {
        return isAdopted;
    }

    public void setAdopted(Boolean adopted) {
        isAdopted = adopted;
    }

    public Status getStatus() {
        return status;
    }


}