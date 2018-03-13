package comp3350.iPuP.presentation;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import comp3350.iPuP.R;
import comp3350.iPuP.business.AccessParkingSpots;
import comp3350.iPuP.objects.ParkingSpot;
import comp3350.iPuP.objects.TimeSlot;

public class HostViewTimeActivity extends ListActivity
{
    AccessParkingSpots accessParkingSpots;
    TimeSlotAdapter adapter;
    long spotID, slotID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view_time);

        slotID = getIntent().getLongExtra("slotID",-1);
        spotID = getIntent().getLongExtra("spotID",-1);

        accessParkingSpots = new AccessParkingSpots();

        populateList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_CANCELED)
        {
            if (populateList())
            {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
    }

    private boolean populateList()
    {
        try
        {
            ParkingSpot spot = accessParkingSpots.getParkingSpot(spotID);

            TextView tv = findViewById(R.id.textViewAddress);
            tv.setText(String.format(getResources().getString(R.string.hostview_Address), spot.getAddress()));
            tv = findViewById(R.id.textViewRate);
            tv.setText(String.format(getResources().getString(R.string.hostview_Rate), spot.getRate()));

            ArrayList<TimeSlot> timeSlots = accessParkingSpots.getTimeSlots(slotID);
            adapter = new TimeSlotAdapter(this, timeSlots);
            setListAdapter(adapter);
            return adapter.getCount() == 0;
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
