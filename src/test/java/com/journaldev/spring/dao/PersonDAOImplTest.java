package com.journaldev.spring.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.journaldev.spring.model.Person;

@ExtendWith(MockitoExtension.class)
public class PersonDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Person> query;

    private PersonDAOImpl personDAO;

    @BeforeEach
    public void setUp() {
        // Initialize the DAO and inject the mocked sessionFactory.
        personDAO = new PersonDAOImpl();
        personDAO.setSessionFactory(sessionFactory);
        // When getCurrentSession() is called, return the mocked session.
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testAddPerson() {
        Person p = new Person();
        personDAO.addPerson(p);
        // Verify that the session.persist method was invoked with the provided Person.
        verify(session, times(1)).persist(p);
    }

    @Test
    public void testUpdatePerson() {
        Person p = new Person();
        personDAO.updatePerson(p);
        // Verify that the session.update method was invoked.
        verify(session, times(1)).update(p);
    }

    @Test
    public void testListPersons() {
        // Prepare a list of persons to be returned by the query.
        List<Person> expectedList = Arrays.asList(new Person(), new Person());
        // Stub the session.createQuery method.
        when(session.createQuery("from Person")).thenReturn(query);
        // Stub the query.list method.
        when(query.list()).thenReturn(expectedList);

        List<Person> actualList = personDAO.listPersons();
        // Check that the returned list matches the expected list.
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testGetPersonById() {
        int personId = 1;
        Person p = new Person();
        // Simulate the session.load behavior.
        when(session.load(Person.class, Integer.valueOf(personId))).thenReturn(p);

        Person result = personDAO.getPersonById(personId);
        // Compare the person loaded from the DAO with our stubbed value.
        assertSame(p, result);
    }

    @Test
    public void testRemovePerson() {
        int personId = 2;
        Person p = new Person();
        when(session.load(Person.class, Integer.valueOf(personId))).thenReturn(p);

        personDAO.removePerson(personId);
        // Verify that the session.delete method is called with the loaded Person.
        verify(session, times(1)).delete(p);
    }

    @Test
    public void testRemovePersonNotFound() {
        int personId = 3;
        // Simulate that session.load returns null (for testing purposes).
        when(session.load(Person.class, Integer.valueOf(personId))).thenReturn(null);

        personDAO.removePerson(personId);
        // Verify that session.delete is never called when no Person is found.
        verify(session, never()).delete(any(Person.class));
    }
}
