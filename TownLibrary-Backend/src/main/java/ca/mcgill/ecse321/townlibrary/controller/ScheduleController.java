package ca.mcgill.ecse321.townlibrary.controller;

import java.sql.Time;
// import java.sql.Time;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.townlibrary.service.ScheduleService;
import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;

@CrossOrigin(origins = "*")
@RestController

/**
 *  FOR DOCUMENTATION, LOOK AT WIKI!
 */

public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @PostMapping(value={"/schedules/other/{dayOfWeek}/", "/schedules/other/{dayOfWeek}"})
    public ResponseEntity<?> createSchedule(
        @PathVariable("dayOfWeek") DayOfWeek dayOfWeek,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date startTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date endTime){
        
        try{
            ScheduleDTO dto = ScheduleDTO.convertScheduleDTO(service.createSchedule(dayOfWeek, new Time(startTime.getTime()), new Time(endTime.getTime())));
            return ResponseEntity.ok().body(dto);
        }catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(value={"/schedules/librarian/{id}/{dayOfWeek}", "/schedules/librarian/{id}/{dayOfWeek}/"})
    public ResponseEntity<?> createLibrarianSchedule(
        @PathVariable("id") int librarianId,
        @PathVariable("dayOfWeek") DayOfWeek dayOfWeek,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date startTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date endTime){
        try{
            ScheduleDTO dto = ScheduleDTO.convertScheduleDTO(service.createLibrarianSchedule(librarianId, dayOfWeek, new Time(startTime.getTime()), new Time(endTime.getTime())));
            return ResponseEntity.ok().body(dto);
        }catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }

    @PostMapping(value={"/schedules/library/{id}/{dayOfWeek}", "/schedules/library/{id}/{dayOfWeek}/"})
    public ResponseEntity<?> createLibrarySchedule(
        @PathVariable("id") int libraryId,
        @PathVariable("dayOfWeek") DayOfWeek dayOfWeek,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date startTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date endTime){
        try{
            ScheduleDTO dto = ScheduleDTO.convertScheduleDTO(service.createLibrarySchedule(libraryId, dayOfWeek, new Time(startTime.getTime()), new Time(endTime.getTime())));
            return ResponseEntity.ok().body(dto);
        }catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }

    // prob not needed, uncomment if needed
    // also add {"/schedules", "/schedules/"} mapping values
    // @GetMapping(value={"/schedules", "/schedules/"})
    // public ResponseEntity<?> getAllDailySchedules(){
    //     List<ScheduleDTO> dto = new ArrayList<ScheduleDTO>();
    //     for (DailySchedule dailySchedule:service.getAllDailySchedules()){
    //         dto.add(ScheduleDTO.convertScheduleDTO(dailySchedule));
    //     }
    //     return ResponseEntity.ok().body(dto);
    // }
  

    @GetMapping(value={"/schedules/librarian/{id}", "/schedules/librarian/{id}/"})
    public ResponseEntity<?> getLibrarianSchedules(@PathVariable("id") int librarianId){
        List<ScheduleDTO> dto = new ArrayList<ScheduleDTO>();
        try{
            for (DailySchedule dailySchedule:service.getLibrarianSchedules(librarianId)){
                dto.add(ScheduleDTO.convertScheduleDTO(dailySchedule));
            }
            return ResponseEntity.ok().body(dto);
        }catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    
    @GetMapping(value={"/schedules/librarian/{id}/{dayOfWeek}", "/schedules/librarian/{id}/{dayOfWeek}/"})
    public ResponseEntity<?> getLibrarianSchedulePerDay(@PathVariable("id") int librarianId, @PathVariable("dayOfWeek") DayOfWeek dayOfWeek){
        try{
            ScheduleDTO dto = ScheduleDTO.convertScheduleDTO(service.getLibrarianScheduleByDay(librarianId, dayOfWeek));
            return ResponseEntity.ok().body(dto);
        }
        catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping(value={"/schedules/library/{id}", "/schedules/library/{id}/"})
    public ResponseEntity<?> getLibrarySchedules(@PathVariable("id") int libraryId){
        List<ScheduleDTO> dto = new ArrayList<ScheduleDTO>();
        try{
            for (DailySchedule dailySchedule:service.getLibrarySchedules(libraryId)){
                dto.add(ScheduleDTO.convertScheduleDTO(dailySchedule));
            }
            return ResponseEntity.ok().body(dto);
        }catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
       
    }

    @GetMapping(value={"/schedules/library/{id}/{dayOfWeek}", "/schedules/library/{id}/{dayOfWeek}/"})
    public ResponseEntity<?> getLibrarySchedulePerDay(@PathVariable("id") int libraryId, @PathVariable("dayOfWeek") DayOfWeek dayOfWeek){
        try{
            ScheduleDTO dto = ScheduleDTO.convertScheduleDTO(service.getLibraryScheduleByDay(libraryId, dayOfWeek));
            return ResponseEntity.ok().body(dto);
        }
        catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping(value={"/schedules/librarian/{id}", "/schedules/librarian/{id}/"})
    public ResponseEntity<?> assignSchedule(@RequestParam int dailyScheduleId, @PathVariable("id") int librarianId){
        try {
            service.assignSchedule(dailyScheduleId, librarianId);
            return ResponseEntity.ok().body("");
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping(value={"/schedules/library/{id}", "/schedules/library/{id}/"})
    public ResponseEntity<?> setLibrarySchedule(@RequestParam List<Integer> dailyScheduleIds, @PathVariable("id") int libraryId){
        try{
            service.setLibrarySchedule(dailyScheduleIds, libraryId);
            return ResponseEntity.ok().body("");
        }catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping(value={"/schedules/{id}", "/schedules/{id}/"})
    public ResponseEntity<?> updateSchedule(
        @PathVariable("id") int dailyScheduleId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date newStartTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") Date newEndTime){
        try{
            service.updateSchedule(dailyScheduleId, new Time(newStartTime.getTime()), new Time(newEndTime.getTime()));
            return ResponseEntity.ok().body("");
        }catch(Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping(value={"/schedules/{id}", "/schedules/{id}/"})
    public ResponseEntity<?> deleteSchedule(
        @PathVariable("id") int dailyScheduleId){
            try{
                service.deleteSchedule(dailyScheduleId);
                return ResponseEntity.ok().body("");
            }catch(Exception exception){
                return ResponseEntity.badRequest().body(exception.getMessage());
            }
    }



}
