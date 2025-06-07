package fr.uha.ensisa.gl.chatest.dao.chatest;

import fr.uha.ensisa.gl.chatest.TestGroup;
import org.springframework.stereotype.Repository; 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TestGroupDao {
    private List<TestGroup> groups = new ArrayList<>();

    public void addGroup(TestGroup group) {
        groups.add(group);
    }

    public List<TestGroup> getAllGroups() {
        return groups;
    }

    public Optional<TestGroup> getGroupById(String id) {
        return groups.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    public void updateGroup(TestGroup updatedGroup) {
        getGroupById(updatedGroup.getId()).ifPresent(existingGroup -> {
            existingGroup.setName(updatedGroup.getName());
            existingGroup.setDescription(updatedGroup.getDescription());
        });
    }

    public boolean removeGroup(String id) {
        return groups.removeIf(g -> g.getId().equals(id));
    }
}
