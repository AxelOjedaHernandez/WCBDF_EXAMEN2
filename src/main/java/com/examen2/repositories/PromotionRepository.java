package com.examen2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.examen2.entities.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
}

