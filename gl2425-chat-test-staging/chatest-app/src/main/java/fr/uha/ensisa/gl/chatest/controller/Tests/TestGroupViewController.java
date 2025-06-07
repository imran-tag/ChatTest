package fr.uha.ensisa.gl.chatest.controller;

import fr.uha.ensisa.gl.chatest.TestGroup; 
import fr.uha.ensisa.gl.chatest.dao.chatest.TestGroupDao; 
import fr.uha.ensisa.gl.chatest.Test;

import fr.uha.ensisa.gl.chatest.Test; 
import fr.uha.ensisa.gl.chatest.dao.chatest.TestDao; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/test/group")
public class TestGroupViewController {
    @Autowired
    private TestGroupDao groupDao;

    @Autowired
    private TestDao testDao;

    // List all groups
    @GetMapping("/list")
    public String listGroups(Model model) {
        model.addAttribute("groups", groupDao.getAllGroups());
        return "group/list";
    }

    // Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("group", new TestGroup());
        return "group/create";
    }

    // Handle create group
    @PostMapping("/create")
    public String createGroup(@RequestParam String name,
                              @RequestParam String description,
                              Model model) {
        groupDao.addGroup(new TestGroup(name, description));
        return "redirect:/test/group/list";
    }

    // Show group details, including its tests and a way to add new tests
    @GetMapping("/edit/{id}")
    public String editGroup(@PathVariable String id, Model model) {
        Optional<TestGroup> optGroup = groupDao.getGroupById(id);
        if (optGroup.isPresent()) {
            TestGroup group = optGroup.get();
            model.addAttribute("group", group);
            // All tests in this group
            List<Test> groupTests = group.getTestIds().stream()
                .map(testId -> testDao.getTestById(testId).orElse(null))
                .filter(t -> t != null)
                .collect(Collectors.toList());
            model.addAttribute("groupTests", groupTests);

            // All tests not in this group (to add)
            List<Test> allTests = testDao.getAllTests();
            List<Test> availableTests = allTests.stream()
                .filter(t -> !group.getTestIds().contains(t.getId()))
                .collect(Collectors.toList());
            model.addAttribute("availableTests", availableTests);

            return "group/edit";
        } else {
            return "redirect:/test/group/list";
        }
    }

    // Add a test to a group
    @PostMapping("/addTest")
    public String addTestToGroup(@RequestParam String groupId, @RequestParam String testId) {
        groupDao.getGroupById(groupId).ifPresent(group -> group.addTestId(testId));
        return "redirect:/test/group/edit/" + groupId;
    }

    // Remove a test from a group
    @PostMapping("/removeTest")
    public String removeTestFromGroup(@RequestParam String groupId, @RequestParam String testId) {
        groupDao.getGroupById(groupId).ifPresent(group -> group.getTestIds().remove(testId));
        return "redirect:/test/group/edit/" + groupId;
    }
}
