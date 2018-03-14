package comp3350.iPuP.tests.business;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import comp3350.iPuP.application.Main;
import comp3350.iPuP.application.Services;
import comp3350.iPuP.business.AccessParkingSpots;
import comp3350.iPuP.objects.Booking;
import comp3350.iPuP.objects.DAOException;
import comp3350.iPuP.objects.DateFormatter;
import comp3350.iPuP.objects.ParkingSpot;
import comp3350.iPuP.objects.TimeSlot;
import comp3350.iPuP.tests.persistence.DataAccessStub;

public class AccessParkingSpotsTest extends TestCase
{
    private static String dbName = Main.dbName;
    AccessParkingSpots parkSpotAccess;
    ParkingSpot ps;
    ArrayList<ParkingSpot> spots;
    ArrayList<ParkingSpot> allSpots;
    ArrayList<Booking> bookings;

    public AccessParkingSpotsTest(String arg0)
    {
        super(arg0);
    }

    public void testEmptyList()
    {
        Main.startUp();
        System.out.println("Starting testAccessParkingSpots: No parking spots inserted.");
        parkSpotAccess=new AccessParkingSpots();
        parkSpotAccess.clearSpots();
        spots=parkSpotAccess.getAvailableSpots();
        assertTrue(spots.size()==0);

        assertTrue(spots.size()==0);
        System.out.println("Finished testAccessParkingSpots: No parking spots inserted.");
    }

    public void testCreateSpotOneDay()
    {
        Services.closeDataAccess();
        Services.createDataAccess(new DataAccessStub(dbName));
        ParkingSpot spot = null;

        parkSpotAccess = new AccessParkingSpots();
        parkSpotAccess.clearSpots();

        Calendar c = Calendar.getInstance();
        c.set(2018, 3, 24, 10, 30);
        Date start, end;
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY, 2);
        end = c.getTime();

        TimeSlot timeSlot = new TimeSlot(start, end);
        try
        {
            parkSpotAccess.insertParkingSpot("testuser", timeSlot, null, "356 testing drive, Winnipeg, MB", "456-6789", "", 42);
        }
        catch (DAOException daoe)
        {
            System.out.println(daoe.getMessage());
            fail();
        }

        try
        {
            assertEquals(parkSpotAccess.getAllParkingSpots().size(), 1);

            spot = parkSpotAccess.getAllParkingSpots().get(0);
        }
        catch (DAOException daoe)
        {
            fail();
        }

        assertEquals(spot.getName(), "testuser");
        assertEquals(spot.getPhone(), "456-6789");
        assertEquals(spot.getEmail(), "");
        assertEquals(spot.getAddress(), "356 testing drive, Winnipeg, MB");
        assertEquals(spot.getRate(), 42.0);

        long spotID = spot.getSpotID();
        ArrayList<TimeSlot> daySlots = null;
        try
        {
            daySlots = parkSpotAccess.getDaySlots(spotID);
        }
        catch (Exception e)
        {
            fail();
        }

