package fr.uha.ensisa.gl.chatest.controller;
import fr.uha.ensisa.gl.chatest.dao.chatest.TestGroupDao;
import fr.uha.ensisa.gl.chatest.TestGroup;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class TestGroupController {

    @Autowired
    public TestGroupService groupService;

    @PostMapping
    public TestGroup createGroup(@RequestBody TestGroup group) {
        return groupService.createGroup(group.getName(), group.getDescription());
    }

    @GetMapping
    public List<TestGroup> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public TestGroup getGroupById(@PathVariable String id) {
        return groupService.getGroupById(id);
    }

    @PutMapping("/{id}")
    public TestGroup updateGroup(@PathVariable String id, @RequestBody TestGroup group) {
        group.setId(id);
        return groupService.updateGroup(group);
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable String id) {
        boolean deleted = groupService.deleteGroup(id);
        if (deleted) {
            return "Group deleted successfully";
        } else {
            return "Group not found";
        }
    }
}
