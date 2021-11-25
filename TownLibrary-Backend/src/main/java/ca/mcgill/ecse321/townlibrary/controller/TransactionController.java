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

        @GetMapping(value = { "/transactions", "/transactions/"})
        public List<TransactionDTO> getAllTransaction() {
            return transactionService.getAllTransactions()
                .stream().map(TransactionDTO::fromModel)
                .collect(Collectors.toList());
        }

        @GetMapping(value = {"/transactions/{id}", "/transactions/{id}/"})
        public ResponseEntity<?> getTransaction(@PathVariable("id") int id,
        @RequestParam String transactionType) {
            final Transaction t = transactionService.getTransaction(id);
            if (t == null) {
                return ResponseEntity.badRequest().body("NOT-FOUND-TRANSACTION");
            }
            return ResponseEntity.ok(TransactionDTO.fromModel(t));
        }

        @PostMapping(value = { "/transactions/{id}", "/transactions/{id}/"})
        public ResponseEntity<?> createTransaction(
            @PathVariable("id") int id,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date endDate,
            @RequestParam String transactionType,
            @RequestParam int userId) {

            try {
                final UserRole ur = userRoleService.getUserRole(userId);
                final Transaction t = transactionService.createTransaction(id, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), ur);
                return ResponseEntity.ok().body(TransactionDTO.fromModel(t));
            }
            catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }

        }
}
