package ru.dexsys.lovetohibernatedemo.domain.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public List<News> findAllByFilter(NewsFilter filter) {
        String request = "" +
                "select distinct n " +
                "from News n " +
                "  left join n.readers r  " +
                "  left join n.divisions d " +
                "  left join n.files f " +
                "  where (1=1) " +
                (isNull(filter.getFrom()) ? ""
                        : " and n.createDate >= :from ") +
                (isNull(filter.getTo()) ? ""
                        : " and n.createDate <= :to ") +
                (" and (" +
                        "(n.personal = TRUE and r.name = :name) " +
                        " or (" +
                        (isEmpty(filter.getDivisionTypes()) ? " (1!=1) " : " (d.type in :types) ") +
                        " or " +
                        (isEmpty(filter.getReaderRoles()) ? " (1!=1) " : " (r.role in :roles) ") +
                        "))"
                ) +
                (isEmpty(filter.getFileExtensions()) ? ""
                        : " and f.extension in :extensions ") +
                (isNull(filter.getMessageFragment()) ? ""
                        : "and UPPER(n.message) like ('%' || UPPER(:text) || '%') ");

        Query query = manager.createQuery(request);

        query.setParameter("name", filter.getReaderName());

        if (!isNull(filter.getFrom())) {
            query.setParameter("from", filter.getFrom());
        }
        if (!isNull(filter.getTo())) {
            query.setParameter("to", filter.getTo());
        }
        if (!isNull(filter.getMessageFragment())) {
            query.setParameter("text", filter.getMessageFragment());
        }
        if (!isEmpty(filter.getFileExtensions())) {
            query.setParameter("extensions", filter.getFileExtensions());
        }
        if (!isEmpty(filter.getDivisionTypes())) {
            query.setParameter("types", filter.getDivisionTypes());
        }
        if (!isEmpty(filter.getReaderRoles())) {
            query.setParameter("roles", filter.getReaderRoles());
        }

        return query.getResultList();
    }
}
