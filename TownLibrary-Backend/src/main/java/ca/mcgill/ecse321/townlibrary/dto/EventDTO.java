package ca.mcgill.ecse321.townlibrary.dto;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import ca.mcgill.ecse321.townlibrary.model.Event;
import ca.mcgill.ecse321.townlibrary.model.UserRole;

public final class EventDTO {

    public int id;
    public String name;
    public Integer libId;
    public Set<Integer> users = new HashSet<>();

    public static EventDTO fromModel(Event e) {
        final EventDTO dto = new EventDTO();
        dto.id = e.getId();
        dto.name = e.getName();
        for (UserRole u : e.getUsers()) {
            dto.users.add(u.getId());
        }
        dto.libId = Optional.ofNullable(e.getLibrary())
                        .map(x -> x.getId()).orElse(null);
        return dto;
    }
}