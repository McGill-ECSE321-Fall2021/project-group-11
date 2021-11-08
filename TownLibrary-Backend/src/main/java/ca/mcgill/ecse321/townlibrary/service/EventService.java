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
     * Sets a transaction for an event
     * 
     * @param e             The event
     * @param transaction   The transaction
     * 
     * @throws IllegalArgumentException if transaction is null
     */

    @Transactional
    public void setEventTransaction(Event e, Transaction transaction) {
        final StringBuilder err = new StringBuilder();
        if (transaction == null) {
            err.append("Invalid transaction");
        }
        e.setTransaction(transaction);
        eventRepository.save(e);
        
        if (err.length() != 0) {
            throw new IllegalArgumentException(err.toString());
        }
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
     * Retrieves an event by its transaction
     * 
     * @param transaction   The event's transaction
     * 
     * @return The event
     */
    @Transactional
    public Event getEventByTransaction(Transaction transaction) {
        return eventRepository.findByTransaction(transaction);
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