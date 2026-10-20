package com.cmpt276.ratecsstaff.service;

import com.cmpt276.ratecsstaff.model.StaffRating;
import com.cmpt276.ratecsstaff.repository.StaffRatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StaffRatingService {

    private final StaffRatingRepository repository;

    public StaffRatingService(StaffRatingRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<StaffRating> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<StaffRating> findById(Long id) {
        return repository.findById(id);
    }

    public StaffRating save(StaffRating rating) {
        return repository.save(rating);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean isEmailTakenByOther(String email, Long currentId) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return repository.findByEmailIgnoreCase(email.trim())
                .map(existing -> !existing.getId().equals(currentId))
                .orElse(false);
    }
}
