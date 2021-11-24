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
}
