package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public class Library {

    private int id;

    private String address;

    private HeadLibrarian headLibrarian;

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Id
    public int getId(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public HeadLibrarian getHeadLibrarian() {
        return this.headLibrarian;
    }

    public void setHeadLibrarian(HeadLibrarian headLibrarian) {
        this.headLibrarian = headLibrarian;
    }
}