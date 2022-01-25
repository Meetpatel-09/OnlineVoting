package com.example.onlinevoting.models;

public class CandidateModel {
    private String name;
    private String email;
    private String id;
    private String phone;
    private String partyName;
    private String profileImage;
    private String partyImage;
    private String aadhaarFront;
    private String aadhaarBack;
    private String isApproved;

    public CandidateModel() {
    }

    public CandidateModel(String name, String email, String id, String phone, String partyName, String profileImage, String partyImage, String aadhaarFront, String aadhaarBack, String isApproved) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.phone = phone;
        this.partyName = partyName;
        this.profileImage = profileImage;
        this.partyImage = partyImage;
        this.aadhaarFront = aadhaarFront;
        this.aadhaarBack = aadhaarBack;
        this.isApproved = isApproved;
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

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String voterID) {
        this.partyName = voterID;
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

    public String getAadhaarFront() {
        return aadhaarFront;
    }

    public void setAadhaarFront(String aadhaarFront) {
        this.aadhaarFront = aadhaarFront;
    }

    public String getAadhaarBack() {
        return aadhaarBack;
    }

    public void setAadhaarBackImage(String aadhaarBack) {
        this.aadhaarBack = aadhaarBack;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }
}
