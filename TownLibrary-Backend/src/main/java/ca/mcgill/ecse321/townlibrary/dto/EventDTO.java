package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Event;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Transaction;

public final class EventDTO {

    public Library lib;
    public int id;
    public String name;
    public Transaction transaction;

    public EventDTO() {
    }

    public EventDTO(Library lib, int id, String name, Transaction transaction) {
        this.lib = lib;
        this.id = id;
        this.name = name;
        this.transaction = transaction;
    }

    public static EventDTO fromModel(Event e) {
        final EventDTO dto = new EventDTO(
            e.getLibrary(), e.getId(), e.getName(), e.getTransaction());
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

    public Transaction getTransaction() {
        return this.transaction;
    }
}