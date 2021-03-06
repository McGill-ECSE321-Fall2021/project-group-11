package ca.mcgill.ecse321.townlibrary.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired 
    private LibraryService libraryService;

    @Autowired
    private OnlineMemberService onlineMemberService;

    @GetMapping(value = { "/events", "/events/"})
    public ResponseEntity<?> getAllEvents() {
        final List<EventDTO> el = this.eventService.getAllEvents()
            .stream().map(EventDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok().body(el);
    }

    @GetMapping(value = {"/events/{id}", "/events/{id}/"})
    public ResponseEntity<?> getEvent(@PathVariable("id") int id) {
        final Event e = eventService.getEventById(id);
        if (e == null) {
            return ResponseEntity.badRequest().body("NOT-FOUND-EVENT");
        }
        return ResponseEntity.ok(EventDTO.fromModel(e));
    }

    @GetMapping(value={"/events/{id}/users", "/events/{id}/users/"})
    public ResponseEntity<?> getEventUsers(
        @PathVariable("id") int id) {
            final Event e = eventService.getEventById(id);
            final Set<Integer> users = new HashSet<>();
            for (UserRole u : e.getUsers()) {
                users.add(u.getId());
            }
            return ResponseEntity.ok(users);
    }
    
    @GetMapping(value={"/users/{id}/events"})
    public ResponseEntity<?> getUserEvents(
        @PathVariable("id") int id) {
            final UserRole user = onlineMemberService.getOnlineMember(id);
            if (user == null)
                return ResponseEntity.badRequest().body("NOT-FOUND-USER");
            final Set<EventDTO> el = eventService.getEventsByUser(user)
                .stream().map(EventDTO::fromModel).collect(Collectors.toSet());
            return ResponseEntity.ok().body(el);
    }
    
    @PostMapping(value = { "/events/{name}", "/events/{name}/"})
    public ResponseEntity<?> createEvent(
        @PathVariable("name") String name,
        @RequestParam int lib) {
        try {
            final Library library = libraryService.getLibrary(lib);
            final Event e = eventService.createEvent(library, name);
            return ResponseEntity.ok().body(EventDTO.fromModel(e));
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping(value = {"/events/{id}", "/events/{id}/"})
    public ResponseEntity<?> deleteEvent(
        @PathVariable("id") int eventId) {
        try {
            final boolean b = this.eventService.deleteEvent(eventId);
            return ResponseEntity.ok().body(b);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    

    @PostMapping(value = {"/events/{eventid}/users/{userid}", "/events/{eventid}/users/{userid}/"})
    public ResponseEntity<?> addUser(
        @PathVariable("eventid") int eventId,
        @PathVariable("userid") int userId) {
        try {
            final Event e = eventService.getEventById(eventId);
            final UserRole u = onlineMemberService.getOnlineMember(userId);
            eventService.addUserToEvent(e, u);
            return ResponseEntity.ok().body(EventDTO.fromModel(e));
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping(value = {"/events/{eventid}/users/{userid}", "/events/{eventid}/users/{userid}/"})
    public ResponseEntity<?> removeUser(
        @PathVariable("eventid") int eventId,
        @PathVariable("userid") int userId) {
        try {
            final Event e = eventService.getEventById(eventId);
            final UserRole u = onlineMemberService.getOnlineMember(userId);
            eventService.removeUserFromEvent(e, u);
            return ResponseEntity.ok().body(EventDTO.fromModel(e));
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}