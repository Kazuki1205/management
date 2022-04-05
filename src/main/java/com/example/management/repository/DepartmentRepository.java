package com.example.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.management.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
