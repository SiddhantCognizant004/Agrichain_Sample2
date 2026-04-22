package com.cts.Registration_Service.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmerResponseDTO {
    private Long farmerId;
    private Long userId;
    private String name;
    private LocalDate dob;
    private String gender;
    private String address;
    private String contactInfo;
    private String landDetails;
    private String status;
}