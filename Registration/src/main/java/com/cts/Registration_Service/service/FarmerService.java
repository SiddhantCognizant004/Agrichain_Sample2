package com.cts.Registration_Service.service;

import com.cts.Registration_Service.client.UserClient;
import com.cts.Registration_Service.dao.FarmerRepo;
import com.cts.Registration_Service.dto.response.FarmerResponseDTO;
import com.cts.Registration_Service.entity.Farmer;
import com.cts.Registration_Service.enums.DocType;
import com.cts.Registration_Service.enums.FarmerStatus;
import com.cts.Registration_Service.exception.FarmerNotFoundException;
import com.cts.Registration_Service.exception.UserConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepo farmerRepo;

    @Autowired
    private UserClient userClient;

    public Farmer registerFarmer(Farmer farmer) {
        try {
            userClient.fetchUserById(farmer.getUsers().getId());
        } catch (Exception e) {
            throw new UserConflictException("Linked User ID not found in the System.");
        }

        boolean hasIdProof = farmer.getDocuments().stream()
                .anyMatch(doc -> doc.getDocType() == DocType.ID_PROOF);

        boolean hasLandRecord = farmer.getDocuments().stream()
                .anyMatch(doc -> doc.getDocType() == DocType.LAND_RECORD);

        if (!hasIdProof || !hasLandRecord) {
            throw new UserConflictException("Farmer must provide both ID Proof and Land Record.");
        }

        farmer.setStatus(FarmerStatus.ACTIVE);
        farmer.getDocuments().forEach(doc -> doc.setFarmer(farmer));
        return farmerRepo.save(farmer);
    }

    public FarmerResponseDTO mapToResponse(Farmer farmer) {
        FarmerResponseDTO response = new FarmerResponseDTO();
        response.setFarmerId(farmer.getFarmerId());
        response.setName(farmer.getName());
        response.setDob(farmer.getDob());

        // FIX: Convert Enum to String for the DTO
        if (farmer.getGender() != null) {
            response.setGender(farmer.getGender().name());
        }

        response.setAddress(farmer.getAddress());
        response.setLandDetails(farmer.getLandDetails());
        response.setContactInfo(farmer.getContactInfo());
        response.setStatus(farmer.getStatus() != null ? farmer.getStatus().name() : "ACTIVE");

        if (farmer.getUsers() != null) {
            response.setUserId(farmer.getUsers().getId());
        }
        return response;
    }

    public Farmer getFarmerById(Long farmerId) {
        return farmerRepo.findById(farmerId)
                .orElseThrow(() -> new FarmerNotFoundException(
                        "The requested Farmer profile (ID: " + farmerId + ") does not exist in our records or may have been removed."));
    }

    public List<Farmer> getAllFarmers() { return farmerRepo.findAll(); }
    public Farmer updateFarmer(Farmer farmer) { return farmerRepo.save(farmer); }
    public void deleteFarmer(Long farmerId) { farmerRepo.deleteById(farmerId); }
}