package fr.uha.ensisa.gl.chatest.controller;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.TestExecution;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestExecutionDao;
import fr.uha.ensisa.gl.chatest.dao.chatest.TestDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test/history")
public class TestExecutionController {

    private final IDaoFactory daoFactory;

    public TestExecutionController(IDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @GetMapping
    public String showHistory(Model model) {
        // Get all test executions
        List<TestExecution> executions = new ArrayList<>(daoFactory.getTestExecutionDao().findAll());
        
        // Sort them: failed first, then by date (newest first)
        executions.sort((e1, e2) -> {
            // First compare by status (FAILED before PASSED)
            if ("FAILED".equals(e1.getStatus()) && !"FAILED".equals(e2.getStatus())) {
                return -1; // e1 comes first
            } else if (!"FAILED".equals(e1.getStatus()) && "FAILED".equals(e2.getStatus())) {
                return 1; // e2 comes first
            } else {
                // If statuses are the same, compare by date (newest first)
                return e2.getExecutionDate().compareTo(e1.getExecutionDate());
            }
        });

        model.addAttribute("executions", executions);
        return "test/history";
    }

    @GetMapping("/{id}")
    public String viewExecution(@PathVariable("id") long executionId, Model model) {
        ITestExecutionDao testExecutionDao = daoFactory.getTestExecutionDao();
        if (testExecutionDao == null) {
            return "redirect:/test/history";
        }
        TestExecution execution = testExecutionDao.find(executionId);

        // Get the test details
        ITestDao testDao = daoFactory.getTestDao();
        if (testDao == null) {
            return "redirect:/test/history";
        }
        ChatTest test = testDao.find(execution.getTestId());
        
        // If the execution doesn't have step results (older executions before this update),
        // display a message to the user
        if (execution.getStepResults() == null || execution.getStepResults().isEmpty()) {
            model.addAttribute("noStepResults", true);
        } else {
            // Create a map for easy lookup of step results by step ID
            Map<Long, TestExecution.StepResult> stepResultMap = new HashMap<>();
            for (TestExecution.StepResult result : execution.getStepResults()) {
                if (result.getStepId() != null) {
                    stepResultMap.put(result.getStepId(), result);
                }
            }
            model.addAttribute("stepResultMap", stepResultMap);
        }
        
        model.addAttribute("execution", execution);
        model.addAttribute("test", test);
        return "test/execution-details";
    }

    @GetMapping("/test/{id}")
    public String viewTestExecutions(@PathVariable("id") long testId, Model model) {
        ChatTest test = daoFactory.getTestDao().find(testId);
        if (test == null) {
            return "redirect:/test/history";
        }
        
        // Get all executions for this test
        List<TestExecution> executions = new ArrayList<>(daoFactory.getTestExecutionDao().findByTestId(testId));
        
        // Sort by status (FAILED before PASSED) and date (newest first)
        executions.sort((e1, e2) -> {
            // First compare by status (FAILED before PASSED)
            if ("FAILED".equals(e1.getStatus()) && !"FAILED".equals(e2.getStatus())) {
                return -1; // e1 comes first
            } else if (!"FAILED".equals(e1.getStatus()) && "FAILED".equals(e2.getStatus())) {
                return 1; // e2 comes first
            } else {
                // If statuses are the same, compare by date (newest first)
                return e2.getExecutionDate().compareTo(e1.getExecutionDate());
            }
        });
        
        model.addAttribute("test", test);
        model.addAttribute("executions", executions);
        return "test/test-history";
    }
}