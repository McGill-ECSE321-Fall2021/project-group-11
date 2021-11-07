package ca.mcgill.ecse321.townlibrary.controller;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import ca.mcgill.ecse321.townlibrary.service.ScheduleService;
import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;

@CrossOrigin(origins = "*")
@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService service;

    @PostMapping(value={"/schedules", "/schedules/"})
    public ScheduleDTO createSchedule(@RequestParam DayOfWeek dayOfWeek, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Time startTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm")Time endTime){
        return ScheduleDTO.convertScheduleDTO(service.createDailySchedule(dayOfWeek, startTime, endTime));
    }

    // prob not needed
    @GetMapping(value={"/schedules", "/schedules/"})
    public ResponseEntity<?> getAllDailySchedules(){
        List<ScheduleDTO> dto = new ArrayList<ScheduleDTO>();
        for (DailySchedule dailySchedule:service.getAllDailySchedules()){
            dto.add(ScheduleDTO.convertScheduleDTO(dailySchedule));
        }
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value={"/schedules/{librarian}", "/schedules/{librarian}/"})
    public ResponseEntity<?> getLibrarianSchedules(@PathVariable("librarian") Librarian librarian){
        List<ScheduleDTO> dto = new ArrayList<ScheduleDTO>();
        for (DailySchedule dailySchedule:service.getLibrarianDailySchedules(librarian)){
            dto.add(ScheduleDTO.convertScheduleDTO(dailySchedule));
        }
        return ResponseEntity.ok().body(dto);
    }
    
    @GetMapping(value={"/schedules/{librarian}", "/schedules/{librarian}/"})
    public ResponseEntity<?> getLibrarianSchedulePerDayOfWeek(@PathVariable("librarian") Librarian librarian, DayOfWeek dayOfWeek){
        try{
            ScheduleDTO dto = ScheduleDTO.convertScheduleDTO(service.getLibrarianDailyScheduleByDayOfWeek(librarian, dayOfWeek));
            return ResponseEntity.ok().body(dto);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value={"/schedules/{library}", "/schedules/{library}/"})
    public List<ScheduleDTO> getLibraryDailySchedule(@PathVariable Library library){
        List<ScheduleDTO> dto = new ArrayList<ScheduleDTO>();
        for (DailySchedule dailySchedule:service.getLibraryDailySchedule(library)){
            dto.add(ScheduleDTO.convertScheduleDTO(dailySchedule));
        }
        return dto;
    }

    @GetMapping(value={"/schedules", "/schedules/"})
    public ResponseEntity<?> getDayOfWeekSchedule(@RequestParam DayOfWeek dayOfWeek){
        List<ScheduleDTO> dto = new ArrayList<ScheduleDTO>();
        for (DailySchedule dailySchedule:service.getDayOfWeekDailySchedules(dayOfWeek)){
            dto.add(ScheduleDTO.convertScheduleDTO(dailySchedule));
        }
        return ResponseEntity.ok().body(dto);
    }

    // not sure
    @PutMapping(value={"/schedules/{librarian}", "/schedules/{librarian}/"})
    public ResponseEntity<?> assignSchedule(@RequestParam DailySchedule dailySchedule, @PathVariable Librarian librarian){
        try {
            service.assignSchedule(dailySchedule, librarian);
            return ResponseEntity.ok().body("");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value={"/schedules/{library}", "/schedules/{library}/"})
    public void setLibrarySchedule(@RequestParam List<DailySchedule> dailySchedules, @PathVariable Library library){
        service.setLibrarySchedule(dailySchedules, library);
    }

    @PutMapping(value={"/schedules", "/schedules/"})
    public void updateSchedule(@RequestParam DailySchedule dailySchedule, @RequestParam DayOfWeek newDayOfWeek, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Time newStartTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm")Time newEndTime){
        service.updateSchedule(dailySchedule, newDayOfWeek, newStartTime, newEndTime);
    }



}
