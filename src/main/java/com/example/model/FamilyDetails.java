package com.example.model;

import com.example.model.BrotherType;
import com.example.model.FamilyStatus;

import jakarta.persistence.*;
import com.example.model.Profile;

@Entity
@Table(name="family_details")
public class FamilyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne



    private Profile profile;

    @ManyToOne
    private FamilyType familyType;

    @ManyToOne
    private FamilyStatus familyStatus;

    @ManyToOne
    private BrotherType brotherType;

    @ManyToOne
    private SisterType sisterType;

    private String fatherOccupation;
    private String motherOccupation;

    public FamilyDetails(){}

    public Long getId(){
        return id;
    }

    public Profile getProfile(){
        return profile;
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }

    public FamilyType getFamilyType(){
        return familyType;
    }

    public void setFamilyType(FamilyType familyType){
        this.familyType = familyType;
    }

    public FamilyStatus getFamilyStatus(){
        return familyStatus;
    }

    public void setFamilyStatus(FamilyStatus familyStatus){
        this.familyStatus = familyStatus;
    }

    public BrotherType getBrotherType(){
        return brotherType;
    }

    public void setBrotherType(BrotherType brotherType){
        this.brotherType = brotherType;
    }

    public SisterType getSisterType(){
        return sisterType;
    }

    public void setSisterType(SisterType sisterType){
        this.sisterType = sisterType;
    }

    public String getFatherOccupation(){
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation){
        this.fatherOccupation = fatherOccupation;
    }

    public String getMotherOccupation(){
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation){
        this.motherOccupation = motherOccupation;
    }
}