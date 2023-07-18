package ru.comitagroup.workingwithfiles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.comitagroup.workingwithfiles.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
}
