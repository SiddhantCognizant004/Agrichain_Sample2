//package com.cts.agrichain.subsidy.controller;
//
//import com.cts.agrichain.subsidy.dto.DisbursementDTO;
//import com.cts.agrichain.subsidy.entity.Disbursement;
//import com.cts.agrichain.subsidy.entity.SubsidyProgram;
//import com.cts.agrichain.subsidy.enums.DisbursementStatus;
//import com.cts.agrichain.subsidy.service.DisbursementService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(DisbursementController.class)
//class DisbursementControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private DisbursementService disbursementService;
//
//    private ObjectMapper objectMapper;
//    private Disbursement disbursement;
//    private DisbursementDTO dto;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//
//        SubsidyProgram program = new SubsidyProgram();
//        program.setProgramID(1);
//        program.setTitle("Test Program");
//
//        disbursement = new Disbursement();
//        disbursement.setDisbursementID(1);
//        disbursement.setFarmerId(101L);
//        disbursement.setSubsidyProgram(program);
//        disbursement.setDisbursementAmount(2000.0);
//        disbursement.setDisbursementDate(LocalDate.now());
//        disbursement.setDisbursementStatus(DisbursementStatus.PENDING);
//
//        dto = new DisbursementDTO();
//        dto.setFarmerId(101L);
//        dto.setProgramId(1);
//        dto.setDisbursementAmount(2000.0);
//    }
//
//    @Test
//    void applyForSubsidy_Returns200AndDisbursement() throws Exception {
//        when(disbursementService.applyForSubsidy(any(DisbursementDTO.class))).thenReturn(disbursement);
//
//        mockMvc.perform(post("/disbursements")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.disbursementID").value(1))
//                .andExpect(jsonPath("$.farmerId").value(101))
//                .andExpect(jsonPath("$.disbursementStatus").value("PENDING"));
//    }
//
//    @Test
//    void getById_Returns200AndDisbursement() throws Exception {
//        when(disbursementService.getById(1)).thenReturn(disbursement);
//
//        mockMvc.perform(get("/disbursements/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.disbursementID").value(1));
//    }
//
//    @Test
//    void getAll_Returns200AndList() throws Exception {
//        when(disbursementService.getAll()).thenReturn(List.of(disbursement));
//
//        mockMvc.perform(get("/disbursements"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void reviewDisbursement_Returns200WithUpdatedStatus() throws Exception {
//        disbursement.setDisbursementStatus(DisbursementStatus.COMPLETED);
//        when(disbursementService.reviewDisbursement(1, DisbursementStatus.COMPLETED))
//                .thenReturn(disbursement);
//
//        mockMvc.perform(patch("/disbursements/1/review")
//                        .param("status", "COMPLETED"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.disbursementStatus").value("COMPLETED"));
//    }
//
//    @Test
//    void getByFarmer_Returns200AndList() throws Exception {
//        when(disbursementService.getByFarmerId(101L)).thenReturn(List.of(disbursement));
//
//        mockMvc.perform(get("/disbursements/farmer/101"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].farmerId").value(101));
//    }
//
//    @Test
//    void getByProgram_Returns200AndList() throws Exception {
//        when(disbursementService.getByProgramId(1)).thenReturn(List.of(disbursement));
//
//        mockMvc.perform(get("/disbursements/program/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//}