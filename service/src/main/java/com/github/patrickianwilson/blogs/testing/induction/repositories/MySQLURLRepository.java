package com.github.patrickianwilson.blogs.testing.induction.repositories;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import com.github.patrickianwilson.blogs.testing.induction.repositories.entities.UrlServiceEntity;
import com.github.patrickianwilson.blogs.testing.induction.service.entity.UrlDbEntity;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.AlreadyShortenedException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.RepositoryUnavilableException;

/*
 The MIT License (MIT)

 Copyright (c) 2014 Patrick Wilson

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
public class MySQLURLRepository implements URLRepository {

    private final Configuration cfg;
    private final ServiceRegistry serviceRegistry;
    private SessionFactory sessionFactory;

    public MySQLURLRepository() {
        cfg = new Configuration().configure();
        serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(cfg.getProperties())
                .buildServiceRegistry();

        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
    }

    @Override
    public UrlServiceEntity findByLongForm(String longForm) {
        //first check that this url doesn't already exist.
        Session session  = acquireSession();

        try {
            UrlDbEntity reference = new UrlDbEntity();
            reference.setLongForm(longForm);

            UrlServiceEntity serviceEntity = UrlDbEntity.toServiceEntity(findByCriteria(reference, session));

            return serviceEntity;
        } finally {
            session.close();
        }


    }

    @Override
    public void persist(UrlServiceEntity entity) {
        Session session  = acquireSession();
        Transaction tx = session.beginTransaction();
        try {
            UrlDbEntity dbEntity = UrlDbEntity.fromServiceEntity(entity);

            //first check that this url doesn't already exist.
            UrlDbEntity reference = new UrlDbEntity();
            reference.setLongForm(entity.getLongForm());

            UrlDbEntity alreadySaved = findByCriteria(reference, session);

            if (alreadySaved != null) {
                throw new AlreadyShortenedException();
            }

            session.save(dbEntity);

        } finally {
            tx.commit();
            session.close();
        }

    }

    private UrlDbEntity findByCriteria(UrlDbEntity reference, Session session) {

        Criteria criteria = session.createCriteria(UrlDbEntity.class);

        if (reference.getLongForm() != null)
            criteria.add(Restrictions.eq("longForm", reference.getLongForm()));

        if (reference.getShortForm() != null)
            criteria.add(Restrictions.eq("shortForm", reference.getShortForm()));

        criteria.setMaxResults(1);

        List<UrlDbEntity> resultSet = criteria.list();

        if(resultSet.size() > 0) {
            return resultSet.get(0);
        } else {
            return null;
        }

    }

    private synchronized Session acquireSession() {
        Session result = sessionFactory.openSession();
        if (!result.isConnected()) {
            //the db must not be available anymore.
            throw new RepositoryUnavilableException("Unable to Connect to the database");
        }

        return result;
    }

}
