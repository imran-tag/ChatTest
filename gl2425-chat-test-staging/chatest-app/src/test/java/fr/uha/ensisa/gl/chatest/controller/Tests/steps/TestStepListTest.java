package fr.uha.ensisa.gl.chatest.controller.Tests.steps;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.controller.TestStepList;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestStepListTest {
    private final long TEST_ID = 1L;
    private final long STEP_ID = 2L;
    @Mock
    private IDaoFactory daoFactory;
    @Mock
    private ITestDao testDao;
    @InjectMocks
    private TestStepList testStepList;
    private ChatTest testData;
    private List<ChatStep> steps;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        // Create test data
        testData = new ChatTest();
        testData.setId(TEST_ID);
        testData.setName("Test Name");
        testData.setDescription("Test Description");

        // Create steps
        steps = new ArrayList<>();
        ChatStep step1 = new ChatStep();
        step1.setId(STEP_ID);
        step1.setName("Step 1 name");
        step1.setStep("Step 1 description");
        steps.add(step1);

        when(testDao.find(TEST_ID)).thenReturn(testData);
        when(testDao.findSteps(TEST_ID)).thenReturn(steps);
    }

    @Test
    public void testListSteps() {
        ModelAndView modelAndView = testStepList.create(TEST_ID);

        assertEquals("test/steps/list", modelAndView.getViewName());
        assertEquals(steps, modelAndView.getModel().get("steps"));
        assertEquals("Test Name", modelAndView.getModel().get("name"));
        verify(testDao).findSteps(TEST_ID);
        verify(testDao).find(TEST_ID);
    }

    @Test
    public void testCreateStep() {
        // Test parameters
        String stepName = "New Step";
        String stepDescription = "New Description";

        // Call the POST method
        ModelAndView modelAndView = testStepList.create(TEST_ID, stepName, stepDescription);

        // Verify redirect view
        assertEquals("redirect:/test/steps/list/" + TEST_ID, modelAndView.getViewName());

        // Verify the step was created with correct properties
        verify(testDao).addStep(eq(TEST_ID), argThat(step ->
                step.getName().equals(stepName)
        ));
    }

    @Test
    public void testRemoveStep_Success() {
        ModelAndView modelAndView = testStepList.remove(TEST_ID, STEP_ID);

        assertEquals("redirect:/test/steps/list/" + TEST_ID, modelAndView.getViewName());
        verify(testDao).removeStep(TEST_ID, STEP_ID);
    }

    @Test
    public void testRemoveStep_Exception() {
        doThrow(new RuntimeException("Test exception")).when(testDao).removeStep(TEST_ID, STEP_ID);

        ModelAndView modelAndView = testStepList.remove(TEST_ID, STEP_ID);

        assertEquals("redirect:/", modelAndView.getViewName());
        verify(testDao).removeStep(TEST_ID, STEP_ID);
    }
}
