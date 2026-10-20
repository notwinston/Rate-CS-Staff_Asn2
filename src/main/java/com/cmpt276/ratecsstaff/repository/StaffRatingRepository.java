package com.cmpt276.ratecsstaff.repository;

import com.cmpt276.ratecsstaff.model.StaffRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRatingRepository extends JpaRepository<StaffRating, Long> {

    Optional<StaffRating> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
