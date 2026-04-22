package com.cts.Registration_Service.service;

import com.cts.Registration_Service.dao.AuditLogRepo;
import com.cts.Registration_Service.dao.FarmerDocumentRepo;
import com.cts.Registration_Service.dao.FarmerRepo;
import com.cts.Registration_Service.entity.AuditLog;
import com.cts.Registration_Service.entity.Farmer;
import com.cts.Registration_Service.entity.FarmerDocument;
import com.cts.Registration_Service.enums.DocType;
import com.cts.Registration_Service.enums.VerificationStatus;
import com.cts.Registration_Service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class FarmerDocumentService {

    @Autowired
    private FarmerDocumentRepo documentRepo;

    @Autowired
    private FarmerRepo farmerRepo;

    @Autowired
    private AuditLogRepo auditLogRepo;

    private final String BASE_PATH = "C:/Registration_FarmerDocument/";

    @Transactional
    public FarmerDocument uploadPhysicalFile(Long farmerId, MultipartFile file, DocType docType) throws IOException {
        if (isGuestUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Guests are not permitted to upload documents.");
        }
        Farmer farmer = authorizeAndGetFarmer(farmerId);
        Path farmerFolder = Paths.get(BASE_PATH).resolve("farmer_" + farmerId);
        if (Files.notExists(farmerFolder)) {
            Files.createDirectories(farmerFolder);
        }
        String originalName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + (originalName != null ? originalName.replaceAll("\\s+", "_") : "doc");
        Path targetLocation = farmerFolder.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        FarmerDocument document = new FarmerDocument();
        document.setFarmer(farmer);
        document.setDocType(docType);
        document.setFileName(fileName);
        document.setFilePath(targetLocation.toAbsolutePath().toString());
        document.setVerificationStatus(VerificationStatus.PENDING);

        return documentRepo.save(document);
    }

    @Transactional(readOnly = true)
    public List<FarmerDocument> getDocumentsByFarmer(Long farmerId) {
        if (isGuestUser()) {
            // FIXED: Matches the new Repo method name
            return documentRepo.findByFarmer_FarmerIdAndDocType(farmerId, DocType.LAND_RECORD);
        }
        authorizeAndGetFarmer(farmerId);
        // FIXED: Matches the new Repo method name
        return documentRepo.findByFarmer_FarmerId(farmerId);
    }

    @Transactional(readOnly = true)
    public List<FarmerDocument> getAllDocuments() {
        if (!isStaffUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied.");
        }
        return documentRepo.findAll();
    }

    @Transactional
    public FarmerDocument verifyDocument(Long documentId, VerificationStatus status) {
        if (!isStaffUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied.");
        }
        FarmerDocument doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new UserNotFoundException("Document not found.", HttpStatus.NOT_FOUND));
        doc.setVerificationStatus(status);
        return documentRepo.save(doc);
    }

    private Farmer authorizeAndGetFarmer(Long farmerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();
        Farmer farmer = farmerRepo.findById(farmerId)
                .orElseThrow(() -> new UserNotFoundException("Farmer not found.", HttpStatus.NOT_FOUND));
        if (!isStaffUser() && !farmer.getEmail().equalsIgnoreCase(currentEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access.");
        }
        return farmer;
    }

    private boolean isStaffUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_OFFICER"));
    }

    private boolean isGuestUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GUEST"));
    }
}