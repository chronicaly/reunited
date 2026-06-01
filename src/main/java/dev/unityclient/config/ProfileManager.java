package dev.unityclient.config;

public final class ProfileManager {
    private String currentProfile = "default";

    public String currentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(String currentProfile) {
        this.currentProfile = currentProfile == null || currentProfile.isBlank() ? "default" : currentProfile;
    }
}
