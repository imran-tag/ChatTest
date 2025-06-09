package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.TestExecution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTestExecutionDaoMem {
    @Test
    void persistsExecutionWithGeneratedId() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        TestExecution execution = new TestExecution(null, "status", "OK");
        dao.persist(execution);
        assertNotNull(execution.getId());
        assertEquals(execution, dao.find(execution.getId()));
    }


    @Test
    void removesExecutionByIdSuccessfully() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        TestExecution execution = new TestExecution(null, "status", "OK");
        dao.persist(execution);
        dao.remove(execution.getId());
        assertNull(dao.find(execution.getId()));
    }

    @Test
    void findsExecutionByIdWhenExists() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        TestExecution execution = new TestExecution(null, "status", "OK");
        dao.persist(execution);
        assertEquals(execution, dao.find(execution.getId()));
    }

    @Test
    void findsExecutionByIdWhenNotExists() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        assertNull(dao.find(999L));
    }

    @Test
    void retrievesAllExecutionsSuccessfully() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        dao.persist(new TestExecution(null, "status1", "OK"));
        dao.persist(new TestExecution(null, "status2", "KO"));
        assertEquals(2, dao.findAll().size());
    }

    @Test
    void retrievesExecutionsByStatusSuccessfully() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        dao.persist(new TestExecution(null, "status1", "OK"));
        dao.persist(new TestExecution(null, "status2", "KO"));
        assertEquals(1, dao.findByStatus("OK").size());
    }

    @Test
    void retrievesExecutionsByTestIdSuccessfully() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        dao.persist(new TestExecution(1L, "status", "OK"));
        dao.persist(new TestExecution(2L, "status", "KO"));
        assertEquals(1, dao.findByTestId(1L).size());
    }

    @Test
    void countsExecutionsSuccessfully() {
        TestExecutionDaoMem dao = new TestExecutionDaoMem();
        dao.persist(new TestExecution(null, "status", "OK"));
        dao.persist(new TestExecution(null, "status", "KO"));
        assertEquals(2, dao.count());
    }
}
