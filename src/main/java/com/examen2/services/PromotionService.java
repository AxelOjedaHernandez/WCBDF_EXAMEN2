package com.examen2.services;

import org.springframework.stereotype.Service;

import com.examen2.entities.Promotion;
import com.examen2.repositories.PromotionRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public Optional<Promotion> getPromotionById(Integer id) {
        return promotionRepository.findById(id);
    }

    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public Promotion updatePromotion(Integer id, Promotion promotion) {
        return promotionRepository.findById(id)
                .map(existing -> {
                    existing.setPromotionName(promotion.getPromotionName());
                    existing.setPromotionDescription(promotion.getPromotionDescription());
                    existing.setStartDate(promotion.getStartDate());
                    existing.setEndDate(promotion.getEndDate());
                    return promotionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    public void deletePromotion(Integer id) {
        promotionRepository.deleteById(id);
    }
}

