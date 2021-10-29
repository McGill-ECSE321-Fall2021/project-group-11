package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.townlibrary.model.Librarian;
import ca.mcgill.ecse321.townlibrary.service.LibrarianService;

@CrossOrigin(origins="*")
@RestController
public class LibrarianController {

    @Autowired
    private LibrarianService librarianService;

    // TODO: Use DTO's... please...

    @GetMapping(value={ "/librarians", "/librarians/" })
    public ResponseEntity<?> getAllLibrarians() {
        final List<Librarian> us = this.librarianService.getAllLibrarians();
        return ResponseEntity.ok().body(us);
    }

    @PostMapping(value={ "/librarians/{name}", "/librarians/{name}/" })
    public ResponseEntity<?> createLibrarian(
            @PathVariable("name") String name,
            @RequestParam String address) {

        try {
            final Librarian u = this.librarianService.createLibrarian(null, name, address);
            return ResponseEntity.ok().body(u);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}