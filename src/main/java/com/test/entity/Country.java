package com.test.entity;

import lombok.*;

import java.util.List;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Country {
    private String name;
    private List<Office> office;
}
