package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public class OnlineMember extends UserRole {

    private String email;

    private String username;

    private String password;

    private boolean inTown;

    public String getEmail() {
        return email;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isInTown() {
        return this.inTown;
    }

    public void setInTown(boolean flag) {
        this.inTown = flag;
    }
}