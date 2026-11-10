package com.ranamustafa.clinicshub.data.model;
import com.google.gson.annotations.SerializedName;
public class DoctorModel {
        @SerializedName("id")
        private int id;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("clinicName")
        private String clinicName;
        @SerializedName("specialtyName")
        private String specialtyName;
        @SerializedName("imageUrl")
        private String imageUrl;

        public int getId() {
            return id;
        }

        public String getFirstName() {
                return firstName;
        }
        public String getLastName() {
                return lastName;
        }

        public String getClinicName() {
                return clinicName;
        }

        public String getSpecialtyName() {
                return specialtyName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

}
