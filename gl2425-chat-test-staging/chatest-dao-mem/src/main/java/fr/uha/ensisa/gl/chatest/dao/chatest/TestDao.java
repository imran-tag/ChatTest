package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.Test;
import org.springframework.stereotype.Repository; 

import java.util.*;

@Repository 
public class TestDao {
    private List<Test> tests = new ArrayList<>();

    public List<Test> getAllTests() {
        return tests;
    }

    public Optional<Test> getTestById(String id) {
        return tests.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public void addTest(Test test) {
        tests.add(test);
    }
}
