package com.example.restapi2.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.restapi2.models.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {

}

/*
 * public interface RentalRepository extends JpaRepository<Rental, Long> {
 * 
 * @Query("SELECT new com.example.UserDto(u.id, u.username) FROM Rental r JOIN r.user u WHERE r.id = :rentalId"
 * )
 * UserDto findUsernameByRentalId(@Param("rentalId") Long rentalId);
 * }
 */

/*
 * 
 * // DTO Projection
 * public class UserDto {
 * private Long id;
 * private String username;
 * // getters and setters
 * }
 * 
 * // Repository
 * public interface UserRepository extends JpaRepository<User, Long> {
 * 
 * @Query("SELECT new com.example.UserDto(u.id, u.username) FROM User u WHERE u.id = :userId"
 * )
 * UserDto findUsernameById(@Param("userId") Long userId);
 * }
 * 
 * // Service
 * 
 * @Service
 * public class UserService {
 * 
 * @Autowired
 * private UserRepository userRepository;
 * 
 * public UserDto getUsernameById(Long userId) {
 * return userRepository.findUsernameById(userId);
 * }
 * }
 */