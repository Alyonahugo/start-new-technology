package com.test.entity;

import lombok.*;


/**
 * Created by opolishchuk on 21.10.2015.
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Office {
    private String address;
    private String city;
    private String code;
    private String email;
    private String phone;
}
