package com.compozed.models;

import javax.persistence.*;

@Entity
@Table(name = "clients")
@Access(AccessType.PROPERTY)
public class Client {
    private int id;
    private String name;

    public Client() {
    }

    public Client(String name) {
        this.name = name;
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
}
