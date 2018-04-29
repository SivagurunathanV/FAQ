package dao;

import Interface.AbstractHibernateDAO;
import io.dropwizard.hibernate.AbstractDAO;
import model.RawData;
import model.TokenizedData;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Mar, 2018
 */
public class TokenizedDataDao extends AbstractHibernateDAO<TokenizedData> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public TokenizedDataDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void batchInsert(List<TokenizedData> tokenizedDataList){
        Session session = currentSession().getSessionFactory().openSession();
        for (int i=0;i<tokenizedDataList.size();i++) {
            session.save(tokenizedDataList.get(i));
            if(i % 20 == 0){
                session.flush();
                session.clear();
            }
        }
        session.close();
    }

    public List<TokenizedData> findAll(){
        Session session = currentSession().getSessionFactory().openSession();
        Query query = session.createQuery("from TokenizedData").setMaxResults(50);
        return query.list();
    }

    @Override
    public Long findNoOfRows() {
        Criteria criteriaForCount = currentSession().createCriteria(TokenizedData.class);
        criteriaForCount.setProjection(Projections.rowCount());
        Long count = (Long) criteriaForCount.uniqueResult();
        return count;
    }

    @Override
    public List<TokenizedData> findAllWithPagination(int start, int end) {
        Criteria criteria = currentSession().createCriteria(TokenizedData.class)
                .setFirstResult(start)
                .setMaxResults(end);
        return criteria.list();
    }

    @Override
    public TokenizedData findById(int id) {
        Criteria criteria = currentSession().createCriteria(TokenizedData.class)
                .add(Restrictions.eq("id", id));
        return (TokenizedData) criteria.uniqueResult();
    }

    @Override
    public void update(TokenizedData newValue) {

    }


}
