package ru.dexsys.lovetohibernatedemo.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.ReaderRole;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reader {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "news_id")
    @ToString.Exclude
    private News news;

    private String name;

    @Enumerated(EnumType.STRING)
    private ReaderRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reader reader = (Reader) o;
        return Objects.equals(id, reader.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
