package com.example.restapi2.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.restapi2.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByAuthority(String authority);
}