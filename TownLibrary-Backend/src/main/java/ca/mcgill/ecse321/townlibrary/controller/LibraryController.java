package ca.mcgill.ecse321.townlibrary.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping(value={ "/libraries", "/libraries/" })
    public ResponseEntity<?> getAllLibraries() {
        final List<LibraryDTO> libs = this.libraryService.getAllLibraries()
                .stream()
                .map(LibraryDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(libs);
    }

    @GetMapping(value={ "/libraries/{id}", "/libraries/{id}/" })
    public ResponseEntity<?> getLibraryById(
            @PathVariable("id") int id) {
        final Library lib = this.libraryService.getLibrary(id);
        if (lib == null)
            return ResponseEntity.badRequest().body("NOT-FOUND-LIBRARY");
        return ResponseEntity.ok(LibraryDTO.fromModel(lib));
    }

    @PostMapping(value={ "/libraries/{id}", "/libraries/{id}/" })
    public ResponseEntity<?> createLibrary(
            @PathVariable("id") int id,
            @RequestParam String address) {
        try {
            final Library lib = this.libraryService.createLibrary(id, address);
            return ResponseEntity.ok(LibraryDTO.fromModel(lib));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
