package ca.mcgill.ecse321.townlibrary.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

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

        @PostMapping(value = { "/transactions/{id}", "/transactions/{id}"})
        public TransactionDTO createTransaction(
            @PathVariable("id") int id,
            @RequestParam Timestamp startDate,
            @RequestParam Timestamp endDate,
            @RequestParam int userId) 

            throws IllegalArgumentException {
                final UserRole userRole = onlineMemberService.getOnlineMember(id)
                Transaction t = transactionService.createTransaction(id, startDate, endDate, userRole);
                TransactionDTO tDTO = new TransactionDTO(t.getId(), t.getStartDate(), t.getEndDate(), t.getUserRole());
                return tDTO;
            }
}
