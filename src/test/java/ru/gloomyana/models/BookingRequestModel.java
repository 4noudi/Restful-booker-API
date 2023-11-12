package ru.gloomyana.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequestModel {
    String firstname, lastname;

    @JsonProperty("totalprice")
    int totalPrice;

    @JsonProperty("depositpaid")
    boolean depositPaid;

    @JsonProperty("bookingdates")
    BookingDatesModel bookingDatesModel;

    @JsonProperty("additionalneeds")
    String additionalNeeds;
}
