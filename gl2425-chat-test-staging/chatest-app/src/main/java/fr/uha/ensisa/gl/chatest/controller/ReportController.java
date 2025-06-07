package fr.uha.ensisa.gl.chatest.controller;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final IDaoFactory daoFactory;

    public ReportController(IDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @GetMapping("/by-test")
    public String reportByTest(Model model) {
        Collection<ChatTest> tests = daoFactory.getTestDao().findAll();

        Map<Long, Map<String, Integer>> stats = new HashMap<>();

        for (ChatTest test : tests) {
            Map<String, Integer> result = new HashMap<>();
            result.put("OK", 0);
            result.put("KO", 0);
            result.put("UNTESTED", 0);

            for (ChatStep step : test.getStep()) {
                String status = step.getStatus();
                if (status == null || status.isEmpty()) {
                    result.put("UNTESTED", result.get("UNTESTED") + 1);
                } else if ("OK".equals(status)) {
                    result.put("OK", result.get("OK") + 1);
                } else if ("KO".equals(status)) {
                    result.put("KO", result.get("KO") + 1);
                }
            }

            stats.put(test.getId(), result);
        }

        model.addAttribute("stats", stats);
        return "report";
    }
}
