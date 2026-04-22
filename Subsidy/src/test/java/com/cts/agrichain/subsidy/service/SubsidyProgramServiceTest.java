//package com.cts.agrichain.subsidy.service;
//
//import com.cts.agrichain.subsidy.dao.SubsidyProgramRepo;
//import com.cts.agrichain.subsidy.entity.SubsidyProgram;
//import com.cts.agrichain.subsidy.enums.SubsidyStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class SubsidyProgramServiceTest {
//
//    @Mock
//    private SubsidyProgramRepo subsidyProgramRepo;
//
//    @InjectMocks
//    private SubsidyProgramService subsidyProgramService;
//
//    private SubsidyProgram program;
//
//    @BeforeEach
//    void setUp() {
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
//    // ─── create ─────────────────────────────────────────────────────────────────
//
//    @Test
//    void create_SetsConsumedBudgetToZeroAndStatusToPending() {
//        SubsidyProgram input = new SubsidyProgram();
//        input.setTitle("New Program");
//        input.setAllottedBudget(20000.0);
//        input.setConsumedBudget(9999.0); // should be overwritten to 0
//        input.setSubsidyStatus(SubsidyStatus.VALIDATED); // should be overwritten to PENDING
//
//        when(subsidyProgramRepo.save(any(SubsidyProgram.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        SubsidyProgram result = subsidyProgramService.create(input);
//
//        assertEquals(0.0, result.getConsumedBudget());
//        assertEquals(SubsidyStatus.PENDING, result.getSubsidyStatus());
//        verify(subsidyProgramRepo).save(input);
//    }
//
//    // ─── findById ────────────────────────────────────────────────────────────────
//
//    @Test
//    void findById_Found_ReturnsProgram() {
//        when(subsidyProgramRepo.findById(1)).thenReturn(Optional.of(program));
//
//        SubsidyProgram result = subsidyProgramService.findById(1);
//
//        assertNotNull(result);
//        assertEquals("Crop Support 2025", result.getTitle());
//    }
//
//    @Test
//    void findById_NotFound_ReturnsNull() {
//        when(subsidyProgramRepo.findById(99)).thenReturn(Optional.empty());
//
//        SubsidyProgram result = subsidyProgramService.findById(99);
//
//        assertNull(result);
//    }
//
//    // ─── getAll ──────────────────────────────────────────────────────────────────
//
//    @Test
//    void getAll_ReturnsList() {
//        when(subsidyProgramRepo.findAll()).thenReturn(List.of(program));
//
//        List<SubsidyProgram> result = subsidyProgramService.getAll();
//
//        assertEquals(1, result.size());
//        assertEquals("Crop Support 2025", result.get(0).getTitle());
//    }
//
//    @Test
//    void getAll_ReturnsEmptyList() {
//        when(subsidyProgramRepo.findAll()).thenReturn(List.of());
//
//        List<SubsidyProgram> result = subsidyProgramService.getAll();
//
//        assertTrue(result.isEmpty());
//    }
//
//    // ─── update ──────────────────────────────────────────────────────────────────
//
//    @Test
//    void update_Found_UpdatesAllFields() {
//        SubsidyProgram updated = new SubsidyProgram();
//        updated.setTitle("Updated Title");
//        updated.setDescription("Updated desc");
//        updated.setAllottedBudget(99000.0);
//        updated.setConsumedBudget(5000.0);
//        updated.setSubsidyStatus(SubsidyStatus.VALIDATED);
//        updated.setStartDate(LocalDate.of(2026, 1, 1));
//        updated.setEndDate(LocalDate.of(2026, 12, 31));
//
//        when(subsidyProgramRepo.findById(1)).thenReturn(Optional.of(program));
//        when(subsidyProgramRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
//
//        SubsidyProgram result = subsidyProgramService.update(1, updated);
//
//        assertNotNull(result);
//        assertEquals("Updated Title", result.getTitle());
//        assertEquals("Updated desc", result.getDescription());
//        assertEquals(99000.0, result.getAllottedBudget());
//        assertEquals(SubsidyStatus.VALIDATED, result.getSubsidyStatus());
//    }
//
//    @Test
//    void update_NotFound_ReturnsNull() {
//        when(subsidyProgramRepo.findById(99)).thenReturn(Optional.empty());
//
//        SubsidyProgram result = subsidyProgramService.update(99, new SubsidyProgram());
//
//        assertNull(result);
//        verify(subsidyProgramRepo, never()).save(any());
//    }
//
//    // ─── delete ──────────────────────────────────────────────────────────────────
//
//    @Test
//    void delete_CallsDeleteById() {
//        doNothing().when(subsidyProgramRepo).deleteById(1);
//
//        subsidyProgramService.delete(1);
//
//        verify(subsidyProgramRepo).deleteById(1);
//    }
//
//    // ─── getProgramsByTitle ───────────────────────────────────────────────────────
//
//    @Test
//    void getProgramsByTitle_ReturnsList() {
//        when(subsidyProgramRepo.findByTitle("Crop Support 2025")).thenReturn(List.of(program));
//
//        List<SubsidyProgram> result = subsidyProgramService.getProgramsByTitle("Crop Support 2025");
//
//        assertEquals(1, result.size());
//        assertEquals("Crop Support 2025", result.get(0).getTitle());
//    }
//
//    @Test
//    void getProgramsByTitle_NoMatch_ReturnsEmpty() {
//        when(subsidyProgramRepo.findByTitle("Unknown")).thenReturn(List.of());
//
//        List<SubsidyProgram> result = subsidyProgramService.getProgramsByTitle("Unknown");
//
//        assertTrue(result.isEmpty());
//    }
//
//    // ─── getByAllottedBudget ─────────────────────────────────────────────────────
//
//    @Test
//    void getByAllottedBudget_ReturnsList() {
//        when(subsidyProgramRepo.findByAllottedBudget(50000.0)).thenReturn(List.of(program));
//
//        List<SubsidyProgram> result = subsidyProgramService.getByAllottedBudget(50000.0);
//
//        assertEquals(1, result.size());
//        assertEquals(50000.0, result.get(0).getAllottedBudget());
//    }
//
//    @Test
//    void getByAllottedBudget_NoMatch_ReturnsEmpty() {
//        when(subsidyProgramRepo.findByAllottedBudget(1.0)).thenReturn(List.of());
//
//        List<SubsidyProgram> result = subsidyProgramService.getByAllottedBudget(1.0);
//
//        assertTrue(result.isEmpty());
//    }
//}
