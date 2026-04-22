package com.cts.Registration_Service.dto.request;

import com.cts.Registration_Service.enums.DocType;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerRegistrationDTO {
    private Long userId;
    private String name;
    private LocalDate dob;
    private String email;
    private String contactInfo;
    private String gender;
    private String address;
    private String landDetails;
    private List<DocumentDTO> documents;

    @Getter
    @Setter
    public static class DocumentDTO {
        private DocType docType;
        private String fileUri;
    }
}