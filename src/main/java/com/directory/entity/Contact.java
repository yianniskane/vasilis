package com.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "contact"
)
public class Contact implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "contact_seq"
    )
    @SequenceGenerator(
            name = "contact_seq",
            sequenceName = "contact_seq",
            allocationSize = 1
    )
    private long id;
    @Column(
            name = "contact_no"
    )
    private Integer contact_no;
    @Column(
            name = "date_recorded"
    )
    private String date_recorded;
    @Column(
            name = "company"
    )
    private String company;
    @Column(
            name = "manager"
    )
    private String manager;
    @Column(
            name = "telephone"
    )
    private String telephone;
    @Column(
            name = "fax"
    )
    private String fax;
    @Embedded
    Address address;
    @Column(
            name = "orders"
    )
    private String orders;
    @Column(
            name = "comments"
    )
    private String comments;
    @Column(
            name = "email"
    )
    private @Email String email;
    @Column(
            name = "seller"
    )
    private String seller;
    @Column(
            name = "completed"
    )
    private boolean completed;
    @Column(
            name = "last_comm_date"
    )
    private String last_comm_date;
    @Column(
            name = "followup"
    )
    private String followup;
    @Column(
            name = "ignored"
    )
    private boolean ignored;
    @Column(
            name = "afm"
    )
    private String afm;


}

