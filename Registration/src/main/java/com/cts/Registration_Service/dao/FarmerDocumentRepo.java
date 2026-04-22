package com.cts.Registration_Service.dao;

import com.cts.Registration_Service.entity.FarmerDocument;
import com.cts.Registration_Service.enums.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FarmerDocumentRepo extends JpaRepository<FarmerDocument, Long> {

    // Corrected: Use underscore to navigate to nested farmerId
    List<FarmerDocument> findByFarmer_FarmerId(Long farmerId);

    // Corrected: This is the one currently causing your startup error
    List<FarmerDocument> findByFarmer_FarmerIdAndDocType(Long farmerId, DocType docType);
}