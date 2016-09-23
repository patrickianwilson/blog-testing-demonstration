package com.github.patrickianwilson.template.java.web.repositories;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.DataException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import com.github.patrickianwilson.template.java.web.repositories.entities.UrlServiceEntity;
import com.github.patrickianwilson.template.java.web.service.entity.UrlDbEntity;
import com.github.patrickianwilson.template.java.web.service.exceptions.AlreadyShortenedException;
import com.github.patrickianwilson.template.java.web.service.exceptions.InvalidShorteningRequestException;
import com.github.patrickianwilson.template.java.web.service.exceptions.RepositoryUnavilableException;

/**
 * Created by pwilson on 9/20/16.
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
