package com.example.onlinevoting.models;

public class CandidateModel {
    private String name;
    private String email;
    private String id;
    private String phone;
    private String voterID;
    private String profileImage;
    private String partyImage;
    private String aadhaarFrontImage;
    private String aadhaarBackImage;

    public CandidateModel() {
    }

    public CandidateModel(String name, String email, String id, String phone, String voterID, String profileImage, String partyImage, String aadhaarFrontImage, String aadhaarBackImage) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.phone = phone;
        this.voterID = voterID;
        this.profileImage = profileImage;
        this.partyImage = partyImage;
        this.aadhaarFrontImage = aadhaarFrontImage;
        this.aadhaarBackImage = aadhaarBackImage;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPartyImage() {
        return partyImage;
    }

    public void setPartyImage(String partyImage) {
        this.partyImage = partyImage;
    }

    public String getAadhaarFrontImage() {
        return aadhaarFrontImage;
    }

    public void setAadhaarFrontImage(String aadhaarFrontImage) {
        this.aadhaarFrontImage = aadhaarFrontImage;
    }

    public String getAadhaarBackImage() {
        return aadhaarBackImage;
    }

    public void setAadhaarBackImage(String aadhaarBackImage) {
        this.aadhaarBackImage = aadhaarBackImage;
    }
}
