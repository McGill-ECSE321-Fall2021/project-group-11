package ca.mcgill.ecse321.townlibrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.townlibrary.service.ScheduleService;

@CrossOrigin(origins = "*")
@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService service;
}
