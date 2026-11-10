package com.ranamustafa.clinicshub.data.model;

import com.google.gson.annotations.SerializedName;

public class SpecialtyModel {
        @SerializedName("id")
        private int id;

        @SerializedName("ClinicID")
        private int ClinicID ;

        @SerializedName("name")
        private String name;

        @SerializedName("imageUrl")
        private String imageUrl;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

}
