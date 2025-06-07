package fr.uha.ensisa.gl.chatest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestGroup {
    private String id;
    private String name;
    private String description;
    private List<String> testIds;

    public TestGroup() {
        this.id = UUID.randomUUID().toString();
        this.testIds = new ArrayList<>();
    }

    public TestGroup(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.testIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTestIds() {
        return testIds;
    }

    public void setTestIds(List<String> testIds) {
        this.testIds = testIds;
    }

    public void addTestId(String testId) {
        this.testIds.add(testId);
    }

    @Override
    public String toString() {
        return "TestGroup{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", testIds=" + testIds +
                '}';
    }
}
