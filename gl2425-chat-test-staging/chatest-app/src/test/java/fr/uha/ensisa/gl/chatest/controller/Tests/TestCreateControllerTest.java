package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.controller.Tests.TestCreateController;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCreateControllerTest {

    @Mock
    private IDaoFactory daoFactory;

    @Mock
    private ITestDao testDao;

    @InjectMocks
    private TestCreateController testCreateController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(daoFactory.getTestDao()).thenReturn(this.testDao);
    }

    @Test
    public void testCreateGet() {
        ModelAndView modelAndView = testCreateController.create();

        assertEquals("test/create", modelAndView.getViewName());
    }

    @Test
    public void testCreatePost() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("Test Name");
        when(request.getParameter("description")).thenReturn("Test Description");

        ModelAndView modelAndView = testCreateController.create("Test Name", "Test Description", "st1", "uiui");
        assertEquals("redirect:/test/steps/list/0", modelAndView.getViewName());
    }
}