package com.test.rmmfl.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    Long id;
    String isoCode;
    String country;
    String website;
    String city;
    String address1;
    String address2;
    String email;
    String phone;
}
