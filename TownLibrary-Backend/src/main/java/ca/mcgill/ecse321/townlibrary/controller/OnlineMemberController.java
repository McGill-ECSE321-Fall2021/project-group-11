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
public class OnlineMemberController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private OnlineMemberService onlineMemberService;

    @GetMapping(value={ "/online-members", "/online-members/" })
    public ResponseEntity<?> getAllOnlineMembers() {
        final List<OnlineMemberDTO> us = this.onlineMemberService.getAllOnlineMembers()
                .stream()
                .map(OnlineMemberDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(us);
    }

    @GetMapping(value={ "/online-members/{id}", "/online-members/{id}/" })
    public ResponseEntity<?> getOnlineMember(@PathVariable("id") int id)  {
        final OnlineMember u = this.onlineMemberService.getOnlineMember(id);
        if (u == null)
            return ResponseEntity.badRequest().body("NOT-FOUND-ONLINE-MEMBER");
        return ResponseEntity.ok(OnlineMemberDTO.fromModel(u));
    }

    @PostMapping(value={ "/online-members/{username}", "/online-members/{username}/" })
    public ResponseEntity<?> createOnlineMember(
            @PathVariable("username") String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam int library) {

        try {
            final Library lib = this.libraryService.getLibrary(library);
            final OnlineMember u = this.onlineMemberService.createOnlineMember(lib, name, address, email, username, password);
            return ResponseEntity.ok().body(OnlineMemberDTO.fromModel(u));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value={ "/auth/online-members/{username}", "/auth/online-members/{username}/"})
    public ResponseEntity<?> authOnlineMember(
            @PathVariable("username") String username,
            @RequestParam String password) {

        final OnlineMember u = this.onlineMemberService.getOnlineMemberByUsername(username);
        if (u != null && u.getPassword().equals(password))
            return ResponseEntity.ok(OnlineMemberDTO.fromModel(u));

        // Working under the assumption that no-user-exists and
        // incorrect-password should both report incorrect-credential or sth.
        return ResponseEntity.badRequest().body("BAD-AUTH-ONLINE-MEMBER");
    }
}