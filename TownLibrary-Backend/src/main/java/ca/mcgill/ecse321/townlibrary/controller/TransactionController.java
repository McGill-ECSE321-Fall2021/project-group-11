package ca.mcgill.ecse321.townlibrary.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class TransactionController {

        @Autowired
        private TransactionService transactionService;

        @Autowired
        private UserRoleService userRoleService;

        @GetMapping(value = { "/transactions/{userId}", "/transactions/{userId}"})
        public ResponseEntity<?> getAllTransaction(@PathVariable("userId") int userId) {

            try {
                UserRole user = userRoleService.getUserRole(userId);
                final List<TransactionDTO> allTransactions = this.transactionService.getTransactionsByUser(user)
                .stream()
                .map(TransactionDTO::fromModel)
                .collect(Collectors.toList());
                return ResponseEntity.ok().body(allTransactions);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @GetMapping(value = {"/transactions/{userId}/{id}", "/transactions/{userId}/{id}/"})
        public ResponseEntity<?> getTransaction(@PathVariable("id") int id,
                                                @PathVariable("userId") int userId) {
            final Transaction t = transactionService.getTransaction(id);
            if (t == null) {
                return ResponseEntity.badRequest().body("NOT-FOUND-TRANSACTION");
            }
            return ResponseEntity.ok(TransactionDTO.fromModel(t));
        }

        @PostMapping(value = { "/transactions/{userId}/{id}", "/transactions/{userId}/{id}/"})
        public ResponseEntity<?> createTransaction(
            @PathVariable("id") int id,
            @PathVariable("userId") int userId,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date endDate,
            @RequestParam TransactionType transactionType) {

            try {
                final UserRole ur = userRoleService.getUserRole(userId);
                final Transaction t = transactionService.createTransaction(id, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), ur, transactionType);
                return ResponseEntity.ok().body(TransactionDTO.fromModel(t));
            }
            catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }

        }
}
