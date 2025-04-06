package com.journaldev.spring.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.journaldev.spring.model.Person;

public class PersonDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query query;

    @InjectMocks
    private PersonDAOImpl personDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testAddPerson() {
        Person person = new Person();
        person.setId(1);
        person.setName("John Doe");

        personDAO.addPerson(person);

        verify(session, times(1)).persist(person);
    }

    @Test
    public void testUpdatePerson() {
        Person person = new Person();
        person.setId(1);
        person.setName("John Doe");

        personDAO.updatePerson(person);

        verify(session, times(1)).update(person);
    }

    @Test
    public void testListPersons() {
        Person person1 = new Person();
        person1.setId(1);
        person1.setName("Alice");

        Person person2 = new Person();
        person2.setId(2);
        person2.setName("Bob");

        List<Person> personList = Arrays.asList(person1, person2);

        when(session.createQuery("from Person")).thenReturn(query);
        when(query.list()).thenReturn(personList);

        List<Person> result = personDAO.listPersons();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
    }

    @Test
    public void testGetPersonById() {
        Person person = new Person();
        person.setId(1);
        person.setName("John Doe");

        when(session.load(Person.class, 1)).thenReturn(person);

        Person result = personDAO.getPersonById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    public void testRemovePerson() {
        Person person = new Person();
        person.setId(1);
        person.setName("John Doe");

        when(session.load(Person.class, 1)).thenReturn(person);

        personDAO.removePerson(1);

        verify(session, times(1)).delete(person);
    }
}
