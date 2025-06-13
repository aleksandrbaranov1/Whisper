package org.example.whisper.DTO;

public class UpdateBIODTO {
    private Long userId;
    private String bio;

    public UpdateBIODTO(){};
    public UpdateBIODTO(Long userId, String bio){
        this.userId = userId;
        this.bio = bio;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
