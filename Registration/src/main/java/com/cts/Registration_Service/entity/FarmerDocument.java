package com.cts.Registration_Service.entity;

import com.cts.Registration_Service.enums.DocType;
import com.cts.Registration_Service.enums.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "farmer_document")
public class FarmerDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocType docType;

    private String fileName;
    private String filePath;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    @JsonBackReference // Stops infinite recursion by not serializing the parent Farmer back
    private Farmer farmer;
}