package org.example.whisper.DTO;

import org.example.whisper.Entity.User;

public class UserDTO {
    private String name;
    private String email;
    private Long id;
    private String role;
    private String bio;

    public UserDTO(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.id = user.getId();
        this.role = user.getRole();
        this.bio = user.getBio();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
