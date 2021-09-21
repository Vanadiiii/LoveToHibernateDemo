package ru.dexsys.lovetohibernatedemo.domain.repository.impl;

import org.hibernate.jpa.QueryHints;
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
        String request0 = "" +
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

        Query query = manager.createQuery(
                "select distinct n " +
                        "from News n " +
                        "  where (1=1) " +
                        (isNull(filter.getFrom()) ? ""
                                : " and n.createDate >= :from ") +
                        (isNull(filter.getTo()) ? ""
                                : " and n.createDate <= :to ") +
                        (isNull(filter.getMessageFragment()) ? ""
                                : "and UPPER(n.message) like ('%' || UPPER(:text) || '%') ")
        );
        if (!isNull(filter.getFrom())) {
            query.setParameter("from", filter.getFrom());
        }
        if (!isNull(filter.getTo())) {
            query.setParameter("to", filter.getTo());
        }
        if (!isNull(filter.getMessageFragment())) {
            query.setParameter("text", filter.getMessageFragment());
        }
        List<News> commonNews = query.getResultList();

        if (commonNews.isEmpty()) {
            return commonNews;
        }

        List<News> personalNews = manager.createQuery(
                        "select distinct n " +
                                " from News n " +
                                " left join fetch n.readers r " +
                                " where (n in :commonNews) " +
                                " and r.name = :name " +
                                " and n.personal = TRUE"
                )
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter("commonNews", commonNews)
                .setParameter("name", filter.getReaderName())
                .getResultList();

        Query divisionNewsQuery = manager.createQuery(
                        "select distinct n " +
                                " from News n " +
                                " left join fetch n.divisions d " +
                                " where (n in :commonNews) " +
                                " and " +
                                (isEmpty(filter.getDivisionTypes())
                                        ? " (1!=1) "
                                        : " d.type in :types")
                )
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter("commonNews", commonNews);
        if (!isEmpty(filter.getDivisionTypes())) {
            divisionNewsQuery.setParameter("types", filter.getDivisionTypes());
        }
        List<News> divisionNews = divisionNewsQuery.getResultList();

        Query roleNewsQuery = manager.createQuery(
                        "select distinct n " +
                                " from News n " +
                                " left join fetch n.readers r " +
                                " where (n in :commonNews) " +
                                " and " +
                                (isEmpty(filter.getReaderRoles())
                                        ? " (1!=1) "
                                        : " r.role in :roles")
                )
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter("commonNews", commonNews);
        if (!isEmpty(filter.getReaderRoles())) {
            roleNewsQuery.setParameter("roles", filter.getReaderRoles());
        }
        List<News> roleNews = roleNewsQuery.getResultList();

        Query fileNewsQuery = manager.createQuery(
                        "select distinct n " +
                                " from News n " +
                                " left join fetch n.files f " +
                                " where (n in :commonNews) " +
                                " and " +
                                (isEmpty(filter.getFileExtensions())
                                        ? " (1!=1) "
                                        : " f.extension in :extensions")
                )
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter("commonNews", commonNews);
        if (!isEmpty(filter.getFileExtensions())) {
            fileNewsQuery.setParameter("extensions", filter.getFileExtensions());
        }
        List<News> fileNews = fileNewsQuery.getResultList();

        Query resultQuery = manager.createQuery(
                        "select distinct n " +
                                " from News n " +
                                " where n in :commonNews " +
                                " and ( " +
                                "   (" +
                                (isEmpty(personalNews) ? " (1!=1) " : " n in :personalNews") +
                                "   ) " +
                                "   or " +
                                "   (" +
                                (isEmpty(divisionNews) ? " (1!=1) " : " n in :divisionNews") +
                                "   )" +
                                "   and " +
                                "   (" +
                                (isEmpty(roleNews) ? " (1!=1) " : " n in :roleNews") +
                                "   ) " +
                                ") " +
                                " and " +
                                (isEmpty(fileNews) ? " (1!=1) " : " n in :fileNews")
                )
                .setParameter("commonNews", commonNews);
        if (!isEmpty(personalNews)) {
            resultQuery.setParameter("personalNews", personalNews);
        }
        if (!isEmpty(divisionNews)) {
            resultQuery.setParameter("divisionNews", divisionNews);
        }
        if (!isEmpty(roleNews)) {
            resultQuery.setParameter("roleNews", roleNews);
        }
        if (!isEmpty(fileNews)) {
            resultQuery.setParameter("fileNews", fileNews);
        }

        return resultQuery.getResultList();
    }
}
