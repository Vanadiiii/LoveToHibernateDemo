package ru.dexsys.lovetohibernatedemo.domain.repository.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.DivisionType;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.NewsType;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.ReaderRole;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsFilter {
    private String messageFragment;
    private LocalDate from;
    private LocalDate to;
    private List<ReaderRole> readerRoles;
    private List<DivisionType> divisionTypes;
    private NewsType newsType;
}