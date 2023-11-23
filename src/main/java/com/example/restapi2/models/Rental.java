package com.example.restapi2.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "rentals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long rentalId;

    @Column(name = "owner")
    private Integer owner;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "picture")
    private String picture;

    @Column(name = "surface")
    private Integer surface;

    @Column(name = "price")
    private Float price;

    @Column(name = "creation")
    private Date creation;

    @Column(name = "update")
    private Date update;
}
