package dao;

import Interface.AbstractHibernateDAO;
import model.AdvTokenizedData;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public class AdvTokenizedDataDao extends AbstractHibernateDAO<AdvTokenizedData> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public AdvTokenizedDataDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void batchInsert(List<AdvTokenizedData> values) {
        Session session = currentSession().getSessionFactory().openSession();
        for (int i = 0; i < values.size(); i++) {
            session.save(values.get(i));
            if(i % 20 == 0){
                session.flush();
                session.clear();
            }
        }
        session.close();
    }

    @Override
    public List<AdvTokenizedData> findAll() {
        return null;
    }

    @Override
    public Long findNoOfRows() {
        return null;
    }

    @Override
    public List<AdvTokenizedData> findAllWithPagination(int start, int end) {
        Session session = currentSession().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(AdvTokenizedData.class)
                .setFirstResult(start)
                .setMaxResults(end);
        return criteria.list();
    }

    @Override
    public AdvTokenizedData findById(int id) {
        Session session = currentSession().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(AdvTokenizedData.class)
                .add(Restrictions.eq("id",id));
        return (AdvTokenizedData) criteria.uniqueResult();
    }

    @Override
    public void update(AdvTokenizedData newValue) {

    }


    public List<AdvTokenizedData> findQuestionByWordnetFeatures(String feature){
        Session session = currentSession().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(AdvTokenizedData.class)
                .add(Restrictions.ilike("wordnet_features", feature, MatchMode.ANYWHERE))
                .setMaxResults(2);
        return criteria.list();
    }
}
