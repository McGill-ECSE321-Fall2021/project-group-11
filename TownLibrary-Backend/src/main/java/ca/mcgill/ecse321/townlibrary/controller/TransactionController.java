package ca.mcgill.ecse321.townlibrary.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class TransactionController {

        @Autowired
        private TransactionService transactionService;

        @Autowired
        private OnlineMemberService onlineMemberService;

        @GetMapping(value = { "/transactions", "/transactions/"})
        public List<TransactionDTO> getAllTransaction() {
            return transactionService.getAllTransactions()
                .stream().map(TransactionDTO::fromModel)
                .collect(Collectors.toList());
        }

        @GetMapping(value = {"/transactions/{id}", "/transactions/{id}/"})
        public ResponseEntity<?> getTransaction(@PathVariable("id") int id) {
            final Transaction t = transactionService.getTransaction(id);
            if (t == null) {
                return ResponseEntity.badRequest().body("NOT-FOUND-TRANSACTION");
            }
            return ResponseEntity.ok(TransactionDTO.fromModel(t));
        }

        @PostMapping(value = { "/transactions/{id}", "/transactions/{id}/"})
        public ResponseEntity<?> createTransaction(
            @PathVariable("id") int id,
            @RequestParam Timestamp startDate,
            @RequestParam Timestamp endDate,
            @RequestParam int userId) {

            try {
                final UserRole ur = onlineMemberService.getOnlineMember(id); 
                final Transaction t = transactionService.createTransaction(id, startDate, endDate, ur);
                return ResponseEntity.ok().body(TransactionDTO.fromModel(t));
            }
            catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }

        }
}
