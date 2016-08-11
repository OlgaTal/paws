package com.compozed.models;

import com.compozed.enums.Gender;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by localadmin on 8/11/16.
 */

@Entity
@Table(name = "animals")
@Access(AccessType.PROPERTY)
public class Animal {
    private int id;
//    private int shelter_id;
    private String name;
    private Gender sex;
    private Date birthday;
    private String species;
    private Date placement;
    private Shelter shelter;

    public Animal(String name, String species) {
        this.name = name;
        this.species = species;
    }

    public Animal() {
    }

    public Animal(String name, Gender sex, Date birthday, String species, Date placement) {
        this(null, name, sex, birthday, species, placement);
    }

    public Animal(Shelter shelter, String name, Gender sex, Date birthday, String species, Date placement) {
        this.shelter = shelter;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.species = species;
        this.placement = placement;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "shelter_id", referencedColumnName = "id")
    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "species", nullable = false, length = 45)
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "placement")
    public Date getPlacement() {
        return placement;
    }

    public void setPlacement(Date placement) {
        this.placement = placement;
    }
}
