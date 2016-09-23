package com.github.patrickianwilson.template.java.web.repositories;

import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.DataException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.patrickianwilson.template.java.web.repositories.entities.UrlServiceEntity;
import com.github.patrickianwilson.template.java.web.service.entity.UrlDbEntity;
import com.github.patrickianwilson.template.java.web.service.exceptions.InvalidShorteningRequestException;

/**
 * This class represents the base case test for the MY SQL database interactions (which can't be mocked
 * safely.
 *
 * I actually wrote this test case before writing the repository implementation and associated service calls as I
 * find it useful to really understand the thirdparty API edge cases before baking anything into code.
 *
 * Test Plan.
 *
 * 1.) Is the configuration correct - IE: can the class under test actually be constructed?
 * 2.) Do a simple persist and retrieve success case (insert followed by retreival)
 * 3.) the "entity not found" case - lookup a URL long form that doesn't yet exist in the database - this is a common case and should not throw errors.
 * 4.) insert a long URL into the database that is over 255 characters (max for that column) - expect an exception
 * 5.) insert a short URL into the database that is over 40 characters (max for the shortform column) - expect an exception.
 *
 */
public class MySQLURLRepositoryTests {

    private MySQLURLRepository underTest = new MySQLURLRepository();

    private Configuration cfg;
    private ServiceRegistry serviceRegistry;
    private SessionFactory sessionFactory;

    @Before()
    public void setup() {
        cfg = new Configuration().configure();
        serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(cfg.getProperties())
                .buildServiceRegistry();

        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
    }


    @After
    public void cleanupDatabase() {
        Session s = sessionFactory.openSession();
        Transaction tx = s.beginTransaction();
        try {
            List allEntities = s.createCriteria(UrlDbEntity.class).list();

            for (Object entity: allEntities) {
                s.delete(entity);
            }
        } finally {
            tx.commit();
            s.close();
        }

    }
    /**
     * likely this would fail with an exception if there was a problem
     */
    @Test
    public void canRepositoryByInstantiated() {
        assertNotNull("Under Test was not instantiated for some reason.", underTest);
    }

    @Test
    public void persistAndRetrieveCase() {
        final String longForm = "http://www.google.com";

        UrlServiceEntity url = new UrlServiceEntity();
        url.setLongForm(longForm);
        url.setShortForm("http://go.og/1");

        underTest.persist(url);

        UrlServiceEntity actual = underTest.findByLongForm(longForm);

        assertThat("The persisted and retrieved url entities are not equal!", actual, is(equalTo(url)));
    }

    @Test
    public void urlNotCachedCase() {
        assertNull("URL was not in database but somehow found an entity.", underTest.findByLongForm("http://www.google.com"));

    }

    @Test(expected = DataException.class)
    public void longNameTooManyCharacters() {
        StringBuilder urlString = new StringBuilder("http://www.google.com");
        for(int i = 0; i < (257 - 22 /* base url is 22 chars long. */); i++) {
            urlString.append('a');
        }


        UrlServiceEntity url = new UrlServiceEntity();
        url.setLongForm(urlString.toString());
        url.setShortForm("http://go.og/1");

        underTest.persist(url);
    }

    @Test(expected = DataException.class)
    public void shortNameTooManyCharacters() {
        StringBuilder urlString = new StringBuilder("http://g.co");
        for(int i = 0; i < (46 - 11 /* base url is 11 chars long. */); i++) {
            urlString.append('a');
        }


        UrlServiceEntity url = new UrlServiceEntity();
        url.setLongForm("http://www.google.com");
        url.setShortForm(urlString.toString());

        underTest.persist(url);
    }

}
