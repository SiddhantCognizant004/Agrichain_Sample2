package com.cts.Registration_Service.controller;

import com.cts.Registration_Service.entity.FarmerDocument;
import com.cts.Registration_Service.enums.DocType;
import com.cts.Registration_Service.enums.VerificationStatus;
import com.cts.Registration_Service.service.FarmerDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class FarmerDocumentController {

    @Autowired
    private FarmerDocumentService documentService;

    @PostMapping("/farmer/{farmerId}/upload")
    public ResponseEntity<FarmerDocument> uploadDocument(
            @PathVariable Long farmerId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("docType") DocType docType) throws IOException {

        FarmerDocument savedDoc = documentService.uploadPhysicalFile(farmerId, file, docType);
        return new ResponseEntity<>(savedDoc, HttpStatus.CREATED);
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<FarmerDocument>> getDocumentsByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(documentService.getDocumentsByFarmer(farmerId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FarmerDocument>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PatchMapping("/{documentId}/verify")
    public ResponseEntity<FarmerDocument> verifyDocument(
            @PathVariable Long documentId,
            @RequestParam("status") VerificationStatus status) {

        return ResponseEntity.ok(documentService.verifyDocument(documentId, status));
    }
}