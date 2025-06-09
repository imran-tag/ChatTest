package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.controller.Tests.TestListController;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestEditControllerTest {
    private final Long TEST_ID = 1L;
    @Mock
    private IDaoFactory daoFactory;
    @Mock
    private ITestDao testDao;
    @InjectMocks
    private TestEditController testEditController;
    private ChatTest testData;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(daoFactory.getTestDao()).thenReturn(testDao);

        // Create test data
        testData = new ChatTest();
        testData.setId(TEST_ID);
        testData.setName("Test Name");
        testData.setDescription("Test Description");

        when(testDao.find(TEST_ID)).thenReturn(testData);
    }

    @Test
    public void testShowEditForm_ValidId() {
        ModelAndView modelAndView = testEditController.showEditForm(TEST_ID);

        assertEquals("test/edit", modelAndView.getViewName());
        assertEquals(testData, modelAndView.getModel().get("test"));
    }

    @Test
    public void testShowEditForm_InvalidId() {
        Long invalidId = 99L;
        when(testDao.find(invalidId)).thenReturn(null);

        ModelAndView modelAndView = testEditController.showEditForm(invalidId);

        // Assuming controller returns empty view or handles null case
        // Adjust this assertion based on expected behavior for invalid IDs
        assertEquals("test/edit", modelAndView.getViewName());
    }

    @Test
    public void testUpdateTest() {
        ChatTest updatedTest = new ChatTest();
        updatedTest.setId(TEST_ID);
        updatedTest.setName("Updated Name");
        updatedTest.setDescription("Updated Description");

        String viewName = testEditController.updateTest(updatedTest);

        verify(testDao).persist(updatedTest);
        assertEquals("redirect:/test/list", viewName);
    }
}
