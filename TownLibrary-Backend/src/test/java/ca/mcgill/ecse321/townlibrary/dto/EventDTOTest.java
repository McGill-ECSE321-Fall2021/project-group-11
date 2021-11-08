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
        Assertions.assertEquals(e.getId(), dto.getEventID());
        Assertions.assertEquals(e.getName(), dto.getEventName());
        Assertions.assertNull(e.getLibrary());
        Assertions.assertNull(e.getTransaction());

        Library l = new Library();
        e.setLibrary(l);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.getEventID());
        Assertions.assertEquals(e.getName(), dto.getEventName());
        Assertions.assertEquals(l.getId(), dto.getLibraryId());
        Assertions.assertNull(e.getTransaction());

        Transaction t = new Transaction();
        t.setId(1002);
        e.setTransaction(t);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.getEventID());
        Assertions.assertEquals(e.getName(), dto.getEventName());
        Assertions.assertEquals(l.getId(), dto.getLibraryId());
        Assertions.assertEquals(t.getId(), dto.getTransactionId());
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
        Assertions.assertEquals(e.getId(), dto.getEventID());
        Assertions.assertEquals(e.getName(), dto.getEventName());
        Assertions.assertEquals(e.getLibrary().getId(), dto.getLibraryId());
        Assertions.assertEquals(e.getTransaction().getId(), dto.getTransactionId());
    }

    @Test
    public void testConstructor() {
        Library l = new Library();
        l.setId(1001);
        Transaction t = new Transaction();
        t.setId(1002);
        final EventDTO dto = new EventDTO(1004, "bday2", l.getId(), t.getId());
        Assertions.assertEquals(1004, dto.getEventID());
        Assertions.assertEquals("bday2", dto.getEventName());
        Assertions.assertEquals(l.getId(), dto.getLibraryId());
        Assertions.assertEquals(t.getId(), dto.getTransactionId());
    }
}