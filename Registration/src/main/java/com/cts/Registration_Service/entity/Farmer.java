package com.cts.Registration_Service.entity;

import com.cts.Registration_Service.enums.FarmerGender;
import com.cts.Registration_Service.enums.FarmerStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "farmer")
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long farmerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    private String name;
    private LocalDate dob;
    private String email;
    private String contactInfo;

    @Enumerated(EnumType.STRING)
    private FarmerGender gender;
    private String address;
    private String landDetails;

    @Enumerated(EnumType.STRING)
    private FarmerStatus status;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // The forward part of the relationship
    private List<FarmerDocument> documents = new ArrayList<>();

    public void addDocument(FarmerDocument document) {
        documents.add(document);
        document.setFarmer(this);
    }
}