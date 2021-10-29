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
    private OfflineMemberService offlineMemberService;

    @GetMapping(value={ "/offline-members", "/offline-members/" })
    public ResponseEntity<?> getAllOfflineMembers() {
        final List<OfflineMemberDTO> us = this.offlineMemberService.getAllOfflineMembers()
                .stream()
                .map(OfflineMemberDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(us);
    }

    @GetMapping(value={ "/offline-members/{id}", "/offline-members/{id}/" })
    public ResponseEntity<?> getOfflineMember(@PathVariable("id") int id)  {
        final OfflineMember u = this.offlineMemberService.getOfflineMember(id);
        if (u == null)
            return ResponseEntity.badRequest().body("NOT-FOUND-OFFLINE-MEMBER");
        return ResponseEntity.ok(OfflineMemberDTO.fromModel(u));
    }

    @PutMapping(value={ "/offline-members/{id}/in-town", "/offline-members/{id}/in-town/" })
    public ResponseEntity<?> setOfflineMemberInTownStatus(
            @PathVariable("id") int id,
            @RequestParam boolean value,
            @RequestParam int initId,
            @RequestParam String initPass) {

        try {
            if (!this.librarianService.authenticateLibrarian(initId, initPass))
                return ResponseEntity.badRequest().body("BAD-ACCESS");

            final OfflineMember u = this.offlineMemberService.setOfflineMemberInTownStatus(id, value);
            return ResponseEntity.ok(OfflineMemberDTO.fromModel(u));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value={ "/offline-members/{name}", "/offline-members/{name}/" })
    public ResponseEntity<?> createOfflineMember(
            @PathVariable("name") String name,
            @RequestParam String address,
            @RequestParam int library,
            @RequestParam int initId,
            @RequestParam String initPass) {

        try {
            if (!this.librarianService.authenticateLibrarian(initId, initPass))
                return ResponseEntity.badRequest().body("BAD-ACCESS");

            final Library lib = this.libraryService.getLibrary(library);
            final OfflineMember u = this.offlineMemberService.createOfflineMember(lib, name, address);
            return ResponseEntity.ok().body(OfflineMemberDTO.fromModel(u));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}