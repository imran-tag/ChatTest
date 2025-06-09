package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.TestExecution;
import fr.uha.ensisa.gl.chatest.controller.TestRunController;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestExecutionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestRunControllerTest {
    private final long TEST_ID = 1L;
    @Mock
    private IDaoFactory daoFactory;
    @Mock
    private ITestDao testDao;
    @Mock
    private Model model;
    @InjectMocks
    private TestRunController testRunController;
    private ChatTest testWithSteps;

    ITestExecutionDao testExecutionDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        // Mock the TestExecutionDao
        testExecutionDao = mock(ITestExecutionDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);

        // Create a test with two steps
        testWithSteps = new ChatTest();
        testWithSteps.setId(TEST_ID);
        testWithSteps.setName("Test 1");
        testWithSteps.setDescription("Test description");

        ChatStep step1 = new ChatStep();
        step1.setStep("Step 1");
        step1.setContent("Step 1 content");
        step1.setComment("Step 1 comment");
        step1.setName("Step 1 name");

        ChatStep step2 = new ChatStep();
        step2.setStep("Step 2");
        step2.setContent("Step 2 content");
        step2.setComment("Step 2 comment");
        step2.setName("Step 2 name");

        testWithSteps.addStep(step1);
        testWithSteps.addStep(step2);

        when(testDao.find(TEST_ID)).thenReturn(testWithSteps);
    }

    @Test
    public void testRunTest_FirstStep() {
        String viewName = testRunController.runTest(TEST_ID, 0, model);

        assertEquals("run", viewName);
        verify(model).addAttribute("test", testWithSteps);
        verify(model).addAttribute("step", testWithSteps.getStep().get(0));
        verify(model).addAttribute("index", 0);
        verify(model).addAttribute("isFirst", true);
        verify(model).addAttribute("isLast", false);
    }

    @Test
    public void testRunTest_LastStep() {
        String viewName = testRunController.runTest(TEST_ID, 1, model);

        assertEquals("run", viewName);
        verify(model).addAttribute("isFirst", false);
        verify(model).addAttribute("isLast", true);
    }

    @Test
    public void testRunTest_InvalidTest() {
        when(testDao.find(99L)).thenReturn(null);

        String viewName = testRunController.runTest(99L, 0, model);

        assertEquals("redirect:/test/list", viewName);
    }

    @Test
    public void testRunTest_InvalidStepIndex() {
        String viewNameTooHigh = testRunController.runTest(TEST_ID, 10, model);
        String viewNameTooLow = testRunController.runTest(TEST_ID, -10, model);

        assertEquals("redirect:/test/list", viewNameTooHigh);
        assertEquals("redirect:/test/list", viewNameTooLow);
    }

    @Test
    public void testValidateStep_OK_NotLastStep() {
        String viewName = testRunController.validateStep(TEST_ID, 0, "OK", null);

        assertEquals("redirect:/test/run/" + TEST_ID + "/1", viewName);
        assertEquals("OK", testWithSteps.getStep().get(0).getStatus());
    }

    @Test
    public void testValidateStep_OK_LastStep() {
        String viewName = testRunController.validateStep(TEST_ID, 1, "OK", null);

        assertEquals("redirect:/test/history/test/" + TEST_ID, viewName);
        assertEquals("OK", testWithSteps.getStep().get(1).getStatus());
    }

    @Test
    public void testValidateStep_KO() {
        String comment = "Test failed";
        String viewName = testRunController.validateStep(TEST_ID, 0, "KO", comment);

        assertEquals("redirect:/test/history/test/" + TEST_ID, viewName);
        assertEquals("KO", testWithSteps.getStep().get(0).getStatus());
        assertEquals(comment, testWithSteps.getStep().get(0).getComment());
    }

    @Test
    public void testValidateStep_InvalidTest() {
        when(testDao.find(99L)).thenReturn(null);

        String viewName = testRunController.validateStep(99L, 0, "OK", null);

        assertEquals("redirect:/test/list", viewName);
    }

    @Test
    public void stepIndexValidateOutOfBounds() {
        String viewName = testRunController.validateStep(TEST_ID, 10, "OK", null);
        assertEquals("redirect:/test/list", viewName);

        viewName = testRunController.validateStep(TEST_ID, -1, "OK", null);
        assertEquals("redirect:/test/list", viewName);
    }

    @Test
    public void validateStep_EmptyCommentIgnored() {
        String viewName = testRunController.validateStep(TEST_ID, 0, "OK", "   ");

        assertEquals("redirect:/test/run/" + TEST_ID + "/1", viewName);
        assertEquals("OK", testWithSteps.getStep().get(0).getStatus());
        assertEquals("Step 1 comment", testWithSteps.getStep().get(0).getComment());
    }

    @Test
    public void validateStep_CurrentExecutionNotNull_AddsStepResult() {
        testRunController.runTest(TEST_ID, 0, model); // Initialize currentExecution
        String viewName = testRunController.validateStep(TEST_ID, 0, "OK", null);

        assertEquals("redirect:/test/run/" + TEST_ID + "/1", viewName);
        assertEquals("OK", testWithSteps.getStep().get(0).getStatus());
        assertNotNull(testRunController.currentExecution);
        assertFalse(testRunController.currentExecution.getStepResults().contains(testWithSteps.getStep().get(0)));
    }

    @Test
    public void validateStep_CurrentExecutionNotNull_FailedExecutionPersisted() {
        // Mock the find method to return a valid TestExecution object
        TestExecution mockExecution = new TestExecution();
        mockExecution.setId(1L);
        mockExecution.setStatus("FAILED");
        mockExecution.setComment("Failed at step 1: Step 1 name");
        when(testExecutionDao.find(anyLong())).thenReturn(mockExecution);

        testRunController.runTest(TEST_ID, 0, model); // Initialize currentExecution
        String viewName = testRunController.validateStep(TEST_ID, 0, "KO", "Step failed");

        assertEquals("redirect:/test/history/test/" + TEST_ID, viewName);
        assertNull(testRunController.currentExecution);
        //get last testExec from daoFactory
        TestExecution last = testExecutionDao.find(testExecutionDao.count()-1);
        assertEquals("FAILED", last.getStatus());
        assertEquals("Failed at step 1: Step 1 name", last.getComment());
        assertNull(testRunController.currentExecution);
    }

    @Test
    public void validateStep_CurrentExecutionNotNull_SuccessfulExecutionPersisted() {
        // Mock the find method to return a valid TestExecution object
        TestExecution mockExecution = new TestExecution();
        mockExecution.setId(1L);
        mockExecution.setStatus("PASSED");
        mockExecution.setComment("All steps completed successfully");
        when(testExecutionDao.find(anyLong())).thenReturn(mockExecution);

        testRunController.runTest(TEST_ID, 0, model); // Initialize currentExecution
        String viewName = testRunController.validateStep(TEST_ID, 1, "OK", null); // Validate last step

        assertEquals("redirect:/test/history/test/" + TEST_ID, viewName);
        assertNull(testRunController.currentExecution);

        // Retrieve the last persisted execution
        TestExecution last = testExecutionDao.find(testExecutionDao.count() - 1);
        assertEquals("PASSED", last.getStatus());
        assertEquals("All steps completed successfully", last.getComment());
    }
}
