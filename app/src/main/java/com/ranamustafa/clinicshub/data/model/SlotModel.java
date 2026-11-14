package com.ranamustafa.clinicshub.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SlotModel {

    @SerializedName("id")
    private int id;

    @SerializedName("code")
    private String code;

    @SerializedName("startsAt")
    private String startsAt;

    @SerializedName("endsAt")
    private String endsAt;

    @SerializedName("booking")
    private List<Object> booking;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public List<Object> getBooking() {
        return booking;
    }
}
