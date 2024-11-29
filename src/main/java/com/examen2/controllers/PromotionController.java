package com.examen2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.examen2.entities.Promotion;
import com.examen2.services.PromotionService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ')") // Solo usuarios con permiso READ pueden acceder
    public List<Promotion> getAllPromotions() {
        return promotionService.getAllPromotions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ')") // Solo usuarios con permiso READ pueden acceder
    public Promotion getPromotionById(@PathVariable Long id) {
        return promotionService.getPromotionById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public Promotion createPromotion(@Valid @RequestBody Promotion promotion) {
        return promotionService.createPromotion(promotion);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE')") // Solo usuarios con permiso UPDATE pueden acceder
    public Promotion updatePromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
        return promotionService.updatePromotion(id, promotion);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')") // Solo usuarios con permiso DELETE pueden acceder
    public String deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return "Promocion borrada correctamente";
    }
}
