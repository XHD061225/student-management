package com.example.studentmanagement.student;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {
    List<ClassGroup> findAllByOrderByNameAsc();
}
