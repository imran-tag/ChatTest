package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.TestBis;
import org.springframework.stereotype.Repository; 

import java.util.*;

@Repository 
public class TestDao {
    private List<TestBis> tests = new ArrayList<>();

    public List<TestBis> getAllTests() {
        return tests;
    }

    public Optional<TestBis> getTestById(String id) {
        return tests.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public void addTest(TestBis test) {
        tests.add(test);
    }
}
