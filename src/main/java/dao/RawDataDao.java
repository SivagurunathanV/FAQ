package dao;

import Interface.AbstractHibernateDAO;
import io.dropwizard.hibernate.AbstractDAO;
import model.RawData;
import org.hibernate.*;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import javax.xml.transform.Transformer;
import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class RawDataDao extends AbstractHibernateDAO<RawData> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public RawDataDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void batchInsert(List<RawData> values) {
        Session session = currentSession().getSessionFactory().openSession();
        for (int i=0;i<values.size();i++) {
            session.save(values.get(i));
            if(i % 20 == 0){
                session.flush();
                session.clear();
            }
        }
        session.close();
    }

    @Override
    public List<RawData> findAll() {
        Session session = currentSession().getSessionFactory().openSession();
        Query query = session.createQuery("from RawData");
        return query.list();
    }

    @Override
    public Long findNoOfRows() {
        Criteria criteriaForCount = currentSession().createCriteria(RawData.class);
        criteriaForCount.setProjection(Projections.rowCount());
        Long count = (Long) criteriaForCount.uniqueResult();
        return count;
    }

    @Override
    public List<RawData> findAllWithPagination(int start, int end) {
        Session session = currentSession().getSessionFactory().openSession();
        Query query = session.createQuery("from RawData")
                .setFirstResult(start)
                .setMaxResults(end);
        return query.list();
    }

    @Override
    public RawData findById(int id) {
        return null;
    }

    @Override
    public void update(RawData newValue) {

    }

    public List<RawData> findByAnswerType(String type){
        Session session = currentSession().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(RawData.class)
                .add(Restrictions.eq("answerType", type));
        return criteria.list();
    }

}
