package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.townlibrary.model.OnlineMember;
import ca.mcgill.ecse321.townlibrary.service.OnlineMemberService;

@CrossOrigin(origins="*")
@RestController
public class OnlineMemberController {

    @Autowired
    private OnlineMemberService onlineMemberService;

    // TODO: Use DTO's... please...

    @GetMapping(value={ "/online-members", "/online-members/" })
    public ResponseEntity<?> getAllOnlineMembers() {
        final List<OnlineMember> us = this.onlineMemberService.getAllOnlineMembers();
        return ResponseEntity.ok().body(us);
    }

    @PostMapping(value={ "/online-members/{username}", "/online-members/{username}/" })
    public ResponseEntity<?> createOnlineMember(
            @PathVariable("username") String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String address) {

        try {
            final OnlineMember u = this.onlineMemberService.createOnlineMember(null, name, address, email, username, password);
            return ResponseEntity.ok().body(u);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}