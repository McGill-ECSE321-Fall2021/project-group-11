package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyScheduleRepository extends CrudRepository<DailySchedule, Integer> {

    /** List all the schedules by day
     * 
     * @param dayOfWeek -   Days of the week (Monday to Sunday)
     * @return              List of schedules on that day of the week
     */
    List<DailySchedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    /** List the library's schedule for a day
     * 
     * @param library   -   System's library
     * @return              Daily schedule of library 
     */
    List<DailySchedule> findByLibrary(Library library);

    /** List the schedules of a specific librarian
     * 
     * @param librarian -   A librarian
     * @return              List of schedules for that librarian
     */
    List<DailySchedule> findByLibrarian(Librarian librarian);
}
