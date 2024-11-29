package com.examen2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.examen2.entities.Promotion;
import com.examen2.repositories.PromotionRepository;

import java.util.List;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    // Obtener todas las promociones
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    // Obtener una promoción por ID
    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElse(new Promotion()); // Devuelve una nueva instancia vacía si no se encuentra
    }
    

    // Crear una nueva promoción
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    // Actualizar una promoción existente
    public Promotion updatePromotion(Long id, Promotion promotion) { // Cambiar Integer a Long
        return promotionRepository.findById(id)
                .map(existing -> {
                    existing.setPromotionName(promotion.getPromotionName());
                    existing.setPromotionDescription(promotion.getPromotionDescription());
                    existing.setStartDate(promotion.getStartDate());
                    existing.setEndDate(promotion.getEndDate());
                    return promotionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
    }

    // Eliminar una promoción por ID
    public void deletePromotion(Long id) { // Cambiar Integer a Long
        promotionRepository.deleteById(id);
    }
}


