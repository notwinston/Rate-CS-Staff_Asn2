package com.cmpt276.ratecsstaff.model.profile;

public class InstructorProfile extends StaffMemberProfile {

    public InstructorProfile(String name) {
        super(name);
    }

    @Override
    public String displayTitle() {
        return "Instructor " + name;
    }

    @Override
    public String roleBadge() {
        return "INSTRUCTOR";
    }
}
