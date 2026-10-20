package com.cmpt276.ratecsstaff.model.profile;

import com.cmpt276.ratecsstaff.model.RoleType;

public final class StaffProfileFactory {

    private StaffProfileFactory() {

    }

    public static StaffMemberProfile forRole(RoleType roleType, String name) {
        return switch (roleType) {
            case PROF -> new ProfessorProfile(name);
            case TA -> new TeachingAssistantProfile(name);
            case INSTRUCTOR -> new InstructorProfile(name);
            case STAFF -> new GeneralStaffProfile(name);
        };
    }
}
