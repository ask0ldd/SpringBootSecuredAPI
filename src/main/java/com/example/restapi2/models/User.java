package com.example.restapi2.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
/*
 * @Data est une annotation Lombok. Nul besoin d’ajouter les getters et les
 * setters. La librairie Lombok s’en charge pour nous. Très utile pour alléger
 * le code.
 */
@Entity
/*
 * @Entity est une annotation qui indique que la classe correspond à une table
 * de la base de données.
 */
@Table(name = "admins")
/* spring security needs some UserDetails methods to be implemented */
public class User implements UserDetails {

    @Id
    /* GeneratedValue / Identity : autoincrement a number when id is missing */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "email", unique = true) // !!! unique
    private String email;

    @Column(name = "password")
    private String password;

    // !!! https://www.baeldung.com/jpa-no-argument-constructor-entity-class
    public User() {
    }

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = password;
        this.role = role; // if role is not defined can't pass through spring security
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    // methods that needs to be overriden to implements userDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRole = role.name();
        if (userRole.contains("ROLE_") == false) {
            userRole = "ROLE_" + userRole;
        }
        return List.of(new SimpleGrantedAuthority(userRole));
    }

    @Override
    public String getUsername() { // identifiant login
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

/*
 * 
 * {"id":3,"firstname":"Agathe","lastname":"FEELING","email":
 * "agathefeeling@mail.com","password":
 * "$2a$10$ymf0tl2GnESWTAMY7.rocetvSQpAI9z1MZ.rpdwzhzKhMNT2YTTQS","role":"USER",
 * "enabled":true,"username":"agathefeeling@mail.com","authorities":[{
 * "authority":"USER"}],"credentialsNonExpired":true,"accountNonExpired":true,
 * "accountNonLocked":true}]
 * 
 */