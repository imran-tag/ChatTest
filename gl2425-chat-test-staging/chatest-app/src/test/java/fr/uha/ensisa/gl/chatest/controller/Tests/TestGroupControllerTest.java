package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.TestGroup;
import fr.uha.ensisa.gl.chatest.controller.TestGroupController;
import fr.uha.ensisa.gl.chatest.controller.TestGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestGroupControllerTest {
    private TestGroupController controller;
    private TestGroupService groupService;

    @BeforeEach
    void setup() {
        groupService = mock(TestGroupService.class);
        controller = new TestGroupController();
        controller.groupService = groupService;
    }

    @Test
    void createGroupReturnsCreatedGroup() {
        TestGroup group = new TestGroup("Group1", "Description1");
        when(groupService.createGroup("Group1", "Description1")).thenReturn(group);

        TestGroup result = controller.createGroup(group);

        assertEquals("Group1", result.getName());
        assertEquals("Description1", result.getDescription());
    }

    @Test
    void getAllGroupsReturnsListOfGroups() {
        List<TestGroup> groups = Arrays.asList(
                new TestGroup("Group1", "Description1"),
                new TestGroup("Group2", "Description2")
        );
        when(groupService.getAllGroups()).thenReturn(groups);

        List<TestGroup> result = controller.getAllGroups();

        assertEquals(2, result.size());
        assertEquals("Group1", result.get(0).getName());
        assertEquals("Group2", result.get(1).getName());
    }

    @Test
    void getGroupByIdReturnsGroupIfExists() {
        TestGroup group = new TestGroup("Group1", "Description1");
        when(groupService.getGroupById("1")).thenReturn(group);

        TestGroup result = controller.getGroupById("1");

        assertEquals("Group1", result.getName());
        assertEquals("Description1", result.getDescription());
    }

    @Test
    void getGroupByIdReturnsNullIfNotFound() {
        when(groupService.getGroupById("999")).thenReturn(null);

        TestGroup result = controller.getGroupById("999");

        assertNull(result);
    }

    @Test
    void updateGroupReturnsUpdatedGroup() {
        TestGroup group = new TestGroup("Group1", "Updated Description");
        group.setId("1");
        when(groupService.updateGroup(group)).thenReturn(group);

        TestGroup result = controller.updateGroup("1", group);

        assertEquals("Group1", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void deleteGroupReturnsSuccessMessageIfDeleted() {
        when(groupService.deleteGroup("1")).thenReturn(true);

        String result = controller.deleteGroup("1");

        assertEquals("Group deleted successfully", result);
    }

    @Test
    void deleteGroupReturnsNotFoundMessageIfNotDeleted() {
        when(groupService.deleteGroup("999")).thenReturn(false);

        String result = controller.deleteGroup("999");

        assertEquals("Group not found", result);
    }
}
