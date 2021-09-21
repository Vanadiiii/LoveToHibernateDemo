package ru.dexsys.lovetohibernatedemo.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dexsys.lovetohibernatedemo.domain.entity.Division;
import ru.dexsys.lovetohibernatedemo.domain.entity.File;
import ru.dexsys.lovetohibernatedemo.domain.entity.News;
import ru.dexsys.lovetohibernatedemo.domain.entity.Reader;
import ru.dexsys.lovetohibernatedemo.domain.repository.filter.NewsFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@ExtendWith(SpringExtension.class)
class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private News news;

    private final EasyRandom random = new EasyRandom(
            new EasyRandomParameters()
                    .seed(System.nanoTime())
                    .stringLengthRange(5, 20)
                    .collectionSizeRange(5, 10)
                    .randomize(field -> field.getName().equals("news"), () -> null)
                    .randomize(UUID.class, () -> null)
                    .randomizationDepth(2)
    );

    @BeforeEach
    void init() {
        create(5 + random.nextInt(4000));
        news = create();
        newsRepository.flush();
    }

    @Test
    void test01() {
        System.out.println("=".repeat(100));

        newsRepository.flush();
        entityManager.clear();

        List<News> foundNews = newsRepository.findAllByFilter(
                NewsFilter.builder()
                        .messageFragment(news.getMessage().substring(0, 2))
                        .fileExtensions(news.getFiles().stream().map(File::getExtension).collect(Collectors.toList()))
                        .divisionTypes(news.getDivisions().stream().map(Division::getType).collect(Collectors.toList()))
                        .readerRoles(news.getReaders().stream().map(Reader::getRole).collect(Collectors.toList()))
                        .build()
        );

        assertNotNull(foundNews);
        assertTrue(foundNews.size() >= 1);
        assertTrue(foundNews.contains(news));

        System.out.println("=".repeat(100));
        log.info("news size - {}", foundNews.size());

        /**unused! just example of proxy working*/
        foundNews.forEach(it -> {
            it.getFiles().forEach(File::getId);
            it.getDivisions().forEach(Division::getId);
            it.getReaders().forEach(Reader::getId);
        });
        System.out.println("=".repeat(100));
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

        return newsRepository.save(someNews);
    }

    private Collection<News> create(int count) {
        return Stream.generate(this::create)
                .limit(count)
                .collect(Collectors.toList());
    }
}