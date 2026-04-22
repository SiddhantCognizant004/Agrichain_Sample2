//package com.cts.agrichain.subsidy.controller;
//
//import com.cts.agrichain.subsidy.entity.SubsidyProgram;
//import com.cts.agrichain.subsidy.enums.SubsidyStatus;
//import com.cts.agrichain.subsidy.service.SubsidyProgramService;
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
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(SubsidyProgramController.class)
//class SubsidyProgramControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private SubsidyProgramService subsidyProgramService;
//
//    private ObjectMapper objectMapper;
//    private SubsidyProgram program;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//
//        program = new SubsidyProgram();
//        program.setProgramID(1);
//        program.setTitle("Crop Support 2025");
//        program.setDescription("Annual crop subsidy");
//        program.setAllottedBudget(50000.0);
//        program.setConsumedBudget(0.0);
//        program.setSubsidyStatus(SubsidyStatus.PENDING);
//        program.setStartDate(LocalDate.of(2025, 1, 1));
//        program.setEndDate(LocalDate.of(2025, 12, 31));
//    }
//
//    @Test
//    void create_Returns200AndProgram() throws Exception {
//        when(subsidyProgramService.create(any(SubsidyProgram.class))).thenReturn(program);
//
//        mockMvc.perform(post("/subsidy-programs")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(program)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.programID").value(1))
//                .andExpect(jsonPath("$.title").value("Crop Support 2025"))
//                .andExpect(jsonPath("$.subsidyStatus").value("PENDING"));
//    }
//
//    @Test
//    void getById_Returns200AndProgram() throws Exception {
//        when(subsidyProgramService.findById(1)).thenReturn(program);
//
//        mockMvc.perform(get("/subsidy-programs/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.programID").value(1))
//                .andExpect(jsonPath("$.title").value("Crop Support 2025"));
//    }
//
//    @Test
//    void getAll_Returns200AndList() throws Exception {
//        when(subsidyProgramService.getAll()).thenReturn(List.of(program));
//
//        mockMvc.perform(get("/subsidy-programs"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void update_Returns200AndUpdatedProgram() throws Exception {
//        SubsidyProgram updated = new SubsidyProgram();
//        updated.setTitle("Updated Program");
//        updated.setAllottedBudget(80000.0);
//        updated.setConsumedBudget(0.0);
//        updated.setSubsidyStatus(SubsidyStatus.PENDING);
//        updated.setStartDate(LocalDate.of(2026, 1, 1));
//        updated.setEndDate(LocalDate.of(2026, 12, 31));
//
//        when(subsidyProgramService.update(eq(1), any(SubsidyProgram.class))).thenReturn(updated);
//
//        mockMvc.perform(put("/subsidy-programs/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updated)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Updated Program"))
//                .andExpect(jsonPath("$.allottedBudget").value(80000.0));
//    }
//
//    @Test
//    void delete_Returns200AndMessage() throws Exception {
//        doNothing().when(subsidyProgramService).delete(1);
//
//        mockMvc.perform(delete("/subsidy-programs/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Subsidy Program deleted successfully"));
//    }
//
//    @Test
//    void getByTitle_Returns200AndList() throws Exception {
//        when(subsidyProgramService.getProgramsByTitle("Crop Support 2025")).thenReturn(List.of(program));
//
//        mockMvc.perform(get("/subsidy-programs/title/Crop Support 2025"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].title").value("Crop Support 2025"));
//    }
//
//    @Test
//    void getByAllottedBudget_Returns200AndList() throws Exception {
//        when(subsidyProgramService.getByAllottedBudget(50000.0)).thenReturn(List.of(program));
//
//        mockMvc.perform(get("/subsidy-programs/allotted-budget/50000.0"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].allottedBudget").value(50000.0));
//    }
//}