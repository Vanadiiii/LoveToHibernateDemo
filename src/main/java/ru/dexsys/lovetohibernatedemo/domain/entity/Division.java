package ru.dexsys.lovetohibernatedemo.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.DivisionType;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Division {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "news_id")
    @ToString.Exclude
    private News news;

    private long number;

    @Enumerated(EnumType.STRING)
    private DivisionType type;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Division division = (Division) o;
//        return Objects.equals(id, division.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return 31;
//    }
}
