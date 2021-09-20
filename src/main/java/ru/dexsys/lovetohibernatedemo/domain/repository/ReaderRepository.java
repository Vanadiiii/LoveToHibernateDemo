package ru.dexsys.lovetohibernatedemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dexsys.lovetohibernatedemo.domain.entity.Reader;

import java.util.UUID;

public interface ReaderRepository extends JpaRepository<Reader, UUID> {
}
