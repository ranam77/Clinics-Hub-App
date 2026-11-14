package com.ranamustafa.clinicshub.data.model;

public class SlotRequest {

    private String fromDate;
    private int doctorId;
    private String toDate;
    private int cl_Sp_Id;

    public SlotRequest(String fromDate, int doctorId,
                       String toDate, int cl_Sp_Id) {
        this.fromDate = fromDate;
        this.doctorId = doctorId;
        this.toDate = toDate;
        this.cl_Sp_Id = cl_Sp_Id;
    }
    public SlotRequest(String fromDate, int doctorId) {
        this.fromDate = fromDate;
        this.doctorId = doctorId;

    }
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getCl_Sp_Id() {
        return cl_Sp_Id;
    }

    public void setCl_Sp_Id(int cl_Sp_Id) {
        this.cl_Sp_Id = cl_Sp_Id;
    }
}