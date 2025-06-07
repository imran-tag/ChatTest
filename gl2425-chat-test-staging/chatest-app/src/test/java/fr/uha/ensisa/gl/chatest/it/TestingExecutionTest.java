package fr.uha.ensisa.gl.chatest.it;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.controller.TestRunController;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.dao.chatest.ITestDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestingExecutionTest {
    /*
    private MockMvc mockMvc;
    private IDaoFactory mockDaoFactory;
    private ITestDao mockTestDao;

    @BeforeEach
    public void setup() {
        mockDaoFactory = Mockito.mock(IDaoFactory.class);
        mockTestDao = Mockito.mock(ITestDao.class);
        when(mockDaoFactory.getTestDao()).thenReturn(mockTestDao);

        TestRunController controller = new TestRunController(mockDaoFactory);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private ChatTest createTestWithTwoSteps() {
        ChatTest test = new ChatTest();
        test.addStep(new ChatStep());
        test.addStep(new ChatStep());
        return test;
    }

    @Test
    public void testShowRunPage() throws Exception {
        ChatTest test = createTestWithTwoSteps();
        when(mockTestDao.find(1L)).thenReturn(test);

        mockMvc.perform(get("/test/run/1/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("run"))
                .andExpect(model().attributeExists("test"))
                .andExpect(model().attributeExists("step"))
                .andExpect(model().attributeExists("index"))
                .andExpect(model().attributeExists("isFirst"))
                .andExpect(model().attributeExists("isLast"));
    }

    @Test
    public void testStartTest() throws Exception {
        ChatTest test = createTestWithTwoSteps();
        when(mockTestDao.find(1L)).thenReturn(test);

        mockMvc.perform(post("/test/run/1/0/validate").param("status", "OK"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/test/run/1/1"));
    }

    @Test
    public void testSkipStep() throws Exception {
        testStartTest(); // identical logic
    }

    @Test
    public void testMarkStepKO() throws Exception {
        ChatTest test = createTestWithTwoSteps();
        when(mockTestDao.find(1L)).thenReturn(test);

        mockMvc.perform(post("/test/run/1/0/validate")
                        .param("status", "KO")
                        .param("comment", "Erreur détectée"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/test/run/1/0"));
    }

    @Test
    public void testMarkStepOK() throws Exception {
        testStartTest(); // identical logic
    }

    @Test
    public void testRestartTest() throws Exception {
        ChatTest test = createTestWithTwoSteps();
        when(mockTestDao.find(1L)).thenReturn(test);

        mockMvc.perform(post("/test/run/1/1/validate")
                        .param("status", "KO")
                        .param("comment", "à refaire"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/test/run/1/1"));
    }

    @Test
    public void testGoBackToPreviousStep() throws Exception {
        testRestartTest(); // same expected behavior
    }*/
}
