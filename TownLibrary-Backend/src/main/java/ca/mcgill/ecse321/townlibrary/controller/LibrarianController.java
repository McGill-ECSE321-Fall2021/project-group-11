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
public class LibrarianController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private LibrarianService librarianService;

    @Autowired
    private HeadLibrarianService headLibrarianService;

    @GetMapping(value={ "/librarians", "/librarians/" })
    public ResponseEntity<?> getAllLibrarians() {
        final List<LibrarianDTO> us = this.librarianService.getAllLibrarians()
                .stream()
                .map(LibrarianDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(us);
    }

    @GetMapping(value={ "/librarians/{id}", "/librarians/{id}/" })
    public ResponseEntity<?> getLibrarian(@PathVariable("id") int id)  {
        final Librarian u = this.librarianService.getLibrarian(id);
        if (u == null)
            return ResponseEntity.badRequest().body("NOT-FOUND-LIBRARIAN");
        return ResponseEntity.ok(LibrarianDTO.fromModel(u));
    }

    @PostMapping(value={ "/librarians/{name}", "/librarians/{name}/" })
    public ResponseEntity<?> createLibrarian(
            @PathVariable("name") String name,
            @RequestParam String password,
            @RequestParam String address,
            @RequestParam int library,
            @RequestParam int initId,
            @RequestParam String initPass) {

        try {
            if (!this.headLibrarianService.authenticateHeadLibrarian(initId, initPass))
            {
                return ResponseEntity.badRequest().body("BAD-ACCESS");
            }
            final Library lib = this.libraryService.getLibrary(library);
            final Librarian u = this.librarianService.createLibrarian(lib, name, address, password);
            return ResponseEntity.ok().body(LibrarianDTO.fromModel(u));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value={ "/auth/librarians/{id}", "/auth/librarians/{id}/" })
    public ResponseEntity<?> authLibrarian(
            @PathVariable("id") int id,
            @RequestParam String password) {

        if (!this.librarianService.authenticateLibrarian(id, password))
            return ResponseEntity.badRequest().body("BAD-AUTH-LIBRARIAN");

        final Librarian u = this.librarianService.getLibrarian(id);
        return ResponseEntity.ok(LibrarianDTO.fromModel(u));
    }
    
    @DeleteMapping(value={ "/librarians/{id}", "/librarians/{id}/"})
    public ResponseEntity<?> deleteLibrarian(
        @PathVariable int id,
        @RequestParam int initId,
        @RequestParam String initPass) {
        try{
            if (!this.headLibrarianService.authenticateHeadLibrarian(initId, initPass))
            {
                return ResponseEntity.badRequest().body("BAD-ACCESS");
            }
            final boolean b = this.librarianService.deleteLibrarian(id);
            return ResponseEntity.ok(b);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    } 
     
}