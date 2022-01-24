package com.example.onlinevoting.models;

public class VotersModel {
    private String name;
    private String email;
    private String isVerified;
    private String isProfileComplete;
    private String id;
    private String phone;
    private String voterID;
    private String profileImage;
    private String voterIDImage;

    public VotersModel() {
    }

    public VotersModel(String name, String email, String isVerified, String isProfileComplete, String id, String phone, String voterID, String profileImage, String voterIDImage) {
        this.name = name;
        this.email = email;
        this.isVerified = isVerified;
        this.isProfileComplete = isProfileComplete;
        this.id = id;
        this.phone = phone;
        this.voterID = voterID;
        this.profileImage = profileImage;
        this.voterIDImage = voterIDImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getVoterIDImage() {
        return voterIDImage;
    }

    public void setVoterIDImage(String voterIDImage) {
        this.voterIDImage = voterIDImage;
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

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsProfileComplete() {
        return isProfileComplete;
    }

    public void setIsProfileComplete(String isProfileComplete) {
        this.isProfileComplete = isProfileComplete;
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
}
