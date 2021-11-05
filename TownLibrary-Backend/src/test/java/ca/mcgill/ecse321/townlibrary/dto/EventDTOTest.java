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
        Assertions.assertEquals(e.getTransaction(), dto.getTransaction());
        Assertions.assertNull(e.getLibrary());
        Assertions.assertNull(e.getTransaction());

        Library l = new Library();
        e.setLibrary(l);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.getEventID());
        Assertions.assertEquals(e.getName(), dto.getEventName());
        Assertions.assertEquals(l, dto.getLibrary());
        Assertions.assertNull(e.getTransaction());

        Transaction t = new Transaction();
        e.setTransaction(t);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.getEventID());
        Assertions.assertEquals(e.getName(), dto.getEventName());
        Assertions.assertEquals(l, dto.getLibrary());
        Assertions.assertEquals(t, dto.getTransaction());
    }
}