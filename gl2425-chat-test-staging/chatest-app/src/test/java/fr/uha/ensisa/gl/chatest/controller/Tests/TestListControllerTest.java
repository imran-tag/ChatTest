package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.controller.Tests.TestListController;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TestListControllerTest {

    @Mock
    private IDaoFactory daoFactory;

    @Mock
    private ITestDao testDao;

    @InjectMocks
    private TestListController testListController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(daoFactory.getTestDao()).thenReturn(testDao);
        when(testDao.findAll()).thenReturn(new ArrayList<>()); // Adjust as necessary
    }

    @Test
    public void testListGet() {
        ModelAndView modelAndView = testListController.create();

        assertEquals("test/list", modelAndView.getViewName());
        assertEquals(new ArrayList<>(), modelAndView.getModel().get("tests")); // Adjust as necessary
    }
}