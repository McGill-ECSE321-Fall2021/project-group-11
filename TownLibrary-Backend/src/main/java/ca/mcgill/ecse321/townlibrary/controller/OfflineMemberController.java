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
public class OfflineMemberController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private LibrarianService librarianService;

    @Autowired
    private OfflineMemberService OfflineMemberService;

    @GetMapping(value={ "/offline-members", "/offline-members/" })
    public ResponseEntity<?> getAllOfflineMembers() {
        final List<OfflineMemberDTO> us = this.OfflineMemberService.getAllOfflineMembers()
                .stream()
                .map(OfflineMemberDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(us);
    }

    @PostMapping(value={ "/offline-members/{name}", "/offline-members/{name}/" })
    public ResponseEntity<?> createOfflineMember(
            @PathVariable("name") String name,
            @RequestParam String address,
            @RequestParam int library,
            @RequestParam int initiatorId) {

        try {
            final Librarian initiator = this.librarianService.getLibrarian(initiatorId);
            if (initiator == null)
                return ResponseEntity.badRequest().body("BAD-ACCESS");

            final Library lib = this.libraryService.getLibrary(library);
            final OfflineMember u = this.OfflineMemberService.createOfflineMember(lib, name, address);
            return ResponseEntity.ok().body(OfflineMemberDTO.fromModel(u));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}