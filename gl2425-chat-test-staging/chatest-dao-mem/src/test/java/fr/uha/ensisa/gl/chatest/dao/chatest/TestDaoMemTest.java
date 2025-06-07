package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TestDaoMemTest {

    private TestDaoMem dao;

    @BeforeEach
    public void setUp() {
        dao = new TestDaoMem();
    }

    @Test
    public void testPersistAndFind() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        dao.persist(test);
        assertEquals(test, dao.find(1L));
    }

    @Test
    public void testFindNonExistentTest() {
        assertNull(dao.find(999L));
    }

    @Test
    public void testFindAll() {
        assertTrue(dao.findAll().isEmpty());
        ChatTest test1 = new ChatTest();
        test1.setId(1L);
        dao.persist(test1);
        assertEquals(1, dao.findAll().size());
    }

    @Test
    public void testRemoveExistingTest() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        dao.persist(test);
        dao.remove(1L);
        assertNull(dao.find(1L));
    }

    @Test
    public void testRemoveNonExistentTest() {
        dao.remove(999L);
    }

    @Test
    public void testAddStepToExistingTest() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        dao.persist(test);
        ChatStep step = new ChatStep();
        step.setName("Step1");
        dao.addStep(1L, step);
        List<ChatStep> steps = dao.findSteps(1L);
        assertNotNull(steps);
        assertEquals(1, steps.size());
    }

    @Test
    public void testAddStepToNonExistentTest() {
        ChatStep step = new ChatStep();
        step.setName("Step1");
        dao.addStep(999L, step);
    }

    @Test
    public void testFindStepsForExistingTest() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        dao.persist(test);
        assertNotNull(dao.findSteps(1L));
    }

    @Test
    public void testFindStepsForNonExistentTest() {
        assertNull(dao.findSteps(999L));
    }

    @Test
    public void testRemoveStepFromExistingTest() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        ChatStep step = new ChatStep();
        step.setId(0L);
        test.addStep(step);
        dao.persist(test);
        dao.removeStep(1L, 0L);
        assertTrue(dao.findSteps(1L).isEmpty());
    }

    @Test
    public void testRemoveStepFromNonExistentTest() {
        dao.removeStep(999L, 0L);
    }

    @Test
    public void testCount() {
        assertEquals(0, dao.count());
        ChatTest test = new ChatTest();
        test.setId(1L);
        dao.persist(test);
        assertEquals(1, dao.count());
    }


    @Test
    public void testAddStepWhenStepsListAlreadyHasElements() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        ChatStep step1 = new ChatStep();
        step1.setId(1L);
        test.addStep(step1);
        dao.persist(test);

        ChatStep step2 = new ChatStep();
        step2.setId(2L);
        step2.setName("Step2");
        dao.addStep(1L, step2);

        List<ChatStep> steps = dao.findSteps(1L);
        assertEquals(2, steps.size());
    }


    @Test
    public void testRemoveStepFromTestWithNoSteps() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        dao.persist(test);
        dao.removeStep(1L, 0L);
    }
    @Test
    public void testAddStepWithLastStepIdNull() {
        ChatTest test = new ChatTest();
        test.setId(1L);

        ChatStep stepWithNullId = new ChatStep();

        test.addStep(stepWithNullId);
        dao.persist(test);

        ChatStep newStep = new ChatStep();
        dao.addStep(1L, newStep);

        List<ChatStep> steps = dao.findSteps(1L);
        assertTrue(steps.contains(newStep));
        assertEquals(0L, newStep.getId());
    }

}
