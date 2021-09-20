package ru.dexsys.lovetohibernatedemo.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dexsys.lovetohibernatedemo.domain.entity.Division;
import ru.dexsys.lovetohibernatedemo.domain.entity.File;
import ru.dexsys.lovetohibernatedemo.domain.entity.News;
import ru.dexsys.lovetohibernatedemo.domain.entity.Reader;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.FileExtension;
import ru.dexsys.lovetohibernatedemo.domain.repository.filter.NewsFilter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@DataJpaTest
@ExtendWith(SpringExtension.class)
class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private DivisionRepository divisionRepository;
    @Autowired
    private FileRepository fileRepository;

    private final EasyRandom random = new EasyRandom(
            new EasyRandomParameters()
                    .seed(System.nanoTime())
                    .collectionSizeRange(5, 10)
                    .randomize(field -> field.getName().equals("news"), () -> null)
                    .randomize(UUID.class, () -> null)
                    .randomizationDepth(2)
    );

    @Test
    void test01() {
        News news = create();

        log.error(news.getId().toString());

        newsRepository.findAllByFilter(
                        NewsFilter
                                .builder()
                                .messageFragment(news.getMessage().substring(0, 2))
                                .build()
                )
                .stream()
                .map(News::toString)
                .forEach(log::info);
    }

    private News create() {
        News someNews = random.nextObject(News.class);

        List<File> files = random.objects(File.class, 2 + random.nextInt(5))
                .collect(Collectors.toList());

        List<Reader> readers = random.objects(Reader.class, 2 + random.nextInt(5))
                .collect(Collectors.toList());

        List<Division> divisions = random.objects(Division.class, 2 + random.nextInt(5))
                .collect(Collectors.toList());

        someNews.setFiles(files);
        someNews.setReaders(readers);
        someNews.setDivisions(divisions);

        return newsRepository.saveAndFlush(someNews);
    }

    private Collection<News> create(int count) {
        return Stream.generate(this::create)
                .limit(count)
                .collect(Collectors.toList());
    }
}