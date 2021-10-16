package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

import java.util.*;

@SpringBootTest
public class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @AfterEach
    public void cleanupDB() {
        this.userRoleRepository.deleteAll();
    }

    @Test
    public void testNameQueries() {
        // Setup some dummy users (of various subclasses)
        final OfflineMember joe = new OfflineMember();
        joe.setId(150);
        joe.setName("Joe Schmoe");
        this.userRoleRepository.save(joe);

        final Librarian john = new Librarian();
        john.setId(151);
        john.setName("John Doe");
        this.userRoleRepository.save(john);

        final OfflineMember bob = new OfflineMember();
        bob.setId(152);
        bob.setName("Bob");
        this.userRoleRepository.save(bob);

        // Query these dummy users
        List<UserRole> ret;
        ret = this.userRoleRepository.findByNameContaining("Bob");
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(152, ret.get(0).getId());

        ret = this.userRoleRepository.findByNameContaining("oe");
        Assertions.assertEquals(2, ret.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(150, 151)),
                new HashSet<>(Arrays.asList(ret.get(0).getId(), ret.get(1).getId())));
    }
}
