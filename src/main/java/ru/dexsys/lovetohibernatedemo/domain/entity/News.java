package ru.dexsys.lovetohibernatedemo.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.CollectionUtils;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.NewsType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class News {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private NewsType type;

    private String message;

    private boolean personal;

    @ToString.Exclude
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Reader> readers = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Division> divisions = new ArrayList<>();

    public void setFiles(List<File> files) {
        if (!CollectionUtils.isEmpty(files)) {
            this.files.clear();
            files.forEach(file -> file.setNews(this));
            this.files.addAll(files);
        }
    }

    public void setReaders(List<Reader> readers) {
        if (!CollectionUtils.isEmpty(readers)) {
            this.readers.clear();
            readers.forEach(reader -> reader.setNews(this));
            this.readers.addAll(readers);
        }
    }

    public void setDivisions(List<Division> divisions) {
        if (!CollectionUtils.isEmpty(divisions)) {
            this.divisions.clear();
            divisions.forEach(division -> division.setNews(this));
            this.divisions.addAll(divisions);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        News news = (News) o;
        return Objects.equals(id, news.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
