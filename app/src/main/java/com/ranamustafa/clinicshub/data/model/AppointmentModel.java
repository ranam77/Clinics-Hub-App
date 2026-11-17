package com.ranamustafa.clinicshub.data.model;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.data.model.PatientModel;
import com.ranamustafa.clinicshub.data.model.SlotModel;
import com.ranamustafa.clinicshub.data.model.SpecialtyModel;

public class AppointmentModel {

    private int id;
    private int slotId;
    private String bookingRef;
    private PatientModel patient;
    private ListModel clinic;
    private DoctorModel doctor;
    private SpecialtyModel speciality;
    private int status;
    private SlotModel slot;

    public int getId() { return id; }
    public int getSlotId() { return slotId; }
    public String getBookingRef() { return bookingRef; }
    public PatientModel getPatient() { return patient; }
    public ListModel getClinic() { return clinic; }
    public DoctorModel getDoctor() { return doctor; }
    public SpecialtyModel getSpeciality() { return speciality; }
    public int getStatus() { return status; }

    public void setId(int id) {
        this.id = id;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    public void setClinic(ListModel clinic) {
        this.clinic = clinic;
    }

    public void setDoctor(DoctorModel doctor) {
        this.doctor = doctor;
    }

    public void setSpeciality(SpecialtyModel speciality) {
        this.speciality = speciality;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSlot(SlotModel slot) {
        this.slot = slot;
    }

    public SlotModel getSlot() { return slot; }
}