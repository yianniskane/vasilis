package com.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "cardinfo"
)
public class CardInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cardinfo")
    @SequenceGenerator(name = "cardinfo", sequenceName = "cardinfo_seq", allocationSize = 1)
    private long id;
    @Column(
            name = "email"
    )
    private String email;
    @Column(
            name = "phone"
    )
    private String telephone;
    @Column(
            name = "www"
    )
    private String www;
    @Column(
            name = "profession"
    )
    private String profession;
    @Column(
            name = "company"
    )
    private String company;
    @Column(
            name = "address"
    )
    private String address;
    @Column(
            name = "date_recorded"
    )
    private Instant date_recorded;
    @Column(
            name = "cell_phone"
    )
    private @Size(
            max = 255
    ) String cellPhone;
    @Column(
            name = "first_name"
    )
    private String firstName;
    @Column(
            name = "last_name"
    )
    private String lastName;
}