package ca.mcgill.ecse321.townlibrary.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse321.townlibrary.model.Event;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Transaction;


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
        Assertions.assertNull(e.getTransaction());

        Library l = new Library();
        e.setLibrary(l);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.id);
        Assertions.assertEquals(e.getName(), dto.name);
        Assertions.assertEquals(l.getId(), dto.libId);
        Assertions.assertNull(e.getTransaction());

        Transaction t = new Transaction();
        t.setId(1002);
        e.setTransaction(t);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.id);
        Assertions.assertEquals(e.getName(), dto.name);
        Assertions.assertEquals(l.getId(), dto.libId);
        Assertions.assertEquals(t.getId(), dto.trId);
    }

    @Test
    public void testGetters() {
        final Event e = new Event();
        e.setId(1001);
        e.setName("bday");
        Library l = new Library();
        l.setId(1001);
        e.setLibrary(l);
        Transaction t = new Transaction();
        t.setId(1002);
        e.setTransaction(t);
        EventDTO dto;
        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.id);
        Assertions.assertEquals(e.getName(), dto.name);
        Assertions.assertEquals(e.getLibrary().getId(), dto.libId);
        Assertions.assertEquals(e.getTransaction().getId(), dto.trId);
    }
}