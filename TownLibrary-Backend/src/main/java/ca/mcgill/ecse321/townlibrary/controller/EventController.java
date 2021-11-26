package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
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
    /*public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents()
            .stream().map(EventDTO::fromModel)
            .collect(Collectors.toList());
    }*/
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

    /*@GetMapping(value={"/events/{id}/users", "/events/{id}/users/"})
    public ResponseEntity<?> getEventUsers(
        @PathVariable("id") int id) {
            final Event e = eventService.getEventById(id);
            return ResponseEntity.ok(e.getUsers());
    }*/
    
    
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

    /*@PostMapping(value = {"/events/add/{id}", "/events/add/{id}/"})
    public ResponseEntity<?> addUser(
        @PathVariable("id") int Id,
        @RequestParam int eventId) {
        try {
            final Event e = eventService.getEventById(eventId);
            final UserRole u = onlineMemberService.getOnlineMember(Id);
            eventService.addUserToEvent(e, u);
            return ResponseEntity.ok().body(EventDTO.fromModel(e));
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }*/
}
