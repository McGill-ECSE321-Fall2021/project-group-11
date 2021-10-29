package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class HeadLibrarianController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private HeadLibrarianService headLibrarianService;

    // TODO: Use DTO's... please...

    @GetMapping(value={ "/head-librarians", "/head-librarians/" })
    public ResponseEntity<?> getAllHeadLibrarians() {
        final List<HeadLibrarian> us = this.headLibrarianService.getAllHeadLibrarians();
        return ResponseEntity.ok().body(us);
    }

    @PostMapping(value={ "/head-librarians/{name}", "/head-librarians/{name}/" })
    public ResponseEntity<?> createHeadLibrarian(
            @PathVariable("name") String name,
            @RequestParam String address,
            @RequestParam int library) {

        try {
            final Library lib = this.libraryService.getLibrary(library);
            final HeadLibrarian u = this.headLibrarianService.createHeadLibrarian(lib, name, address);
            return ResponseEntity.ok().body(u);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}