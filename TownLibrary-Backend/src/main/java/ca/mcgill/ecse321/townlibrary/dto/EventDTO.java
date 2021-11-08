package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Event;

public final class EventDTO {

    public int id;
    public String name;
    public int libId;
    public int trId;

    public EventDTO() {
    }

    public EventDTO(int id, String name, int libId, int trId) {
        this.id = id;
        this.name = name;
        this.libId = libId;
        this.trId = trId;
    }

    public static EventDTO fromModel(Event e) {
        final EventDTO dto = new EventDTO();
        dto.id = e.getId();
        dto.name = e.getName();
        dto.libId = Optional.ofNullable(e.getLibrary())
                        .map(x -> x.getId()).orElse(null);
        dto.trId = Optional.ofNullable(e.getTransaction())
                        .map(x -> x.getId()).orElse(null);
        return dto;
    }

    public int getEventID() {
        return this.id;
    }

    public String getEventName() {
        return this.name;
    }

    public int getLibraryId() {
        return this.libId;
    }

    public int getTransactionId() {
        return this.trId;
    }
}