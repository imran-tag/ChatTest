package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFactoryMemTest {
    private TestDaoMem testDaoMem;

    @BeforeEach
    public void setUp() {
        testDaoMem = new TestDaoMem();
    }

    @Test
    public void testPersistAndFind() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        testDaoMem.persist(test);
        assertEquals(test, testDaoMem.find(1L));
    }

    @Test
    public void testRemoveExistingTest() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        testDaoMem.persist(test);
        testDaoMem.remove(1L);
        assertNull(testDaoMem.find(1L));
    }

    @Test
    public void testFindAll() {
        assertTrue(testDaoMem.findAll().isEmpty());
        ChatTest test = new ChatTest();
        test.setId(1L);
        testDaoMem.persist(test);
        Collection<ChatTest> allTests = testDaoMem.findAll();
        assertEquals(1, allTests.size());
    }

    @Test
    public void testCount() {
        assertEquals(0, testDaoMem.count());
        ChatTest test = new ChatTest();
        test.setId(1L);
        testDaoMem.persist(test);
        assertEquals(1, testDaoMem.count());
    }

    @Test
    public void testAddStep() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        testDaoMem.persist(test);
        ChatStep step = new ChatStep();
        testDaoMem.addStep(1L, step);
        assertEquals(1, testDaoMem.findSteps(1L).size());
    }

    @Test
    public void testFindSteps() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        testDaoMem.persist(test);
        assertNotNull(testDaoMem.findSteps(1L));
    }

    @Test
    public void testRemoveStep() {
        ChatTest test = new ChatTest();
        test.setId(1L);
        ChatStep step = new ChatStep();
        step.setId(0L);
        test.addStep(step);
        testDaoMem.persist(test);
        testDaoMem.removeStep(1L, 0L);
        assertTrue(testDaoMem.findSteps(1L).isEmpty());
    }
}
