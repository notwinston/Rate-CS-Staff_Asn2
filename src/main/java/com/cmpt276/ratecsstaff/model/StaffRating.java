package com.cmpt276.ratecsstaff.model;

import com.cmpt276.ratecsstaff.model.profile.StaffMemberProfile;
import com.cmpt276.ratecsstaff.model.profile.StaffProfileFactory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "staff_rating",
        uniqueConstraints = @UniqueConstraint(name = "uk_staff_rating_email", columnNames = "email")
)
public class StaffRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    @Size(max = 150, message = "Email must be at most 150 characters.")
    @Column(nullable = false, length = 150)
    private String email;

    @NotNull(message = "Please choose a role.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleType roleType;

    @NotNull(message = "Clarity score is required.")
    @Min(value = 1, message = "Clarity must be between 1 and 10.")
    @Max(value = 10, message = "Clarity must be between 1 and 10.")
    @Column(nullable = false)
    private Integer clarity;

    @NotNull(message = "Niceness score is required.")
    @Min(value = 1, message = "Niceness must be between 1 and 10.")
    @Max(value = 10, message = "Niceness must be between 1 and 10.")
    @Column(nullable = false)
    private Integer niceness;

    @NotNull(message = "Knowledge score is required.")
    @Min(value = 1, message = "Knowledge must be between 1 and 10.")
    @Max(value = 10, message = "Knowledge must be between 1 and 10.")
    @Column(nullable = false)
    private Integer knowledgeableScore;

    @Size(max = 500, message = "Comment must be at most 500 characters.")
    @Column(length = 500)
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public StaffRating() {

    }

    @Transient
    public StaffMemberProfile getProfile() {
        if (roleType == null || name == null) {
            return null;
        }
        return StaffProfileFactory.forRole(roleType, name);
    }

    @Transient
    public String getDisplayTitle() {
        StaffMemberProfile profile = getProfile();
        return profile == null ? name : profile.displayTitle();
    }

    @Transient
    public Double getOverallScore() {
        if (clarity == null || niceness == null || knowledgeableScore == null) {
            return null;
        }
        double average = (clarity + niceness + knowledgeableScore) / 3.0;
        return Math.round(average * 10.0) / 10.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Integer getClarity() {
        return clarity;
    }

    public void setClarity(Integer clarity) {
        this.clarity = clarity;
    }

    public Integer getNiceness() {
        return niceness;
    }

    public void setNiceness(Integer niceness) {
        this.niceness = niceness;
    }

    public Integer getKnowledgeableScore() {
        return knowledgeableScore;
    }

    public void setKnowledgeableScore(Integer knowledgeableScore) {
        this.knowledgeableScore = knowledgeableScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
