package com.cts.Registration_Service.controller;

import com.cts.Registration_Service.dto.response.FarmerResponseDTO;
import com.cts.Registration_Service.entity.Farmer;
import com.cts.Registration_Service.service.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/farmers")
public class FarmerController {

    @Autowired
    private FarmerService farmerService;

    @PostMapping
    public ResponseEntity<FarmerResponseDTO> registerFarmer(@RequestBody Farmer farmer) {
        Farmer savedFarmer = farmerService.registerFarmer(farmer);
        return ResponseEntity.status(HttpStatus.CREATED).body(farmerService.mapToResponse(savedFarmer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmerResponseDTO> getFarmerById(@PathVariable Long id) {
        Farmer farmer = farmerService.getFarmerById(id);
        return ResponseEntity.ok(farmerService.mapToResponse(farmer));
    }

    @GetMapping
    public List<Farmer> getAllFarmers() { return farmerService.getAllFarmers(); }

    @PutMapping("/{id}")
    public Farmer updateFarmer(@PathVariable Long id, @RequestBody Farmer farmer) {
        farmer.setFarmerId(id);
        return farmerService.updateFarmer(farmer);
    }

    @DeleteMapping("/{id}")
    public void deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
    }

}