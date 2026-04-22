package com.cts.Registration_Service.dto.response;

import com.cts.Registration_Service.enums.DocType;
import com.cts.Registration_Service.enums.VerificationStatus;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerDocumentResponseDTO {
    private Long documentId;
    private Long farmerId;
    private DocType docType;
    private String fileUri;
    private LocalDate uploadedDate;
    private VerificationStatus verificationStatus;
}