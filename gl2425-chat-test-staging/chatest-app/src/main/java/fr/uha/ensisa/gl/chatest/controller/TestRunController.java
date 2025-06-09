package fr.uha.ensisa.gl.chatest.controller;

import java.util.Date;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.TestExecution;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/test/run")
public class TestRunController {

    private final IDaoFactory daoFactory;
    // Store the current test execution in progress
    public TestExecution currentExecution;

    public TestRunController(IDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @GetMapping("/{id}/{stepIndex}")
    public String runTest(
            @PathVariable("id") long testId,
            @PathVariable("stepIndex") int stepIndex,
            Model model) {

        ChatTest test = daoFactory.getTestDao().find(testId);
        if (test == null) {
            return "redirect:/test/list";
        }

        List<ChatStep> steps = test.getStep();

        if (stepIndex < 0 || stepIndex >= steps.size()) {
            return "redirect:/test/list";
        }

        // Start a new test execution when beginning at step 0
        if (stepIndex == 0) {
            currentExecution = new TestExecution();
            currentExecution.setTestId(testId);
            currentExecution.setTestName(test.getName());
            currentExecution.setExecutionDate(new Date());
            
            // Reset all step statuses for display purposes
            for (ChatStep step : steps) {
                step.setStatus(null);
                step.setComment(null);
            }
        }

        model.addAttribute("test", test);
        model.addAttribute("step", steps.get(stepIndex));
        model.addAttribute("index", stepIndex);
        model.addAttribute("isFirst", stepIndex == 0);
        model.addAttribute("isLast", stepIndex == steps.size() - 1);

        return "run";
    }

    @PostMapping("/{id}/{stepIndex}/validate")
    public String validateStep(
            @PathVariable("id") Long testId,
            @PathVariable("stepIndex") int stepIndex, 
            @RequestParam("status") String result,
            @RequestParam(value = "comment", required = false) String comment) {
        
        ChatTest test = daoFactory.getTestDao().find(testId);
        if (test == null) {
            return "redirect:/test/list";
        }
        
        if (stepIndex < 0 || stepIndex >= test.getStep().size()) {
            return "redirect:/test/list";
        }
        
        // Get the current step
        ChatStep currentStep = test.getStep().get(stepIndex);
        
        // Set the step status and comment
        currentStep.setStatus(result);
        if (comment != null && !comment.trim().isEmpty()) {
            currentStep.setComment(comment.trim());
        }
        
        // Add step result to the current execution
        if (currentExecution != null) {
            currentExecution.addStepResult(currentStep);
        }
        
        // Check if the step failed (KO)
        if ("KO".equals(result)) {
            // Step failed - finalize the test execution as FAILED and stop
            if (currentExecution != null) {
                currentExecution.setStatus("FAILED");
                currentExecution.setComment("Failed at step " + (stepIndex + 1) + ": " + currentStep.getName());
                
                // Persist the failed execution
                daoFactory.getTestExecutionDao().persist(currentExecution);
                
                // Clear the current execution
                currentExecution = null;
            }
            
            // Redirect to test history to show the failed result
            return "redirect:/test/history/test/" + testId;
        }
        
        // Step passed (OK)
        if (stepIndex == test.getStep().size() - 1) {
            // Last step and it passed - finalize the test execution as PASSED
            if (currentExecution != null) {
                currentExecution.setStatus("PASSED");
                currentExecution.setComment("All steps completed successfully");
                
                // Persist the successful execution
                daoFactory.getTestExecutionDao().persist(currentExecution);
                
                // Clear the current execution
                currentExecution = null;
            }
            
            // Redirect to test history to show the successful result
            return "redirect:/test/history/test/" + testId;
        } else {
            // Not the last step and it passed - continue to next step
            return "redirect:/test/run/" + testId + "/" + (stepIndex + 1);
        }
    }
}