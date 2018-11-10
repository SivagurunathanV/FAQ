package Interface;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by sivagurunathanvelayutham on Apr, 2018
 */
public abstract class AbstractHibernateDAO<T> extends AbstractDAO<T>{
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public AbstractHibernateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public abstract void batchInsert(List<T> values);

    public abstract List<T> findAll();

    public abstract Long findNoOfRows();

    public abstract List<T> findAllWithPagination(int start, int end);

    public abstract T findById(int id);

    public abstract void update(T newValue);

}
