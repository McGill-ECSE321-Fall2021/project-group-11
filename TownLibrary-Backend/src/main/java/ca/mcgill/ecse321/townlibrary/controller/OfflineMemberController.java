package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class OfflineMemberController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private OfflineMemberService OfflineMemberService;

    // TODO: Use DTO's... please...

    @GetMapping(value={ "/offline-members", "/offline-members/" })
    public ResponseEntity<?> getAllOfflineMembers() {
        final List<OfflineMember> us = this.OfflineMemberService.getAllOfflineMembers();
        return ResponseEntity.ok().body(us);
    }

    @PostMapping(value={ "/offline-members/{name}", "/offline-members/{name}/" })
    public ResponseEntity<?> createOfflineMember(
            @PathVariable("name") String name,
            @RequestParam String address,
            @RequestParam int library) {

        try {
            final Library lib = this.libraryService.getLibrary(library);
            final OfflineMember u = this.OfflineMemberService.createOfflineMember(lib, name, address);
            return ResponseEntity.ok().body(u);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}