package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.UserRoleRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @InjectMocks
    private UserRoleService userRoleService;

    @Test
    public void testGetUserRole() {
        // Artificially create a situation where only id 0 is bound to a
        // user.
        lenient().when(this.mockUserRoleRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new OfflineMember()));

        UserRole u;

        u = this.userRoleService.getUserRole(0);
        Assertions.assertEquals(0, u.getId());

        u = this.userRoleService.getUserRole(4);
        Assertions.assertNull(u);
    }
}