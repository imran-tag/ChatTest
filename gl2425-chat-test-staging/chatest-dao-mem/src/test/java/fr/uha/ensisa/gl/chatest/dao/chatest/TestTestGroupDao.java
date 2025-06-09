package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.TestGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTestGroupDao {
    @Test
    void addsGroupSuccessfully() {
        TestGroupDao dao = new TestGroupDao();
        TestGroup group = new TestGroup("Group 1", "Description 1");
        dao.addGroup(group);
        assertEquals(1, dao.getAllGroups().size());
        assertEquals(group, dao.getAllGroups().get(0));
    }

    @Test
    void retrievesAllGroupsWhenListIsEmpty() {
        TestGroupDao dao = new TestGroupDao();
        assertTrue(dao.getAllGroups().isEmpty());
    }

    @Test
    void retrievesAllGroupsWhenListHasElements() {
        TestGroupDao dao = new TestGroupDao();
        dao.addGroup(new TestGroup("Group 1", "Description 1"));
        dao.addGroup(new TestGroup("Group 2", "Description 2"));
        assertEquals(2, dao.getAllGroups().size());
    }

    @Test
    void retrievesGroupByIdWhenIdExists() {
        TestGroupDao dao = new TestGroupDao();
        TestGroup group = new TestGroup("Group 1", "Description 1");
        dao.addGroup(group);
        assertTrue(dao.getGroupById(group.getId()).isPresent());
        assertEquals(group, dao.getGroupById(group.getId()).get());
    }

    @Test
    void retrievesGroupByIdWhenIdDoesNotExist() {
        TestGroupDao dao = new TestGroupDao();
        assertFalse(dao.getGroupById("nonexistent").isPresent());
    }

    @Test
    void updatesGroupSuccessfully() {
        TestGroupDao dao = new TestGroupDao();
        TestGroup group = new TestGroup("Group 1", "Description 1");
        dao.addGroup(group);
        group.setName("Updated Group");
        group.setDescription("Updated Description");
        dao.updateGroup(group);
        assertEquals("Updated Group", dao.getGroupById(group.getId()).get().getName());
        assertEquals("Updated Description", dao.getGroupById(group.getId()).get().getDescription());
    }

    @Test
    void removesGroupSuccessfully() {
        TestGroupDao dao = new TestGroupDao();
        TestGroup group = new TestGroup( "Group 1", "Description 1");
        dao.addGroup(group);
        assertTrue(dao.removeGroup(group.getId()));
        assertTrue(dao.getAllGroups().isEmpty());
    }

    @Test
    void failsToRemoveGroupWhenIdDoesNotExist() {
        TestGroupDao dao = new TestGroupDao();
        assertFalse(dao.removeGroup("0"));
    }
}
