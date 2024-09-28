package com.directory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@Entity
@Table(
        name = "users"
)
public class User implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;
    @Column(
            nullable = false
    )
    private String name;
    @Column(
            nullable = false,
            unique = true
    )
    private @NotEmpty(
            message = "Το email είναι υποχρεωτικό πεδίο."
    ) String email;
    @Column(
            nullable = false
    )
    private @NotEmpty(
            message = "Το συνθηματικό είναι υποχρεωτικό πεδίο."
    ) String password;
    @OneToOne(
            cascade = {CascadeType.ALL}
    )
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Role role;


}
