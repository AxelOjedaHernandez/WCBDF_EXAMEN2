package com.examen2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionsController {

    @GetMapping("/read")
    public String read() {
        return "Promotions: Listado de promociones";
    }

    @GetMapping("/create")
    public String create() {
        return "Promotions: Nueva promoción creada";
    }

    @GetMapping("/update")
    public String update() {
        return "Promotions: Promoción actualizada";
    }

    @GetMapping("/delete")
    public String delete() {
        return "Promotions: Promoción eliminada";
    }
}