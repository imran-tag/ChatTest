package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.TestGroup;
import fr.uha.ensisa.gl.chatest.controller.TestGroupService;
import fr.uha.ensisa.gl.chatest.dao.chatest.TestGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class TestGroupServiceTest {
    @Mock
    private TestGroupDao groupDao;

    private TestGroupService testGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testGroupService = new TestGroupService(groupDao);
    }

    @Test
    @DisplayName("createGroup should create and return a new group with correct properties")
    void createGroupCreatesAndReturnsNewGroupWithCorrectProperties() {
        TestGroup group = new TestGroup("GroupName", "GroupDescription");
        doAnswer(invocation -> {
            TestGroup passedGroup = invocation.getArgument(0);
            passedGroup.setId("b3055f6a-d428-409c-8bb7-b15ef260f470"); // Set expected ID
            return null;
        }).when(groupDao).addGroup(any(TestGroup.class));

        TestGroup result = testGroupService.createGroup("GroupName", "GroupDescription");

        assertEquals("GroupName", result.getName());
        assertEquals("GroupDescription", result.getDescription());
        assertEquals("b3055f6a-d428-409c-8bb7-b15ef260f470", result.getId());
        verify(groupDao).addGroup(argThat(groupArg ->
                "GroupName".equals(groupArg.getName()) &&
                        "GroupDescription".equals(groupArg.getDescription()) &&
                        groupArg.getTestIds().isEmpty()
        ));
    }

    @Test
    @DisplayName("getAllGroups should return all groups")
    void getAllGroupsReturnsAllGroups() {
        TestGroup group1 = new TestGroup("Group1", "Description1");
        TestGroup group2 = new TestGroup("Group2", "Description2");
        when(groupDao.getAllGroups()).thenReturn(Arrays.asList(group1, group2));

        var result = testGroupService.getAllGroups();

        assertEquals(2, result.size());
        assertTrue(result.contains(group1));
        assertTrue(result.contains(group2));
    }

    @Test
    @DisplayName("getGroupById should return group if found")
    void getGroupByIdReturnsGroupIfFound() {
        TestGroup group = new TestGroup("GroupName", "GroupDescription");
        when(groupDao.getGroupById("1")).thenReturn(Optional.of(group));

        var result = testGroupService.getGroupById("1");

        assertNotNull(result);
        assertEquals("GroupName", result.getName());
        assertEquals("GroupDescription", result.getDescription());
    }

    @Test
    @DisplayName("getGroupById should return null if group not found")
    void getGroupByIdReturnsNullIfGroupNotFound() {
        when(groupDao.getGroupById("1")).thenReturn(Optional.empty());

        var result = testGroupService.getGroupById("1");

        assertNull(result);
    }

    @Test
    @DisplayName("updateGroup should update and return the group")
    void updateGroupUpdatesAndReturnsGroup() {
        TestGroup group = new TestGroup("UpdatedName", "UpdatedDescription");
        doNothing().when(groupDao).updateGroup(group);

        var result = testGroupService.updateGroup(group);

        assertEquals("UpdatedName", result.getName());
        assertEquals("UpdatedDescription", result.getDescription());
        verify(groupDao).updateGroup(group);
    }

    @Test
    @DisplayName("deleteGroup should return true if group is deleted")
    void deleteGroupReturnsTrueIfGroupDeleted() {
        when(groupDao.removeGroup("1")).thenReturn(true);

        var result = testGroupService.deleteGroup("1");

        assertTrue(result);
        verify(groupDao).removeGroup("1");
    }

    @Test
    @DisplayName("deleteGroup should return false if group is not deleted")
    void deleteGroupReturnsFalseIfGroupNotDeleted() {
        when(groupDao.removeGroup("1")).thenReturn(false);

        var result = testGroupService.deleteGroup("1");

        assertFalse(result);
        verify(groupDao).removeGroup("1");
    }

    @Test
    @DisplayName("default constructor should initialize groupDao")
    void defaultConstructorInitializesGroupDao() {
        TestGroupService testGroupService = new TestGroupService();

        assertNotNull(testGroupService.getGroupDao());
        assertTrue(testGroupService.getGroupDao() instanceof TestGroupDao);
    }
}
