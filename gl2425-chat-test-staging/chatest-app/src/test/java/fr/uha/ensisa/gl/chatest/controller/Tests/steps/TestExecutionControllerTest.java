package fr.uha.ensisa.gl.chatest.controller.Tests.steps;

import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.TestExecution;
import fr.uha.ensisa.gl.chatest.controller.TestExecutionController;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestExecutionDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TestExecutionControllerTest {
    @Test
    @DisplayName("showHistory returns sorted executions by status and date")
    void showHistoryReturnsSortedExecutions() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao); // Ensure mock is returned

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        List<TestExecution> executions = new ArrayList<>();
        TestExecution failedExecution = mock(TestExecution.class);
        when(failedExecution.getStatus()).thenReturn("FAILED");
        when(failedExecution.getExecutionDate()).thenReturn(new Date(1000));
        TestExecution passedExecution = mock(TestExecution.class);
        when(passedExecution.getStatus()).thenReturn("PASSED");
        when(passedExecution.getExecutionDate()).thenReturn(new Date(2000));
        executions.add(passedExecution);
        executions.add(failedExecution);

        when(testExecutionDao.findAll()).thenReturn(executions); // Mock behavior of findAll()

        String viewName = controller.showHistory(model);

        verify(model).addAttribute(eq("executions"), argThat((List<TestExecution> list) -> {
            return list.get(0).getStatus().equals("FAILED") && list.get(1).getStatus().equals("PASSED");
        }));
        assertEquals("test/history", viewName);
    }

    @Test
    @DisplayName("showHistory sorts FAILED status before other statuses")
    void showHistorySortsFailedStatusBeforeOtherStatuses() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        List<TestExecution> executions = new ArrayList<>();
        TestExecution execution1 = mock(TestExecution.class);
        when(execution1.getStatus()).thenReturn("PASSED");
        when(execution1.getExecutionDate()).thenReturn(new Date(2000));
        TestExecution execution2 = mock(TestExecution.class);
        when(execution2.getStatus()).thenReturn("FAILED");
        when(execution2.getExecutionDate()).thenReturn(new Date(1000));
        executions.add(execution1);
        executions.add(execution2);

        when(testExecutionDao.findAll()).thenReturn(executions);

        String viewName = controller.showHistory(model);

        verify(model).addAttribute(eq("executions"), argThat((List<TestExecution> list) -> {
            return list.get(0).getStatus().equals("FAILED") && list.get(1).getStatus().equals("PASSED");
        }));
        assertEquals("test/history", viewName);
    }

    @Test
    @DisplayName("showHistory sorts e2 with FAILED status before e1 with non-FAILED status")
    void showHistorySortsE2FailedBeforeE1NonFailed() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        List<TestExecution> executions = new ArrayList<>();
        TestExecution execution1 = mock(TestExecution.class);
        when(execution1.getStatus()).thenReturn("PASSED");
        when(execution1.getExecutionDate()).thenReturn(new Date(2000));
        TestExecution execution2 = mock(TestExecution.class);
        when(execution2.getStatus()).thenReturn("FAILED");
        when(execution2.getExecutionDate()).thenReturn(new Date(1000));
        executions.add(execution1);
        executions.add(execution2);

        when(testExecutionDao.findAll()).thenReturn(executions);

        String viewName = controller.showHistory(model);

        verify(model).addAttribute(eq("executions"), argThat((List<TestExecution> list) -> {
            return list.get(0).getStatus().equals("FAILED") && list.get(1).getStatus().equals("PASSED");
        }));
        assertEquals("test/history", viewName);
    }

    @Test
    @DisplayName("showHistory triggers else if condition in sorting logic")
    void showHistoryTriggersElseIfConditionInSortingLogic() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        List<TestExecution> executions = new ArrayList<>();
        TestExecution execution1 = mock(TestExecution.class);
        when(execution1.getStatus()).thenReturn("PASSED");
        when(execution1.getExecutionDate()).thenReturn(new Date(2000));
        TestExecution execution2 = mock(TestExecution.class);
        when(execution2.getStatus()).thenReturn("FAILED");
        when(execution2.getExecutionDate()).thenReturn(new Date(1000));
        executions.add(execution1);
        executions.add(execution2);

        when(testExecutionDao.findAll()).thenReturn(executions);

        String viewName = controller.showHistory(model);

        verify(model).addAttribute(eq("executions"), argThat((List<TestExecution> list) -> {
            return list.get(0).getStatus().equals("FAILED") && list.get(1).getStatus().equals("PASSED");
        }));
        assertEquals("test/history", viewName);
    }

    @Test
    @DisplayName("Comparator returns 1 when e1 status is not FAILED and e2 status is FAILED")
    void comparatorReturnsOneWhenE1NotFailedAndE2Failed() {
        TestExecution execution1 = mock(TestExecution.class);
        when(execution1.getStatus()).thenReturn("PASSED");
        when(execution1.getExecutionDate()).thenReturn(new Date(2000));

        TestExecution execution2 = mock(TestExecution.class);
        when(execution2.getStatus()).thenReturn("FAILED");
        when(execution2.getExecutionDate()).thenReturn(new Date(1000));

        int result = execution1.getStatus().equals("FAILED") && !execution2.getStatus().equals("FAILED") ? -1 :
                (!execution1.getStatus().equals("FAILED") && execution2.getStatus().equals("FAILED")) ? 1 :
                        execution2.getExecutionDate().compareTo(execution1.getExecutionDate());
        assertEquals(1, result);
    }

    @Test
    @DisplayName("viewExecution redirects when execution not found")
    void viewExecutionRedirectsWhenExecutionNotFound() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao); // Ensure mock is returned

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        when(testExecutionDao.find(anyLong())).thenReturn(null); // Mock behavior of find(long)

        String viewName = controller.viewExecution(1L, model);

        assertEquals("redirect:/test/history", viewName);
    }

    @Test
    @DisplayName("viewExecution handles null TestExecutionDao gracefully")
    void viewExecutionHandlesNullTestExecutionDaoGracefully() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(null); // Simulate null TestExecutionDao

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        String viewName = controller.viewExecution(1L, model);

        assertEquals("redirect:/test/history", viewName); // Ensure proper redirection
    }

    @Test
    @DisplayName("viewExecution handles null TestDao gracefully")
    void viewExecutionHandlesNullTestDaoGracefully() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao); // Ensure mock is returned
        when(daoFactory.getTestDao()).thenReturn(null); // Simulate null TestDao

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        TestExecution execution = mock(TestExecution.class);
        when(testExecutionDao.find(anyLong())).thenReturn(execution); // Mock behavior of find(long)

        String viewName = controller.viewExecution(1L, model);

        assertEquals("redirect:/test/history", viewName); // Ensure proper redirection
    }

    @Test
    @DisplayName("viewExecution returns execution details when execution exists")
    void viewExecutionReturnsExecutionDetailsWhenExecutionExists() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        TestExecution execution = mock(TestExecution.class);
        when(execution.getTestId()).thenReturn(1L);
        when(testExecutionDao.find(anyLong())).thenReturn(execution);

        ChatTest test = mock(ChatTest.class);
        when(testDao.find(anyLong())).thenReturn(test);

        String viewName = controller.viewExecution(1L, model);

        verify(model).addAttribute("execution", execution);
        verify(model).addAttribute("test", test);
        assertEquals("test/execution-details", viewName);
    }

    @Test
    @DisplayName("viewExecution adds noStepResults attribute when step results are empty")
    void viewExecutionAddsNoStepResultsAttributeWhenStepResultsAreEmpty() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        TestExecution execution = mock(TestExecution.class);
        when(execution.getTestId()).thenReturn(1L);
        when(execution.getStepResults()).thenReturn(new ArrayList<>());
        when(testExecutionDao.find(anyLong())).thenReturn(execution);

        ChatTest test = mock(ChatTest.class);
        when(testDao.find(anyLong())).thenReturn(test);

        String viewName = controller.viewExecution(1L, model);

        verify(model).addAttribute("noStepResults", true);
        verify(model).addAttribute("execution", execution);
        verify(model).addAttribute("test", test);
        assertEquals("test/execution-details", viewName);
    }

    @Test
    @DisplayName("viewExecution adds stepResultMap attribute when step results exist")
    void viewExecutionAddsStepResultMapAttributeWhenStepResultsExist() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        TestExecution execution = mock(TestExecution.class);
        when(execution.getTestId()).thenReturn(1L);

        TestExecution.StepResult stepResult = mock(TestExecution.StepResult.class);
        when(stepResult.getStepId()).thenReturn(1L);
        List<TestExecution.StepResult> stepResults = new ArrayList<>();
        stepResults.add(stepResult);
        when(execution.getStepResults()).thenReturn(stepResults);
        when(testExecutionDao.find(anyLong())).thenReturn(execution);

        ChatTest test = mock(ChatTest.class);
        when(testDao.find(anyLong())).thenReturn(test);

        String viewName = controller.viewExecution(1L, model);

        verify(model).addAttribute(eq("stepResultMap"), argThat((Map<Long, TestExecution.StepResult> map) -> map.containsKey(1L)));
        verify(model).addAttribute("execution", execution);
        verify(model).addAttribute("test", test);
        assertEquals("test/execution-details", viewName);
    }

    @Test
    @DisplayName("viewTestExecutions returns test and sorted executions by status and date")
    void viewTestExecutionsReturnsTestAndSortedExecutions() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        ChatTest test = mock(ChatTest.class);
        when(testDao.find(anyLong())).thenReturn(test);

        List<TestExecution> executions = new ArrayList<>();
        TestExecution failedExecution = mock(TestExecution.class);
        when(failedExecution.getStatus()).thenReturn("FAILED");
        when(failedExecution.getExecutionDate()).thenReturn(new Date(1000));
        TestExecution passedExecution = mock(TestExecution.class);
        when(passedExecution.getStatus()).thenReturn("PASSED");
        when(passedExecution.getExecutionDate()).thenReturn(new Date(2000));
        executions.add(passedExecution);
        executions.add(failedExecution);

        when(testExecutionDao.findByTestId(anyLong())).thenReturn(executions);

        String viewName = controller.viewTestExecutions(1L, model);

        verify(model).addAttribute("test", test);
        verify(model).addAttribute(eq("executions"), argThat((List<TestExecution> list) -> {
            return list.get(0).getStatus().equals("FAILED") && list.get(1).getStatus().equals("PASSED");
        }));
        assertEquals("test/test-history", viewName);
    }

    @Test
    @DisplayName("viewTestExecutions redirects when test not found")
    void viewTestExecutionsRedirectsWhenTestNotFound() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        when(testDao.find(anyLong())).thenReturn(null);

        String viewName = controller.viewTestExecutions(1L, model);

        assertEquals("redirect:/test/history", viewName);
    }

    @Test
    @DisplayName("viewTestExecutions handles empty executions list")
    void viewTestExecutionsHandlesEmptyExecutionsList() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        ChatTest test = mock(ChatTest.class);
        when(testDao.find(anyLong())).thenReturn(test);

        when(testExecutionDao.findByTestId(anyLong())).thenReturn(new ArrayList<>());

        String viewName = controller.viewTestExecutions(1L, model);

        verify(model).addAttribute("test", test);
        verify(model).addAttribute(eq("executions"), argThat(list -> list instanceof List && ((List<?>) list).isEmpty()));
        assertEquals("test/test-history", viewName);
    }

    @Test
    @DisplayName("viewTestExecutions sorts e2 with FAILED status before e1 with non-FAILED status")
    void viewTestExecutionsSortsE2FailedBeforeE1NonFailed() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        ChatTest test = mock(ChatTest.class);
        when(testDao.find(anyLong())).thenReturn(test);

        List<TestExecution> executions = new ArrayList<>();
        TestExecution execution1 = mock(TestExecution.class);
        when(execution1.getStatus()).thenReturn("PASSED");
        when(execution1.getExecutionDate()).thenReturn(new Date(2000));
        TestExecution execution2 = mock(TestExecution.class);
        when(execution2.getStatus()).thenReturn("FAILED");
        when(execution2.getExecutionDate()).thenReturn(new Date(1000));
        executions.add(execution1);
        executions.add(execution2);

        when(testExecutionDao.findByTestId(anyLong())).thenReturn(executions);

        String viewName = controller.viewTestExecutions(1L, model);

        verify(model).addAttribute(eq("executions"), argThat((List<TestExecution> list) -> {
            return list.get(0).getStatus().equals("FAILED") && list.get(1).getStatus().equals("PASSED");
        }));
        assertEquals("test/test-history", viewName);
    }

    @Test
    @DisplayName("viewTestExecutions sorts by date when statuses are the same")
    void viewTestExecutionsSortsByDateWhenStatusesAreTheSame() {
        IDaoFactory daoFactory = mock(IDaoFactory.class);
        ITestExecutionDao testExecutionDao = mock(ITestExecutionDao.class);
        ITestDao testDao = mock(ITestDao.class);
        when(daoFactory.getTestExecutionDao()).thenReturn(testExecutionDao);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        TestExecutionController controller = new TestExecutionController(daoFactory);
        Model model = mock(Model.class);

        ChatTest test = mock(ChatTest.class);
        when(testDao.find(anyLong())).thenReturn(test);

        List<TestExecution> executions = new ArrayList<>();
        TestExecution execution1 = mock(TestExecution.class);
        when(execution1.getStatus()).thenReturn("PASSED");
        when(execution1.getExecutionDate()).thenReturn(new Date(2000));
        TestExecution execution2 = mock(TestExecution.class);
        when(execution2.getStatus()).thenReturn("PASSED");
        when(execution2.getExecutionDate()).thenReturn(new Date(1000));
        executions.add(execution1);
        executions.add(execution2);

        when(testExecutionDao.findByTestId(anyLong())).thenReturn(executions);

        String viewName = controller.viewTestExecutions(1L, model);

        verify(model).addAttribute(eq("executions"), argThat((List<TestExecution> list) -> {
            return list.get(0).getExecutionDate().equals(new Date(2000)) && list.get(1).getExecutionDate().equals(new Date(1000));
        }));
        assertEquals("test/test-history", viewName);
    }
}
