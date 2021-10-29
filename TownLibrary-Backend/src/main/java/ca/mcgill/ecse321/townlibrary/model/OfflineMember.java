package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public class OfflineMember extends UserRole {

    private boolean inTown;

    public boolean isInTown() {
        return this.inTown;
    }

    public void setInTown(boolean flag) {
        this.inTown = flag;
    }
}