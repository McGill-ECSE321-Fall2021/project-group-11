package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Newspaper extends Item {

    @Id
    private int id;
}