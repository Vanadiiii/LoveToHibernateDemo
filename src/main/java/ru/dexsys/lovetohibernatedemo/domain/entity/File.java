package ru.dexsys.lovetohibernatedemo.domain.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.FileExtension;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class File {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "news_id")
    @ToString.Exclude
    private News news;

    private String link;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        File file = (File) o;
//        return Objects.equals(id, file.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return 31;
//    }
}
