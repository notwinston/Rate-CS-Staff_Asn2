package com.cmpt276.ratecsstaff.repository;

import com.cmpt276.ratecsstaff.model.RoleType;
import com.cmpt276.ratecsstaff.model.StaffRating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class StaffRatingRepositoryTest {

    @Autowired
    private StaffRatingRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private StaffRating sampleRating(String email) {
        StaffRating r = new StaffRating();
        r.setName("Ada Lovelace");
        r.setEmail(email);
        r.setRoleType(RoleType.PROF);
        r.setClarity(8);
        r.setNiceness(9);
        r.setKnowledgeableScore(10);
        r.setComment("Clear lectures.");
        return r;
    }

    @Test
    void savingAssignsIdAndTimestampsThenCanBeRetrieved() {
        StaffRating saved = repository.save(sampleRating("ada@sfu.ca"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        Optional<StaffRating> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ada Lovelace");
        assertThat(found.get().getEmail()).isEqualTo("ada@sfu.ca");
        assertThat(found.get().getRoleType()).isEqualTo(RoleType.PROF);
    }

    @Test
    void findAllReturnsSavedEntries() {
        repository.save(sampleRating("ada@sfu.ca"));
        repository.save(sampleRating("grace@sfu.ca"));

        List<StaffRating> all = repository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void deleteRemovesTheEntry() {
        StaffRating saved = repository.save(sampleRating("ada@sfu.ca"));
        Long id = saved.getId();

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void findByEmailIgnoreCaseMatchesRegardlessOfCase() {
        repository.save(sampleRating("Ada@SFU.ca"));

        Optional<StaffRating> found = repository.findByEmailIgnoreCase("ada@sfu.ca");

        assertThat(found).isPresent();
    }

    @Test
    void duplicateEmailViolatesUniqueConstraint() {
        repository.save(sampleRating("ada@sfu.ca"));

        StaffRating duplicate = sampleRating("ada@sfu.ca");

        assertThatThrownBy(() -> {
            repository.save(duplicate);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
