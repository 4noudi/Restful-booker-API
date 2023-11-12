package ru.gloomyana.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookingResponseModel {
    @JsonProperty("bookingid")
    int bookingId;

    @JsonProperty("booking")
    BookingRequestModel bookingRequestModel;
}
