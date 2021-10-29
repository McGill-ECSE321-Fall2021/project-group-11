package ca.mcgill.ecse321.townlibrary.controller;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    // TODO: return DTO's

    @GetMapping(value={ "/libraries", "/libraries/" })
    public ResponseEntity<?> getAllLibraries() {
        final List<Library> libs = this.libraryService.getAllLibraries();
        return ResponseEntity.ok(libs);
    }

    @PostMapping(value={ "/libraries/{id}", "/libraries/{id}/" })
    public ResponseEntity<?> createLibrary(
            @PathVariable("id") int id,
            @RequestParam String address) {
        try {
            final Library lib = this.libraryService.createLibrary(id, address);
            return ResponseEntity.ok(lib);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
