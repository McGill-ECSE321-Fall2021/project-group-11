package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Archive extends Item {

    @Override
    public String toString() {
        final int id = this.getId();
        final String name = this.getName();
        final Status status = this.getStatus();
        final Library lib = this.getLibrary();
        return "Archive {id=" + id
            + ", name=" + name
            + ", status=" + status
            + ", library.id=" + (lib == null ? null : lib.getId()) + "}";
    }
}