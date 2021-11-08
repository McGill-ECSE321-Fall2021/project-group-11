package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Event;

public final class EventDTO {

    public int id;
    public String name;
    public Integer libId;
    public Integer trId;

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
}