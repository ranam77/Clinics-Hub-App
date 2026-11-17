package com.ranamustafa.clinicshub.data.model;

public class AppointmentListItem {

    public static final int TYPE_DAY = 0;
    public static final int TYPE_APPOINTMENT = 1;
    private final int type;
    private String day;
    private final AppointmentModel booking ;
    public AppointmentListItem(int type, String day, AppointmentModel booking) {
        this.type = type;
        this.day = day;
        this.booking = booking;
    }

    public static AppointmentListItem appointment(AppointmentModel booking) {
        return new AppointmentListItem(TYPE_APPOINTMENT, null, booking);
    }

    public AppointmentListItem daytoappointment(String day) {
        return new AppointmentListItem(TYPE_DAY, day, (AppointmentModel) null);
    }

    public int getType() {
        return type;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day)
    {
        this.day = day;
    }
    public AppointmentModel getBooking() {
        return booking;
    }
}
