package fr.uha.ensisa.gl.chatest.dao.chatest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDaoFactory {

    private DaoFactoryMem daoFactoryMem;

    @BeforeEach
    public void setUp() {
        daoFactoryMem = new DaoFactoryMem();
    }

    @Test
    public void testGetTestDaoReturnsNonNullDao() {
        ITestDao testDao = daoFactoryMem.getTestDao();
        assertNotNull(testDao, "The returned ITestDao should not be null.");
    }

    @Test
    public void testGetTestDaoReturnsSameInstance() {
        ITestDao firstCall = daoFactoryMem.getTestDao();
        ITestDao secondCall = daoFactoryMem.getTestDao();
        assertSame(firstCall, secondCall, "The ITestDao instance should be the same across calls.");
    }
}
