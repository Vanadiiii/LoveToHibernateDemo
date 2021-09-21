package ru.dexsys.lovetohibernatedemo.domain.repository.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.DivisionType;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.FileExtension;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.NewsType;
import ru.dexsys.lovetohibernatedemo.domain.entity.enums.ReaderRole;

import javax.validation.constraints.NotNull;
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
    private List<FileExtension> fileExtensions;
    private NewsType newsType;
    @NotNull
    private String readerName;
}
