package ru.dexsys.lovetohibernatedemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dexsys.lovetohibernatedemo.domain.entity.File;
import ru.dexsys.lovetohibernatedemo.domain.entity.News;

import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
}
