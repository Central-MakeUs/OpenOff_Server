package com.example.openoff.domain.interest.domain.repository;

import com.example.openoff.domain.interest.domain.entity.UserInterestField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestRepository extends JpaRepository<UserInterestField, Long> {
}
