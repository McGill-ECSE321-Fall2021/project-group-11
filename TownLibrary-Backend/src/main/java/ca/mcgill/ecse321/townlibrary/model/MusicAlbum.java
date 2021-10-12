package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class MusicAlbum extends Item {

    @Id
    private int id;
}