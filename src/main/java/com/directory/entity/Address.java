package com.directory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Embeddable
public class Address {
    @Column(
            name = "street"
    )
    private String street;
    @Column(
            name = "area"
    )
    private String area;
    @Column(
            name = "city"
    )
    private String city;
    @Column(
            name = "zip"
    )
    private String zip;
    @Column(
            name = "nomos"
    )
    private String nomos;
    @Column(
            name = "country"
    )
    private String country;
}