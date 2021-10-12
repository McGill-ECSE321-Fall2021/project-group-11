package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class OnlineMember extends UserRole {

    private String email;

    private String username;

    private String password;
}