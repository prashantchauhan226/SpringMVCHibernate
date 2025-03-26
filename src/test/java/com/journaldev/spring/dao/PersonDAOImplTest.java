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
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.journaldev.spring.model.Person;

@ExtendWith(SpringExtension.class)
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
        // Initialize mocks using MockitoAnnotations or @ExtendWith(MockitoExtension.class)
        MockitoAnnotations.openMocks(this);
        personDAO = new PersonDAOImpl();
        personDAO.setSessionFactory(sessionFactory);
        // When getCurrentSession() is called, return the mocked session
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testAddPerson() {
        Person p = new Person();
        personDAO.addPerson(p);
        // Verify that the session.persist method was called once
        verify(session, times(1)).persist(p);
    }

    @Test
    public void testUpdatePerson() {
        Person p = new Person();
        personDAO.updatePerson(p);
        // Verify that the session.update method was called once
        verify(session, times(1)).update(p);
    }

    @Test
    public void testListPersons() {
        List<Person> expectedList = Arrays.asList(new Person(), new Person());
        // When session.createQuery is called with "from Person", return the mocked query
        when(session.createQuery("from Person")).thenReturn(query);
        // When query.list() is called, return the expected list
        when(query.list()).thenReturn(expectedList);
        
        List<Person> actualList = personDAO.listPersons();
        
        assertEquals(expectedList, actualList, "The returned list of persons should match the expected list");
    }

    @Test
    public void testGetPersonById() {
        int personId = 1;
        Person p = new Person();
        // Stub the session.load method
        when(session.load(Person.class, Integer.valueOf(personId))).thenReturn(p);
        
        Person result = personDAO.getPersonById(personId);
        
        assertEquals(p, result, "The person returned should match the expected person");
    }

    @Test
    public void testRemovePerson() {
        int personId = 1;
        Person p = new Person();
        // Stub the session.load method to return a person
        when(session.load(Person.class, Integer.valueOf(personId))).thenReturn(p);
        
        personDAO.removePerson(personId);
        
        // Verify that session.delete was called
        verify(session, times(1)).delete(p);
    }

    @Test
    public void testRemovePersonNotFound() {
        int personId = 2;
        // Simulate a scenario where load returns null (in typical Hibernate use, load may throw an exception
        // or return a proxy; adjust this based on your actual behavior)
        when(session.load(Person.class, Integer.valueOf(personId))).thenReturn(null);
        
        personDAO.removePerson(personId);
        
        // Verify that session.delete is never called since p is null
        verify(session, never()).delete(any(Person.class));
    }
}
