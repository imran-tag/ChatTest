package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.controller.ReportController;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReportControllerTest {
    @Mock
    private IDaoFactory daoFactory;

    @Mock
    private ITestDao testDao;

    @Mock
    private Model model;

    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(daoFactory.getTestDao()).thenReturn(testDao);
        reportController = new ReportController(daoFactory);
    }

    @Test
    @DisplayName("reportByTest should handle empty test collection")
    void reportByTestHandlesEmptyTestCollection() {
        when(testDao.findAll()).thenReturn(Collections.emptyList());

        String viewName = reportController.reportByTest(model);

        verify(model).addAttribute("stats", new HashMap<>());
        assertEquals("report", viewName);
    }

    @Test
    @DisplayName("reportByTest should calculate stats for tests with steps")
    void reportByTestCalculatesStatsForTestsWithSteps() {
        ChatStep step1 = mock(ChatStep.class);
        ChatStep step2 = mock(ChatStep.class);
        ChatStep step3 = mock(ChatStep.class);
        when(step1.getStatus()).thenReturn("OK");
        when(step2.getStatus()).thenReturn("KO");
        when(step3.getStatus()).thenReturn(null);

        ChatTest test = mock(ChatTest.class);
        when(test.getId()).thenReturn(1L);
        when(test.getStep()).thenReturn(Arrays.asList(step1, step2, step3));

        when(testDao.findAll()).thenReturn(Collections.singletonList(test));

        String viewName = reportController.reportByTest(model);

        HashMap<Long, HashMap<String, Integer>> expectedStats = new HashMap<>();
        HashMap<String, Integer> testStats = new HashMap<>();
        testStats.put("OK", 1);
        testStats.put("KO", 1);
        testStats.put("UNTESTED", 1);
        expectedStats.put(1L, testStats);

        verify(model).addAttribute("stats", expectedStats);
        assertEquals("report", viewName);
    }

    @Test
    @DisplayName("reportByTest should handle tests with no steps")
    void reportByTestHandlesTestsWithNoSteps() {
        ChatTest test = mock(ChatTest.class);
        when(test.getId()).thenReturn(2L);
        when(test.getStep()).thenReturn(Collections.emptyList());

        when(testDao.findAll()).thenReturn(Collections.singletonList(test));

        String viewName = reportController.reportByTest(model);

        HashMap<Long, HashMap<String, Integer>> expectedStats = new HashMap<>();
        HashMap<String, Integer> testStats = new HashMap<>();
        testStats.put("OK", 0);
        testStats.put("KO", 0);
        testStats.put("UNTESTED", 0);
        expectedStats.put(2L, testStats);

        verify(model).addAttribute("stats", expectedStats);
        assertEquals("report", viewName);
    }
}
