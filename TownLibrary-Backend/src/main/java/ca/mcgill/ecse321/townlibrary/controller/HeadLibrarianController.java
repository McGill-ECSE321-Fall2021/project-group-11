package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class HeadLibrarianController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private HeadLibrarianService headLibrarianService;

    @GetMapping(value={ "/head-librarians", "/head-librarians/" })
    public ResponseEntity<?> getAllHeadLibrarians() {
        final List<HeadLibrarianDTO> us = this.headLibrarianService.getAllHeadLibrarians()
                .stream()
                .map(HeadLibrarianDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(us);
    }

    @GetMapping(value={ "/head-librarians/{id}", "/head-librarians/{id}/" })
    public ResponseEntity<?> getHeadLibrarian(@PathVariable("id") int id)  {
        final HeadLibrarian u = this.headLibrarianService.getHeadLibrarian(id);
        if (u == null)
            return ResponseEntity.badRequest().body("NOT-FOUND-HEAD-LIBRARIAN");
        return ResponseEntity.ok(HeadLibrarianDTO.fromModel(u));
    }

    @PostMapping(value={ "/head-librarians/{name}", "/head-librarians/{name}/" })
    public ResponseEntity<?> createHeadLibrarian(
            @PathVariable("name") String name,
            @RequestParam String password,
            @RequestParam String address,
            @RequestParam int library) {

        try {
            final Library lib = this.libraryService.getLibrary(library);
            final HeadLibrarian u = this.headLibrarianService.createHeadLibrarian(lib, name, address, password);
            return ResponseEntity.ok().body(HeadLibrarianDTO.fromModel(u));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value={ "/auth/head-librarians/{id}", "/auth/head-librarians/{id}/" })
    public ResponseEntity<?> authHeadLibrarian(
            @PathVariable("id") int id,
            @RequestParam String password) {

        if (!this.headLibrarianService.authenticateHeadLibrarian(id, password))
            return ResponseEntity.badRequest().body("BAD-AUTH-LIBRARIAN");

        final HeadLibrarian u = this.headLibrarianService.getHeadLibrarian(id);
        return ResponseEntity.ok(HeadLibrarianDTO.fromModel(u));
    }
}