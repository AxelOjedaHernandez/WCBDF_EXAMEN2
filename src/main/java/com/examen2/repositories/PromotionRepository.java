package com.examen2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.examen2.entities.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}


