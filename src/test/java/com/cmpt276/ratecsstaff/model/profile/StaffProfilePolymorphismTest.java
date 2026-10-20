package com.cmpt276.ratecsstaff.model.profile;

import com.cmpt276.ratecsstaff.model.RoleType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StaffProfilePolymorphismTest {

    @Test
    void factoryReturnsProfessorProfileForProf() {
        StaffMemberProfile profile = StaffProfileFactory.forRole(RoleType.PROF, "Ada Lovelace");

        assertThat(profile).isInstanceOf(ProfessorProfile.class);
        assertThat(profile.displayTitle()).isEqualTo("Prof. Ada Lovelace");
        assertThat(profile.roleBadge()).isEqualTo("PROF");
    }

    @Test
    void factoryReturnsTaProfileForTa() {
        StaffMemberProfile profile = StaffProfileFactory.forRole(RoleType.TA, "Grace Hopper");

        assertThat(profile).isInstanceOf(TeachingAssistantProfile.class);
        assertThat(profile.displayTitle()).isEqualTo("Grace Hopper (TA)");
    }

    @Test
    void differentRolesProduceDifferentTitlesForSameName() {
        String name = "Alan Turing";

        String profTitle = StaffProfileFactory.forRole(RoleType.PROF, name).displayTitle();
        String taTitle = StaffProfileFactory.forRole(RoleType.TA, name).displayTitle();
        String instructorTitle = StaffProfileFactory.forRole(RoleType.INSTRUCTOR, name).displayTitle();
        String staffTitle = StaffProfileFactory.forRole(RoleType.STAFF, name).displayTitle();

        assertThat(profTitle).isEqualTo("Prof. Alan Turing");
        assertThat(taTitle).isEqualTo("Alan Turing (TA)");
        assertThat(instructorTitle).isEqualTo("Instructor Alan Turing");
        assertThat(staffTitle).isEqualTo("Alan Turing, Staff");
    }
}
