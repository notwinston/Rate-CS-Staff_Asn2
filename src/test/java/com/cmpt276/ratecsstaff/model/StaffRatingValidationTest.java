package com.cmpt276.ratecsstaff.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class StaffRatingValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    private StaffRating validRating() {
        StaffRating r = new StaffRating();
        r.setName("Ada Lovelace");
        r.setEmail("ada@sfu.ca");
        r.setRoleType(RoleType.PROF);
        r.setClarity(8);
        r.setNiceness(9);
        r.setKnowledgeableScore(10);
        r.setComment("Clear and patient.");
        return r;
    }

    @Test
    void validRatingHasNoViolations() {
        Set<ConstraintViolation<StaffRating>> violations = validator.validate(validRating());
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidEmailIsRejected() {
        StaffRating r = validRating();
        r.setEmail("not-an-email");

        Set<ConstraintViolation<StaffRating>> violations = validator.validate(r);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");
    }

    @Test
    void scoreAboveRangeIsRejected() {
        StaffRating r = validRating();
        r.setClarity(11);

        Set<ConstraintViolation<StaffRating>> violations = validator.validate(r);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("clarity");
    }

    @Test
    void scoreBelowRangeIsRejected() {
        StaffRating r = validRating();
        r.setNiceness(0);

        Set<ConstraintViolation<StaffRating>> violations = validator.validate(r);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("niceness");
    }

    @Test
    void missingNameIsRejected() {
        StaffRating r = validRating();
        r.setName("   ");

        Set<ConstraintViolation<StaffRating>> violations = validator.validate(r);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("name");
    }

    @Test
    void missingRequiredFieldsAreRejected() {
        StaffRating r = new StaffRating();

        Set<ConstraintViolation<StaffRating>> violations = validator.validate(r);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("name", "email", "roleType", "clarity", "niceness", "knowledgeableScore");
    }

    @Test
    void overlyLongCommentIsRejected() {
        StaffRating r = validRating();
        r.setComment("x".repeat(501));

        Set<ConstraintViolation<StaffRating>> violations = validator.validate(r);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("comment");
    }
}
