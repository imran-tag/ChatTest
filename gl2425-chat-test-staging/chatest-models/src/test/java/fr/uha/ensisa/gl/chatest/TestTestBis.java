package fr.uha.ensisa.gl.chatest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestTestBis {
    @Test
    void createsInstanceWithValidIdAndName() {
        TestBis testBis = new TestBis("1", "Sample TestBis");
        assertEquals("1", testBis.getId());
        assertEquals("Sample TestBis", testBis.getName());
    }

    @Test
    void updatesIdSuccessfully() {
        TestBis testBis = new TestBis();
        testBis.setId("123");
        assertEquals("123", testBis.getId());
    }

    @Test
    void updatesNameSuccessfully() {
        TestBis testBis = new TestBis();
        testBis.setName("Updated Name");
        assertEquals("Updated Name", testBis.getName());
    }

    @Test
    void handlesNullIdGracefully() {
        TestBis testBis = new TestBis();
        testBis.setId(null);
        assertNull(testBis.getId());
    }

    @Test
    void handlesNullNameGracefully() {
        TestBis testBis = new TestBis();
        testBis.setName(null);
        assertNull(testBis.getName());
    }

    @Test
    void handlesEmptyId() {
        TestBis testBis = new TestBis();
        testBis.setId("");
        assertEquals("", testBis.getId());
    }

    @Test
    void handlesEmptyName() {
        TestBis testBis = new TestBis();
        testBis.setName("");
        assertEquals("", testBis.getName());
    }
}
