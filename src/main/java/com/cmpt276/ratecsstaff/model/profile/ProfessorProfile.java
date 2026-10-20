package com.cmpt276.ratecsstaff.model.profile;

public class ProfessorProfile extends StaffMemberProfile {

    public ProfessorProfile(String name) {
        super(name);
    }

    @Override
    public String displayTitle() {
        return "Prof. " + name;
    }

    @Override
    public String roleBadge() {
        return "PROF";
    }
}
