package fr.uha.ensisa.gl.chatest.controller;
import fr.uha.ensisa.gl.chatest.Test;

import org.springframework.stereotype.Service;
import fr.uha.ensisa.gl.chatest.TestGroup; 
import fr.uha.ensisa.gl.chatest.dao.chatest.TestGroupDao; 

import java.util.List;

@Service
public class TestGroupService {
    private final TestGroupDao groupDao;

    public TestGroupService() {
        this.groupDao = new TestGroupDao();
    }

    public TestGroup createGroup(String name, String description) {
        TestGroup group = new TestGroup(name, description);
        groupDao.addGroup(group);
        return group;
    }

    public List<TestGroup> getAllGroups() {
        return groupDao.getAllGroups();
    }

    public TestGroup getGroupById(String id) {
        return groupDao.getGroupById(id).orElse(null);
    }

    public TestGroup updateGroup(TestGroup updatedGroup) {
        groupDao.updateGroup(updatedGroup);
        return updatedGroup;
    }

    public boolean deleteGroup(String id) {
        return groupDao.removeGroup(id);
    }
}
