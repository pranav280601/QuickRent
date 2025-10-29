package com.shounoop.carrentalspring.controller;

import com.shounoop.carrentalspring.services.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ml")
public class MLController {

    @Autowired
    private MLService mlService;

    @PostMapping("/train")
    public String trainModel() {
        return mlService.trainModel();
    }

    @PostMapping("/recommend")
    public String recommendCars(@RequestBody Map<String, Object> request) {
        return mlService.getRecommendations(request);
    }
}

