package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping(value = { "/events", "/events/"})
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents()
            .stream().map(EventDTO::fromModel)
            .collect(Collectors.toList());
    }
    
    @PostMapping(value = { "/events/{name}", "/events/{name}/"})
    public EventDTO createEvent(
        @PathVariable("name") String name,
        @RequestParam int id,
        @RequestParam Library lib,
        @RequestParam Transaction transaction)
        throws IllegalArgumentException {
            Event e = eventService.createEvent(lib, id, name, transaction);
            EventDTO eDTO = new EventDTO(e.getLibrary(), e.getId(), e.getName(), e.getTransaction());
            return eDTO;
        }

}