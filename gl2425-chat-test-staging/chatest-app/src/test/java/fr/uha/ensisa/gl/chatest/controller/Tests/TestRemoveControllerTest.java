package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.controller.TestRemoveController;
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

public class TestRemoveControllerTest {

    @Mock
    private IDaoFactory daoFactory;

    @Mock
    private ITestDao testDao;

    @InjectMocks
    private TestRemoveController testRemoveController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(daoFactory.getTestDao()).thenReturn(testDao);
    }

    @Test
    public void testRemoveGet() {
        long testId = 1L;

        ModelAndView modelAndView = testRemoveController.create(testId);

        verify(testDao).remove(testId);
        assertEquals("redirect:/test/list", modelAndView.getViewName());
    }
}