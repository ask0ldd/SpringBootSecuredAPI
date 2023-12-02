package com.example.restapi2.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long rentalId;

    // @Column(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "picture", length = 255)
    private String picture;

    @Column(name = "surface")
    private Float surface;

    @Column(name = "price")
    private Float price;

    /*
     * @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval =
     * true)
     * private List<Message> messages = new ArrayList<Message>();
     */

    @CreationTimestamp
    @Column(name = "created_at")
    private Date creation;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date update;
}
