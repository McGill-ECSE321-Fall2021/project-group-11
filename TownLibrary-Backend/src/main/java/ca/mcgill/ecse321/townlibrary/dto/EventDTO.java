package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Event;
import ca.mcgill.ecse321.townlibrary.model.Library;

public final class EventDTO {

    public Library lib;
    public int id;
    public String name;

    public EventDTO() {
    }

    public EventDTO(Library lib, int id, String name) {
        this.lib = lib;
        this.id = id;
        this.name = name;
    }

    public static EventDTO fromModel(Event e) {
        final EventDTO dto = new EventDTO(
            e.getLibrary(), e.getId(), e.getName());
        return dto;
    }

    public Library getLibrary() {
        return this.lib;
    }

    public int getEventID() {
        return this.id;
    }

    public String getEventName() {
        return this.name;
    }
}