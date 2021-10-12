package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class OfflineMember extends UserRole {

    @Id
    private int id;
}