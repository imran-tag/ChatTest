package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.TestBis;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTestDao {
    @Test
    void retrievesAllTestsWhenListIsEmpty() {
        TestDao testDao = new TestDao();
        assertTrue(testDao.getAllTests().isEmpty());
    }

    @Test
    void retrievesAllTestsWhenListHasElements() {
        TestDao testDao = new TestDao();
        testDao.addTest(new TestBis("1", "Test 1"));
        testDao.addTest(new TestBis("2", "Test 2"));
        assertEquals(2, testDao.getAllTests().size());
    }

    @Test
    void retrievesTestByIdWhenIdExists() {
        TestDao testDao = new TestDao();
        TestBis test = new TestBis("1", "Test 1");
        testDao.addTest(test);
        assertTrue(testDao.getTestById("1").isPresent());
        assertEquals(test, testDao.getTestById("1").get());
    }

    @Test
    void retrievesTestByIdWhenIdDoesNotExist() {
        TestDao testDao = new TestDao();
        assertFalse(testDao.getTestById("nonexistent").isPresent());
    }

    @Test
    void addsTestSuccessfully() {
        TestDao testDao = new TestDao();
        TestBis test = new TestBis("1", "Test 1");
        testDao.addTest(test);
        assertEquals(1, testDao.getAllTests().size());
        assertEquals(test, testDao.getAllTests().get(0));
    }
}
