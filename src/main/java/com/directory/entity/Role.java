package com.directory.entity;

import com.directory.entity.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "roles"
)
@Entity
public class Role implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false
    )
    private Integer id;
    @Column(
            unique = true,
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
}