package com.compozed.models;

import com.compozed.enums.Gender;
import com.compozed.util.Mysql;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created by localadmin on 8/11/16.
 */
public class AnimalTest {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() throws Exception {
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Session session = Mysql.getSession();
        session.beginTransaction();
        session.createNativeQuery("set FOREIGN_KEY_CHECKS = 0").executeUpdate();
        session.createNativeQuery("truncate table animals").executeUpdate();
        session.createNativeQuery("truncate table shelters").executeUpdate();
        session.createNativeQuery("set FOREIGN_KEY_CHECKS = 1").executeUpdate();
        Shelter shelter = new Shelter("Furry Friends Supreme", format.parse("2003-04-30"));
        Animal a1 = new Animal(shelter, "Boomer", Gender.F, format.parse("2011-02-11"),
                "cat", format.parse("2014-06-09"));
        Animal a2 = new Animal(shelter, "Alan", Gender.M, format.parse("2001-12-01"),
                "gopher", format.parse("2004-01-01"));
        Animal a3 = new Animal(shelter, "Cary", Gender.F, format.parse("1992-08-17"),
                "dog", format.parse("2008-11-07"));

        shelter.getAnimals().add(a1);
        shelter.getAnimals().add(a2);
        shelter.getAnimals().add(a3);

        session.save(shelter);
        session.getTransaction().commit();
        session.close();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldCreateNewAnimalAndSave() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal a = new Animal("Rover", "dog");
        session.save(a);
        session.getTransaction().commit();
        session.close();

        assertEquals(4, a.getId());
    }

    @Test
    public void shouldCreateNewAnimalWithDetailsAndSave() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal a = new Animal("Rover", Gender.M, format.parse("2013-02-11"),
                              "dog", format.parse("2014-06-19"));
        session.save(a);
        session.getTransaction().commit();
        session.close();

        assertEquals(4, a.getId());
    }

    @Test(expected = org.hibernate.exception.DataException.class)
    public void shouldNotSaveAnimalDuetoNameTooLong() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal animal = new Animal("RoverRoverRoverRoverRoverRoverRoverRoverRoverRover", "dog");
        try {
            session.save(animal);
            session.getTransaction().commit();

        } finally {
            session.close();
        }
    }

    @Test
    public void shouldUpdateAnimalName() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal animal = session.get(Animal.class, 1);
        try {
            animal.setName("Ronald");
            session.getTransaction().commit();
            session.refresh(animal);
            assertEquals(1, animal.getId());
            assertEquals("Ronald", animal.getName());
        } finally {
            session.close();
        }
    }

    @Test
    public void shouldRetrieveAnimal() throws Exception {
        Session session = Mysql.getSession();

        Animal animal = session.get(Animal.class, 1);
        try {
            assertEquals(1, animal.getId());
            assertEquals("Boomer", animal.getName());
            assertEquals(Gender.F, animal.getSex());
            assertEquals("2011-02-11", format.format(animal.getBirthday()));
            assertEquals("2014-06-09", format.format(animal.getPlacement()));
            assertEquals("cat", animal.getSpecies());
        } finally {
            session.close();
        }
    }
    @Test
    public void shouldDeleteAnimal() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal animal = session.get(Animal.class, 1);
        try {
            session.delete(animal);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    @Test
    public void proofOfConcept() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();

        Shelter shelter = session.get(Shelter.class, 1);
        shelter.setName("Happy Home");

        Optional<Animal> fido = shelter.getAnimals().stream().filter(a -> a.getName().equals("Alan")).findFirst();
        fido.ifPresent(f -> {
            f.setName("Steve");
        });
        Optional<Animal> cary = shelter.getAnimals().stream().filter(a -> a.getName().equals("Cary")).findFirst();
        cary.ifPresent(m -> {
            shelter.getAnimals().remove(m);
            session.delete(m);
        });

        Animal a4 = new Animal(shelter, "Mildred", Gender.F, format.parse("1913-02-11"),
                "dog", format.parse("2000-06-19"));
        shelter.getAnimals().add(a4);
        try {
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

}