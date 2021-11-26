package ca.mcgill.ecse321.townlibrary.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse321.townlibrary.model.Event;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.OnlineMember;
import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.UserRole;


public class EventDTOTest {

    @Test
    public void testFromModel() {
        final Event e = new Event();
        e.setId(1001);
        e.setName("bday");

        EventDTO dto;
        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.id);
        Assertions.assertEquals(e.getName(), dto.name);
        Assertions.assertNull(e.getLibrary());
        Assertions.assertEquals(e.getUsers(), dto.users);;

        Library l = new Library();
        e.setLibrary(l);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.id);
        Assertions.assertEquals(e.getName(), dto.name);
        Assertions.assertEquals(l.getId(), dto.libId);
        Assertions.assertEquals(e.getUsers(), dto.users);;
    
        UserRole u = new OnlineMember();
        Set<UserRole> users = new HashSet<>();
        users.add(u);
        e.setUsers(users);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.id);
        Assertions.assertEquals(e.getName(), dto.name);
        Assertions.assertEquals(l.getId(), dto.libId);
        Assertions.assertEquals(e.getUsers().iterator().next().getId(), dto.users.iterator().next());
    }

    @Test
    public void testGetters() {
        final Event e = new Event();
        e.setId(1001);
        e.setName("bday");
        Library l = new Library();
        l.setId(1001);
        e.setLibrary(l);
        UserRole u = new OnlineMember();
        UserRole u2 = new OnlineMember();
        Set<UserRole> users = new HashSet<>();
        users.add(u);
        users.add(u2);
        e.setUsers(users);
        EventDTO dto;
        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.id);
        Assertions.assertEquals(e.getName(), dto.name);
        Assertions.assertEquals(e.getLibrary().getId(), dto.libId);
        Assertions.assertEquals(e.getUsers().iterator().next().getId(), dto.users.iterator().next());
    }
}