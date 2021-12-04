package ca.mcgill.ecse321.townlibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import ca.mcgill.ecse321.townlibrary.databinding.FragmentScheduleBinding;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;
    private ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    private String error;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // LOGIC
        HttpUtils.get("/schedules/library/0", new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] Header, JSONArray response){
                for (int i = 0; i < response.length(); i++){
                    Schedule schedule = new Schedule();
                    try {
                        String startTime = String.valueOf(response.getJSONObject(i).getString("startTime"));
                        String endTime = String.valueOf(response.getJSONObject(i).getString("endTime"));
                        String dayOfWeek = String.valueOf(response.getJSONObject(i).getString("dayOfWeek"));
                        int dayOfWeekInt;

                        switch (dayOfWeek){
                            case "MONDAY":
                                dayOfWeekInt = 0;
                                break;
                            case "TUESDAY":
                                dayOfWeekInt = 1;
                                break;
                            case "WEDNESDAY":
                                dayOfWeekInt = 2;
                                break;
                            case "THURSDAY":
                                dayOfWeekInt = 3;
                                break;
                            case "FRIDAY":
                                dayOfWeekInt = 4;
                                break;
                            case "SATURDAY":
                                dayOfWeekInt = 5;
                                break;
                            case "SUNDAY":
                                dayOfWeekInt = 6;
                                break;
                            default:
                                dayOfWeekInt = 7;
                                break;
                        }
                        schedule.setDay(dayOfWeekInt);
                        schedule.setStartTime(new Time(Integer.valueOf(startTime.substring(0,2)),0));
                        schedule.setEndTime(new Time(Integer.valueOf(endTime.substring(0,2)),0));
                        schedules.add(schedule);

                    } catch (JSONException e) {
                        error += e.getMessage();
                    }
                }
                binding.timetable.add(schedules);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                error += responseString;
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
