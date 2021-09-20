package ru.dexsys.lovetohibernatedemo.domain.repository.impl;

import org.springframework.stereotype.Repository;
import ru.dexsys.lovetohibernatedemo.domain.entity.News;
import ru.dexsys.lovetohibernatedemo.domain.repository.NewsRepositoryCustom;
import ru.dexsys.lovetohibernatedemo.domain.repository.filter.NewsFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Repository
public class NewsRepositoryCustomImpl implements NewsRepositoryCustom {
    @PersistenceContext
    private EntityManager manager;

    @Override
    @SuppressWarnings("unchecked")
    public List<News> findAllByFilter(NewsFilter filter) {
        String request =
                "select distinct n " +
                        "from News n " +
                        "left join n.readers r " +
                        "left join n.divisions d " +
                        "left join n.files f " +
                        "where (1=1) " +
                        (isNull(filter.getFrom()) ? "" : "and n.createDate >= :from ") +
                        (isNull(filter.getTo()) ? "" : "and n.createDate <= :to ") +
                        (isNull(filter.getMessageFragment()) ? "" : "and UPPER(n.message) like ('%' || UPPER(:text) || '%') ");

        Query query = manager.createQuery(request);

        if (!isNull(filter.getFrom())) {
            query.setParameter("from", filter.getFrom());
        }
        if (!isNull(filter.getTo())) {
            query.setParameter("to", filter.getTo());
        }
        if (!isNull(filter.getMessageFragment())) {
            query.setParameter("text", filter.getMessageFragment());
        }

        return query.getResultList();
    }
}
