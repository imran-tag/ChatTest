package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;

import java.util.*;

public class TestDaoMem implements ITestDao {
    private final Map<Long, ChatTest> store = Collections.synchronizedMap(new TreeMap<>());

    public void persist(ChatTest test) {
        store.put(test.getId(), test);
    }

    public void remove(long id) {
        ChatTest test = store.get(id);
        if (test != null) {
            test.getStep().clear();
        }
        store.remove(id);
    }

    public ChatTest find(long id) {
        return store.get(id);
    }

    public Collection<ChatTest> findAll() {
        return store.values();
    }

    public long count() {
        return store.size();
    }

    public void addStep(long testId, ChatStep step) {
        ChatTest test = store.get(testId);
        if (test == null) {
            System.out.println("Warning: Tried to add step to non-existent test ID: " + testId);
            return;
        }

        long id = 0;
        List<ChatStep> steps = test.getStep();
        if (!steps.isEmpty()) {
            ChatStep lastStep = steps.get(steps.size() - 1);
            if (lastStep.getId() != null) {
                id = lastStep.getId() + 1;
            }
        }
        step.setId(id);
        test.addStep(step);
    }

    public List<ChatStep> findSteps(long testId) {
        ChatTest test = store.get(testId);
        if (test != null) {
            return test.getStep();
        }
        return null;
    }

    public void removeStep(long testId, long stepId) {
        ChatTest test = store.get(testId);
        if (test != null) {
            test.removeStep(stepId);
        }
    }
}
