package comp3350.iPuP.objects;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Mark Van Egmond on 1/23/2018.
 */

public class ParkingSpot
{
    public Date getStartTime() {
        return reservation.getStart();
    }

    public Date getEndTime() {
        return reservation.getEnd();
    }
    /*public String getStartTime() {
        return reservation.getStart();
    }
    public String getEndTime() {
        return reservation.getEnd();
    }*/

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public double getRate()
    {
        return rate;
    }

    private ReservationTime reservation;
    private String address;
    private String name;
    private String phone;
    private String email;
    private double rate;
    //Rodney added
    private boolean booked;

    public ParkingSpot(ReservationTime reservation, String address, String name, String phone, String email, double rate)
    {
        this.reservation = reservation; //required
        this.address = address;// required
        this.name = name;// required

        // either phone or email required
        this.phone = phone;
        this.email = email;

        this.rate = rate;// required
        //Rodney added.
        booked=false;
    }

    @Override
    public String toString() {
        return this.address + "\n" + this.reservation.toString();
    }
}
