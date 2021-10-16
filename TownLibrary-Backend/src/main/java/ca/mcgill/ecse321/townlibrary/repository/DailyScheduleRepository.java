package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public interface DailyScheduleRepository extends CrudRepository<DailySchedule, Integer> {

    List<DailySchedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    // not sure about these
    List<DailySchedule> findByStartTime(Time startTime);
    List<DailySchedule> findByEndTime(Time endTime);
    List<DailySchedule> findByStartTimeAndEndTime(Time startTime, Time endTime);
    DailySchedule findByLibrary(Library library);
    List<DailySchedule> findByLibrarian(Librarian librarian);
}
