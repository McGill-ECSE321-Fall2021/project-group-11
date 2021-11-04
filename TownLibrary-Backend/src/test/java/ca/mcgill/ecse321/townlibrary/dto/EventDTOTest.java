package ca.mcgill.ecse321.townlibrary.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse321.townlibrary.model.Event;
import ca.mcgill.ecse321.townlibrary.model.Library;


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

        Library l = new Library();
        e.setLibrary(l);

        dto = EventDTO.fromModel(e);
        Assertions.assertEquals(e.getId(), dto.getEventID());
        Assertions.assertEquals(e.getName(), dto.getEventName());
        Assertions.assertEquals(l, dto.getLibrary());
    }
}