        c.set(2018, 3, 24, 10, 30);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY, 2);
        end = c.getTime();

        assertEquals(daySlots.size(), 1);
        TimeSlot daySlot = daySlots.get(0);
        assertEquals(daySlot.getStart(), start);
        assertEquals(daySlot.getEnd(), end);

        long daySlotID = daySlot.getSlotID();
        ArrayList<TimeSlot> timeSlots = null;
        try
        {
            timeSlots = parkSpotAccess.getTimeSlots(daySlotID);
        }
        catch (Exception e)
        {
            fail();
        }

        assertEquals(timeSlots.size(), 4);

        c.set(2018, 3, 24, 10, 30);
        start = c.getTime();
        c.add(Calendar.MINUTE, 30);
        end = c.getTime();
        timeSlot = timeSlots.get(0);

        assertEquals(timeSlot.getStart(), start);
        assertEquals(timeSlot.getEnd(), end);

        start = c.getTime();
        c.add(Calendar.MINUTE, 30);
        end = c.getTime();
        timeSlot = timeSlots.get(1);

        assertEquals(timeSlot.getStart(), start);
        assertEquals(timeSlot.getEnd(), end);
    }

    public void testCreateSpotRepeatDay()
    {
        Services.closeDataAccess();
        Services.createDataAccess(new DataAccessStub(dbName));
        ParkingSpot spot = null;

        parkSpotAccess = new AccessParkingSpots();
        parkSpotAccess.clearSpots();

        Calendar c = Calendar.getInstance();
        Date start, end;
        long spotID, daySlotID;
        ArrayList<TimeSlot> daySlots;
        ArrayList<TimeSlot> timeSlots;
        TimeSlot daySlot;
        TimeSlot timeSlot;

        c.set(2018, 10, 12, 16, 30);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,1);
        end = c.getTime();

        timeSlot = new TimeSlot(start,end);
        try
        {
            parkSpotAccess.insertParkingSpot("testuser2", timeSlot, "Days 3 4", "whodunnit St.", "555-5555", "hans@hans.hans", 10);
        }
        catch (Exception e)
        {
            fail();
        }

        try
        {
            assertEquals(parkSpotAccess.getAllParkingSpots().size(), 1);

            spot = parkSpotAccess.getAllParkingSpots().get(0);
        }
        catch (DAOException daoe) {fail();}

        assertEquals(spot.getName(), "testuser2");
        assertEquals(spot.getPhone(), "555-5555");
        assertEquals(spot.getEmail(), "hans@hans.hans");
        assertEquals(spot.getAddress(), "whodunnit St.");
        assertEquals(spot.getRate(), 10.0);

        spotID = spot.getSpotID();
        daySlots = null;
        try
        {
            daySlots = parkSpotAccess.getDaySlots(spotID);
        }
        catch (Exception e)
        {
            fail();
        }

        assertEquals(daySlots.size(), 4);

        c.set(2018, 10, 15, 16, 30);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,1);
        end = c.getTime();

        daySlot = daySlots.get(1);
        assertEquals(daySlot.getStart(), start);
        assertEquals(daySlot.getEnd(), end);

        c.set(2018, 10, 18, 16, 30);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,1);
        end = c.getTime();

        daySlot = daySlots.get(2);
        assertEquals(daySlot.getStart(), start);
        assertEquals(daySlot.getEnd(), end);


        c.set(2018, 10, 21, 16, 30);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,1);
        end = c.getTime();

        assertEquals(daySlots.size(), 4);
        daySlot = daySlots.get(3);
        assertEquals(daySlot.getStart(), start);
        assertEquals(daySlot.getEnd(), end);

        daySlotID = daySlot.getSlotID();
        timeSlots = null;
        try
        {
            timeSlots = parkSpotAccess.getTimeSlots(daySlotID);
        }
        catch (Exception e)
        {
            fail();
        }

        assertEquals(2, timeSlots.size());

        c.set(2018, 10, 21, 16, 30);
        start = c.getTime();
        c.add(Calendar.MINUTE,30);
        end = c.getTime();
        timeSlot = timeSlots.get(0);

        assertEquals(timeSlot.getStart(), start);
        assertEquals(timeSlot.getEnd(), end);

        start = c.getTime();
        c.add(Calendar.MINUTE,30);
        end = c.getTime();
        timeSlot = timeSlots.get(1);

        assertEquals(start, timeSlot.getStart());
        assertEquals(end, timeSlot.getEnd());
    }

    public void testCreateSpotRepeatWeek()
    {
        Services.closeDataAccess();
        Services.createDataAccess(new DataAccessStub(dbName));
        ParkingSpot spot = null;

        parkSpotAccess = new AccessParkingSpots();
        parkSpotAccess.clearSpots();

        Calendar c = Calendar.getInstance();
        Date start, end;
        long spotID, daySlotID;
        ArrayList<TimeSlot> daySlots = null;
        ArrayList<TimeSlot> timeSlots = null;
        TimeSlot daySlot;
        TimeSlot timeSlot;

        c.set(1455, 1, 1, 1, 0);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,6);
        end = c.getTime();

        timeSlot = new TimeSlot(start,end);
        try
        {
            parkSpotAccess.insertParkingSpot("Sir Galavant", timeSlot, "Weeks 2 5 0100111", "5 Smithy Lane, Camelot", "0909090", "galavant@roundtable.brit", 0.02);
        }
        catch (Exception e)
        {
            fail();
        }

        try
        {
            assertEquals(parkSpotAccess.getAllParkingSpots().size(), 1);

            spot = parkSpotAccess.getAllParkingSpots().get(0);
        }
        catch (DAOException daoe) {fail();}

        assertEquals(spot.getName(), "Sir Galavant");
        assertEquals(spot.getPhone(), "0909090");
        assertEquals(spot.getEmail(), "galavant@roundtable.brit");
        assertEquals(spot.getAddress(), "5 Smithy Lane, Camelot");
        assertEquals(spot.getRate(), 0.02);

        spotID = spot.getSpotID();
        try
        {
            daySlots = parkSpotAccess.getDaySlots(spotID);
        }
        catch (Exception e)
        {
            fail();
        }

        assertEquals(20, daySlots.size());

        c.set(1455, 1, 1, 1, 0);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,6);
        end = c.getTime();

        daySlot = daySlots.get(0);
        assertEquals(start, daySlot.getStart());
        assertEquals(end, daySlot.getEnd());

        c.set(1455, 1, 1, 1, 0);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,6);
        end = c.getTime();

        daySlot = daySlots.get(0);
        assertEquals(start, daySlot.getStart());
        assertEquals(end, daySlot.getEnd());

        c.set(1455, 1, 3, 1, 0);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,6);
        end = c.getTime();

        daySlot = daySlots.get(1);
        assertEquals(daySlot.getStart(), start);
        assertEquals(daySlot.getEnd(), end);

        c.set(1455, 1, 6, 1, 0);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,6);
        end = c.getTime();

        daySlot = daySlots.get(2);
        assertEquals(start, daySlot.getStart());
        assertEquals(end, daySlot.getEnd());

        c.set(1455, 1, 7, 1, 0);
        start = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,6);
        end = c.getTime();

        daySlot = daySlots.get(3);
        assertEquals(start, daySlot.getStart());
        assertEquals(end, daySlot.getEnd());

        daySlotID = daySlot.getSlotID();
        timeSlots = null;
        try
        {
            timeSlots = parkSpotAccess.getTimeSlots(daySlotID);
        }
        catch (Exception e)
        {
            fail();
        }

        assertEquals(timeSlots.size(), 12);

        c.set(1455, 1, 7, 1, 0);
        start = c.getTime();
        c.add(Calendar.MINUTE,30);
        end = c.getTime();
        timeSlot = timeSlots.get(0);

        assertEquals(timeSlot.getStart(), start);
        assertEquals(timeSlot.getEnd(), end);

        start = c.getTime();
        c.add(Calendar.MINUTE,30);
        end = c.getTime();
        timeSlot = timeSlots.get(1);

        assertEquals(timeSlot.getStart(), start);
        assertEquals(timeSlot.getEnd(), end);

        start = c.getTime();
        c.add(Calendar.MINUTE,30);
        end = c.getTime();
        timeSlot = timeSlots.get(2);

        assertEquals(timeSlot.getStart(), start);
        assertEquals(timeSlot.getEnd(), end);

        start = c.getTime();
        c.add(Calendar.MINUTE,30);
        end = c.getTime();
        timeSlot = timeSlots.get(3);

        assertEquals(timeSlot.getStart(), start);
        assertEquals(timeSlot.getEnd(), end);
    }



    public void testGettingMyBookingsValid()
    {
        Services.closeDataAccess();

        Services.createDataAccess(new DataAccessStub(dbName));

        parkSpotAccess = new AccessParkingSpots();
        //parkSpotAccess.clearSpots();

        String username = "marker";
        try
        {
            Booking abooking;
            DateFormatter dateFormatter = new DateFormatter();
            bookings = parkSpotAccess.getMyBookedSpots(username);
            assertEquals(4, bookings.size());
            abooking = bookings.get(0);
            assertEquals("marker",abooking.getName());
            assertEquals((long)173,abooking.getTimeSlotId());
            assertEquals("1000 St. Mary's Rd",abooking.getAddress());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 18:30:00"),abooking.getStart());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 19:00:00"),abooking.getEnd());
            abooking = bookings.get(1);
            assertEquals("marker",abooking.getName());
            assertEquals((long)91,abooking.getTimeSlotId());
            assertEquals("1338 Chancellor Drive",abooking.getAddress());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 14:00:00"),abooking.getStart());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 14:30:00"),abooking.getEnd());
            abooking = bookings.get(2);
            assertEquals("marker",abooking.getName());
            assertEquals((long)94,abooking.getTimeSlotId());
            assertEquals("91 Dalhousie Drive",abooking.getAddress());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 10:30:00"),abooking.getStart());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 11:00:00"),abooking.getEnd());
            abooking = bookings.get(3);
            assertEquals("marker",abooking.getName());
            assertEquals((long)145,abooking.getTimeSlotId());
            assertEquals("1 Pembina Hwy",abooking.getAddress());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 12:30:00"),abooking.getStart());
            assertEquals(dateFormatter.getSqlDateTimeFormat().parse("2018-06-11 13:00:00"),abooking.getEnd());
        }
        catch (DAOException de)
        {
            System.out.print(de.getMessage());
            fail();
        }
        catch (ParseException pe)
        {
            System.out.print(pe.getMessage());
            fail();
        }
    }

    public void testGettingMyBookingsEmptyList()
    {
        Services.closeDataAccess();

        Services.createDataAccess(new DataAccessStub(dbName));
        parkSpotAccess = new AccessParkingSpots();
        parkSpotAccess.clearSpots();

        String username = "tester";
        try
        {
            bookings = parkSpotAccess.getMyBookedSpots(username);
            assertEquals(0, bookings.size());
        }
        catch (DAOException de)
        {
            System.out.print(de.getMessage());
            fail();
        }
    }

    public void testCancelABookingValid()
    {
        Services.closeDataAccess();

        Services.createDataAccess(new DataAccessStub(dbName));
        parkSpotAccess = new AccessParkingSpots();
        //parkSpotAccess.clearSpots();

        String username = "marker";
        long timeSlotId = 91;
        try
        {
            bookings = parkSpotAccess.getMyBookedSpots(username);
            Booking removed = bookings.get(1);
            parkSpotAccess.cancelThisSpot(username, timeSlotId);
            bookings = parkSpotAccess.getMyBookedSpots(username);
            assertEquals(3, bookings.size());
            assertEquals(false, bookings.contains(removed));
        }
        catch (DAOException de)
        {
            System.out.print(de.getMessage());
            fail();
        }
    }

    public void testCancelABookingOfAnotherUser()
    {
        Services.closeDataAccess();

        Services.createDataAccess(new DataAccessStub(dbName));
        parkSpotAccess = new AccessParkingSpots();
        parkSpotAccess.clearSpots();
        String username = "Donald Trump";
        long timeSlotId = 91;

        try
        {
            parkSpotAccess.cancelThisSpot(username, timeSlotId);
            bookings = parkSpotAccess.getMyBookedSpots(username);
            assertEquals(0, bookings.size());
        }
        catch (DAOException de)
        {
            System.out.print(de.getMessage());
            fail();
        }
    }

    public void testCancelABookingOfEmptyList()
    {
        Services.closeDataAccess();

        Services.createDataAccess(new DataAccessStub(dbName));
        parkSpotAccess = new AccessParkingSpots();
        parkSpotAccess.clearSpots();

        String username = "tester";
        long timeSlotId = 91;
        try
        {
            parkSpotAccess.cancelThisSpot(username, timeSlotId);
            bookings = parkSpotAccess.getMyBookedSpots(username);
            assertEquals(0, bookings.size());
        }
        catch (DAOException de)
        {
            System.out.print(de.getMessage());
            fail();
        }
    }

}