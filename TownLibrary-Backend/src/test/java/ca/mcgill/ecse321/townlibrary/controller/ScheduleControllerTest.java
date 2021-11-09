package ca.mcgill.ecse321.townlibrary.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import ca.mcgill.ecse321.townlibrary.dto.ScheduleDTO;
import ca.mcgill.ecse321.townlibrary.model.DayOfWeek;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Time;
import java.util.ArrayList;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ScheduleControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private int libraryId = 0;
    private int librarianId;

    @BeforeEach
    public void setup(){
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        
        // library 
        post("/libraries/" + libraryId +"?address=1800 some address");

        // librarian
        this.librarianId = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .post("/head-librarians/Joe Schmoe")
            .then()
            .extract()
            .response().body().path("id");
    }

    @Test
    public void testGetLibrarianScheduleByDay(){
        // no schedule for given librarian and day
        when()
        .get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(400)
        .body(equalTo("NO-SCHEDULE"));

        // add a schedule
        int scheduleId = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().body().path("id");
        
        // get schedule
        ScheduleDTO dto = when()
        .get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().as(ScheduleDTO.class);

        // check if values are correct
        assertEquals(scheduleId, dto.getId());
        assertEquals(DayOfWeek.MONDAY.toString(), dto.getDayOfWeek().toString());
        assertEquals(Time.valueOf("8:00:00"), dto.getStartTime());
        assertEquals(Time.valueOf("12:00:00"), dto.getEndTime());
        assertEquals(librarianId, dto.getLibrarianId());
    }

    @Test
    public void testGetLibraryScheduleByDay(){
        // no schedules
        when()
        .get("/schedules/library/"+libraryId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(400)
        .body(equalTo("NO-SCHEDULE"));

        // add a schedule
        int scheduleId = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/library/"+libraryId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().body().path("id");
        
        // get schedule
        ScheduleDTO dto = when()
        .get("/schedules/library/"+libraryId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().as(ScheduleDTO.class);

        // check if values are correct
        assertEquals(scheduleId, dto.getId());
        assertEquals(DayOfWeek.MONDAY.toString(), dto.getDayOfWeek().toString());
        assertEquals(Time.valueOf("8:00:00"), dto.getStartTime());
        assertEquals(Time.valueOf("12:00:00"), dto.getEndTime());
        assertEquals(libraryId, dto.getLibraryId());
    }

    @Test
    public void testCreateSchedule(){
        given().param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/other/"+ DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200);
    }

    @Test
    public void testBadCreateSchedule(){
        given()
        .param("startTime", "12:00")
        .param("endTime", "8:00")
        .when()
        .post("/schedules/other/"+ DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(400)
        .body(equalTo("START-TIME-AFTER-END-TIME"));
    }

    @Test
    public void testCreateLibrarianSchedule(){
        // post a schedule
        ScheduleDTO dto = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract()
        .response().as(ScheduleDTO.class); 

        // check if values are correct
        assertEquals(librarianId, dto.getLibrarianId());
        assertEquals(DayOfWeek.MONDAY.toString(), dto.getDayOfWeek().toString());
        assertEquals(Time.valueOf("8:00:00"), dto.getStartTime());
        assertEquals(Time.valueOf("12:00:00"), dto.getEndTime());

        int scheduleId = get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString()).path("id");
        assertEquals(scheduleId, dto.getId());
    }

    @Test
    public void testBadCreateLibrarianSchedule(){
        // post a schedule with start time after end time
        given()
        .param("startTime", "12:00")
        .param("endTime", "8:00")
        .when()
        .post("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(400).body(equalTo("START-TIME-AFTER-END-TIME"));
    }

    @Test
    public void testCreateLibrarySchedule(){
        // post a schedule
        ScheduleDTO dto = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/library/"+libraryId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract()
        .response().as(ScheduleDTO.class); 

        // check if values are correct
        assertEquals(libraryId, dto.getLibraryId());
        assertEquals(DayOfWeek.MONDAY.toString(), dto.getDayOfWeek().toString());
        assertEquals(Time.valueOf("8:00:00"), dto.getStartTime());
        assertEquals(Time.valueOf("12:00:00"), dto.getEndTime());

        int scheduleId = get("/schedules/library/"+libraryId+"/"+DayOfWeek.MONDAY.toString()).path("id");
        assertEquals(scheduleId, dto.getId());
    }

    @Test
    public void testBadCreateLibrarySchedule(){
        // post a schedule with start time after end time
        given()
        .param("startTime", "12:00")
        .param("endTime", "8:00")
        .when()
        .post("/schedules/library/"+libraryId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(400).body(equalTo("START-TIME-AFTER-END-TIME"));
    }

    @Test
    public void testDeleteSchedule(){
        // post a schedule
        int scheduleId = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract()
        .response().body().path("id");

        // get the schedule
        ScheduleDTO dto = 
        when().get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().as(ScheduleDTO.class);

        // should be true if schedule is still there
        assertEquals(scheduleId, dto.getId());

        // delete
        given()
        .param("id", scheduleId)
        .when()
        .delete("/schedules/" + scheduleId)
        .then()
        .statusCode(200);
        
        // should not exist anymore 
        when().get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(400)
        .body(equalTo("NO-SCHEDULE"));
    }

    @Test
    public void testDeleteScheduleEmpty(){
        // trying to delete an object that does not exist
        int notExistingScheduleId = 500;
        given()
        .param("id", notExistingScheduleId)
        .when()
        .delete("/schedules/" + notExistingScheduleId)
        .then()
        .statusCode(400)
        .body(equalTo("NO-SCHEDULE"));
    }

    @Test
    public void testGetLibrarianSchedules(){
        // no schedules yet
        when().get("/schedules/librarian/"+librarianId).then().statusCode(400).body(equalTo("NO-SCHEDULE"));
        // create librarian schedule for mon-fri
        for (int i = 0; i < 5; i++){
            given()
            .param("startTime", "8:00")
            .param("endTime", "17:00")
            .when()
            .post("/schedules/librarian/" + librarianId + "/" + dayOfWeekToString(i))
            .then()
            .statusCode(200);
        }

        when().get("/schedules/librarian/"+librarianId).then().statusCode(200).body("size()", equalTo(5));
    }

    @Test
    public void testGetLibrarySchedules(){
        // no schedules yet
        when().get("/schedules/library/"+libraryId).then().statusCode(400).body(equalTo("NO-SCHEDULE"));
        // create librarian schedule for mon-fri
        for (int i = 0; i < 7; i++){
            given()
            .param("startTime", "8:00")
            .param("endTime", "17:00")
            .when()
            .post("/schedules/library/" + libraryId + "/" + dayOfWeekToString(i))
            .then()
            .statusCode(200);
        }

        when().get("/schedules/library/"+libraryId).then().statusCode(200).body("size()", equalTo(7));
    }

    @Test
    public void testAssignSchedule(){
        // initial librarian schedule
        given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().body().path("id");

        ScheduleDTO dto = get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString()).as(ScheduleDTO.class);
        assertEquals(DayOfWeek.MONDAY.toString(), dto.getDayOfWeek().toString());
        // tuesday schedule should no exist yet
        when().get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.TUESDAY.toString()).then().statusCode(400).body(equalTo("NO-SCHEDULE"));

        // schedule that cannot be assigned
        int badScheduleId = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/other/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().body().path("id");

        // schedule that can be assigned
        int goodScheduleId = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/other/"+DayOfWeek.TUESDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().body().path("id");

        // test if schedules are assigned properly
        given().param("dailyScheduleId", badScheduleId).when().put("/schedules/librarian/" + librarianId).then().statusCode(400).body(equalTo("OVERLAP-SCHEDULE-ASSIGNMENT"));
        given().param("dailyScheduleId", goodScheduleId).when().put("/schedules/librarian/" + librarianId).then().statusCode(200);
        
        // now tuesday schedule should exist
        dto = get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.TUESDAY.toString()).as(ScheduleDTO.class);
        assertEquals(DayOfWeek.TUESDAY.toString(), dto.getDayOfWeek().toString());

    }

    @Test
    public void testSetLibrarySchedule(){
        // initial library schedule
        ArrayList<Integer> librarySchedule = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++){
            librarySchedule.add(
            given()
            .param("startTime", "8:00")
            .param("endTime", "17:00")
            .when()
            .post("/schedules/library/" + libraryId + "/" + dayOfWeekToString(i))
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id")
            );
        }
        // check current endtime for reference
        ScheduleDTO dto = 
        get("/schedules/library/" + libraryId + "/" + DayOfWeek.MONDAY.toString())
        .then()
        .extract()
        .response().as(ScheduleDTO.class);
        Time currentEndTime = dto.getEndTime();
        assertEquals(Time.valueOf("17:00:00"), currentEndTime);

        // create new set of schedules
        ArrayList<Integer> newSchedules = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++){
            newSchedules.add(
            given()
            .param("startTime", "8:00")
            .param("endTime", "19:00")
            .when()
            .post("/schedules/other/" + dayOfWeekToString(i))
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id")
            );
        }

        given().param("dailyScheduleIds", newSchedules).when().put("/schedules/library/"+libraryId).then().statusCode(200);

        // check current endtime after set schedule
        dto = 
        get("/schedules/library/" + libraryId + "/" + DayOfWeek.MONDAY.toString())
        .then()
        .extract()
        .response().as(ScheduleDTO.class);
        currentEndTime = dto.getEndTime();
        assertEquals(Time.valueOf("19:00:00"), currentEndTime);
    }

    @Test
    public void testBadSetLibrarySchedule(){
        // initial library schedule
        ArrayList<Integer> librarySchedule = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++){
            librarySchedule.add(
            given()
            .param("startTime", "8:00")
            .param("endTime", "17:00")
            .when()
            .post("/schedules/library/" + libraryId + "/" + dayOfWeekToString(i))
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id")
            );
        }

        // create new set of schedules with only 5 days instead of 7
        ArrayList<Integer> newSchedules = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++){
            newSchedules.add(
            given()
            .param("startTime", "8:00")
            .param("endTime", "19:00")
            .when()
            .post("/schedules/other/" + dayOfWeekToString(i))
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id")
            );
        }

        given().param("dailyScheduleIds", newSchedules).when().put("/schedules/library/"+libraryId).then().statusCode(400).body(equalTo("NOT-WEEK-LIBRARY-SCHEDULE"));
    }

    @Test 
    public void testUpdateSchedule(){
        // post a schedule
        int scheduleId = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract()
        .response().body().path("id");

        // get the schedule
        ScheduleDTO dto = 
        when().get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().as(ScheduleDTO.class);

        // check current end time
        assertEquals(Time.valueOf("12:00:00"), dto.getEndTime());

        // update with end time as 17:00
        given().param("newStartTime", "8:00").param("newEndTime", "17:00").when().put("/schedules/" + scheduleId).then().statusCode(200);

        // get the schedule
        dto = 
        when().get("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract().response().as(ScheduleDTO.class);

        // check if current time updated
        assertEquals(Time.valueOf("17:00:00"), dto.getEndTime());

    }

    @Test 
    public void testBadUpdateSchedule(){
        // post a schedule
        int scheduleId = given()
        .param("startTime", "8:00")
        .param("endTime", "12:00")
        .when()
        .post("/schedules/librarian/"+librarianId+"/"+DayOfWeek.MONDAY.toString())
        .then()
        .statusCode(200)
        .extract()
        .response().body().path("id");

        // update with start time after end time
        given().param("newStartTime", "17:00").param("newEndTime", "10:00").when().put("/schedules/" + scheduleId).then().statusCode(400).body(equalTo("START-TIME-AFTER-END-TIME"));

    }    

  

    @AfterEach
    public void cleanup(){
        RestAssuredMockMvc.reset();
    }

    /** Returns day of week as string depending on index 
     * 
     * @param i - index
     * @return day of week at index as string
     */
    private String dayOfWeekToString(int i){
        switch(i){
            case 0:
                return DayOfWeek.MONDAY.toString();
            case 1:
                return DayOfWeek.TUESDAY.toString();
            case 2:
                return DayOfWeek.WEDNESDAY.toString();
            case 3:
                return DayOfWeek.THURSDAY.toString();
            case 4:
                return DayOfWeek.FRIDAY.toString();
            case 5:
                return DayOfWeek.SATURDAY.toString();
            case 6:
                return DayOfWeek.SUNDAY.toString();
            default:
                return null;
        }
    }

}
