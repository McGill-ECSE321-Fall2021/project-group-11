package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Creates an event
     *
     * @param lib           The library
     * @param id            The event id
     * @param name          The event name
     * @param transaction   The transaction
     *
     * @return              The event instance
     *
     * @throws IllegalArgumentException invalid inputs
     */
    @Transactional
    public Event createEvent(Library lib, String name) {
        final StringBuilder err = new StringBuilder();
        if (lib == null) {
            err.append("Invalid inputs");
        }
        final Event e = new Event();
        e.setLibrary(lib);
        e.setName(name);
        eventRepository.save(e);

        if (err.length() != 0) {
            throw new IllegalArgumentException(err.toString());
        }
        return e;
    }

    /**
     * Deletes an event from the repository
     * 
     * @param eventId       The event's id
     * @return              True if deleted
     * 
     * @throws IllegalArgumentException if the event does not exist
     */
    @Transactional
    public boolean deleteEvent(int eventId) {
        final Event e = this.eventRepository.findById(eventId).orElseThrow(() ->
                new IllegalArgumentException("EVENT-NOT-FOUND"));
        this.eventRepository.delete(e);
        return !this.eventRepository.findById(eventId).isPresent();
    }

    /**
     * Adds a user to an event
     * 
     * @param e             The event
     * @param user          The user
     * 
     * @throws IllegalArgumentException if either event or user is null
     */
    @Transactional
    public void addUserToEvent(Event e, UserRole user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        if (e == null) {
            throw new IllegalArgumentException("Invalid event");
        }
        e.getUsers().add(user);;
    }

    /**
     * Removes a user from an event
     * 
     * @param e             The event
     * @param user          The user
     * 
     * @throws IllegalArgumentException if either event or user is null
     */
    @Transactional
    public void removeUserFromEvent(Event e, UserRole user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        if (e == null) {
            throw new IllegalArgumentException("Invalid event");
        }
        e.getUsers().remove(user);
    }

    /**
     * Retrieves an event by its id
     *
     * @param id    The event's id
     *
     * @return The event or null
     */
    @Transactional
    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all the events in the system
     *
     * @return all the events
     */
    @Transactional
    public List<Event> getAllEvents() {
        final ArrayList<Event> events = new ArrayList<>();
        for (final Event e : eventRepository.findAll()) {
            events.add(e);
        }
        return events;
    }
}