package fr.uha.ensisa.gl.chatest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestGroupTest {
    private TestGroup testGroup;
    @BeforeEach
    public void setUp() {
        testGroup = new TestGroup();
    }

    @Test
    @DisplayName("Test default constructor")
    public void testDefaultConstructor() {
        assertNotNull(testGroup.getId());
        assertNull(testGroup.getName());
        assertNull(testGroup.getDescription());
        assertNotNull(testGroup.getTestIds());
        assertTrue(testGroup.getTestIds().isEmpty());
    }

    @Test
    @DisplayName("Test parameterized constructor")
    public void testParameterizedConstructor() {
        String name = "Group Name";
        String description = "Group Description";
        TestGroup group = new TestGroup(name, description);

        assertNotNull(group.getId());
        assertEquals(name, group.getName());
        assertEquals(description, group.getDescription());
        assertNotNull(group.getTestIds());
        assertTrue(group.getTestIds().isEmpty());
    }

    @Test
    @DisplayName("Test id getter and setter")
    public void testId() {
        String id = "custom-id";
        testGroup.setId(id);
        assertEquals(id, testGroup.getId());
    }

    @Test
    @DisplayName("Test name getter and setter")
    public void testName() {
        String name = "Test Group Name";
        testGroup.setName(name);
        assertEquals(name, testGroup.getName());
    }

    @Test
    @DisplayName("Test description getter and setter")
    public void testDescription() {
        String description = "Test Group Description";
        testGroup.setDescription(description);
        assertEquals(description, testGroup.getDescription());
    }

    @Test
    @DisplayName("Test testIds getter and setter")
    public void testTestIds() {
        List<String> testIds = List.of("test1", "test2");
        testGroup.setTestIds(testIds);
        assertEquals(testIds, testGroup.getTestIds());
    }

    @Test
    @DisplayName("Test addTestId method")
    public void testAddTestId() {
        String testId = "test1";
        testGroup.addTestId(testId);
        assertTrue(testGroup.getTestIds().contains(testId));
    }

    @Test
    @DisplayName("Test toString method")
    public void testToString() {
        testGroup.setName("Group Name");
        testGroup.setDescription("Group Description");
        String result = testGroup.toString();
        assertTrue(result.contains("Group Name"));
        assertTrue(result.contains("Group Description"));
    }
}
