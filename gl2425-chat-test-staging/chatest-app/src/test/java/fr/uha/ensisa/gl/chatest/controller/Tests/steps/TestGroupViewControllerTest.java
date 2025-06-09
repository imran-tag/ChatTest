package fr.uha.ensisa.gl.chatest.controller.Tests.steps;

import fr.uha.ensisa.gl.chatest.TestBis;
import fr.uha.ensisa.gl.chatest.TestGroup;
import fr.uha.ensisa.gl.chatest.controller.Tests.TestGroupViewController;
import fr.uha.ensisa.gl.chatest.dao.chatest.TestDao;
import fr.uha.ensisa.gl.chatest.dao.chatest.TestGroupDao;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class TestGroupViewControllerTest {
    private final TestGroupDao groupDao = mock(TestGroupDao.class);
    private final TestDao testDao = mock(TestDao.class);
    private final Model model = mock(Model.class);
    private final TestGroupViewController testGroupViewController = new TestGroupViewController(groupDao, testDao);

    @Test
    public void listGroups_ReturnsEmptyListWhenNoGroupsExist() {
        when(groupDao.getAllGroups()).thenReturn(List.of());

        String viewName = testGroupViewController.listGroups(model);

        assertEquals("group/list", viewName);
        verify(model).addAttribute("groups", List.of());
    }

    @Test
    public void showCreateForm_InitializesNewGroup() {
        String viewName = testGroupViewController.showCreateForm(model);

        assertEquals("group/create", viewName);
        verify(model).addAttribute(eq("group"), any(TestGroup.class));
    }

    @Test
    public void createGroup_ValidInput_AddsGroupAndRedirects() {
        String viewName = testGroupViewController.createGroup("Group Name", "Group Description", model);

        assertEquals("redirect:/test/group/list", viewName);
        verify(groupDao).addGroup(argThat(group -> group.getName().equals("Group Name") && group.getDescription().equals("Group Description")));
    }

    @Test
    public void editGroup_GroupExists_ReturnsGroupDetails() {
        TestGroup mockGroup = new TestGroup("Group 1", "Description 1");
        mockGroup.addTestId("Test1");
        when(groupDao.getGroupById("1")).thenReturn(Optional.of(mockGroup));
        when(testDao.getTestById("Test1")).thenReturn(Optional.of(new TestBis("Test1", "Test Name 1")));
        when(testDao.getAllTests()).thenReturn(List.of(new TestBis("Test1", "Test Name 1"), new TestBis("Test2", "Test Name 2")));

        String viewName = testGroupViewController.editGroup("1", model);

        assertEquals("group/edit", viewName);
        verify(model).addAttribute("group", mockGroup);
    }

    @Test
    public void editGroup_GroupDoesNotExist_RedirectsToList() {
        when(groupDao.getGroupById("999")).thenReturn(Optional.empty());

        String viewName = testGroupViewController.editGroup("999", model);

        assertEquals("redirect:/test/group/list", viewName);
    }

    @Test
    public void addTestToGroup_ValidGroupAndTest_AddsTestAndRedirects() {
        TestGroup mockGroup = new TestGroup("Group 1", "Description 1");
        when(groupDao.getGroupById("1")).thenReturn(Optional.of(mockGroup));

        String viewName = testGroupViewController.addTestToGroup("1", "Test1");

        assertEquals("redirect:/test/group/edit/1", viewName);
        assertTrue(mockGroup.getTestIds().contains("Test1"));
    }

    @Test
    public void removeTestFromGroup_ValidGroupAndTest_RemovesTestAndRedirects() {
        TestGroup mockGroup = new TestGroup("Group 1", "Description 1");
        mockGroup.addTestId("Test1");
        when(groupDao.getGroupById("1")).thenReturn(Optional.of(mockGroup));

        String viewName = testGroupViewController.removeTestFromGroup("1", "Test1");

        assertEquals("redirect:/test/group/edit/1", viewName);
        assertFalse(mockGroup.getTestIds().contains("Test1"));
    }
}
