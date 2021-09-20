package ru.dexsys.lovetohibernatedemo.domain.repository;

import ru.dexsys.lovetohibernatedemo.domain.entity.News;
import ru.dexsys.lovetohibernatedemo.domain.repository.filter.NewsFilter;

import java.util.List;

public interface NewsRepositoryCustom {
    List<News> findAllByFilter(NewsFilter filter);
}
