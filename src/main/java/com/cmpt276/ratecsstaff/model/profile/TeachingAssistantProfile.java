package com.cmpt276.ratecsstaff.model.profile;

public class TeachingAssistantProfile extends StaffMemberProfile {

    public TeachingAssistantProfile(String name) {
        super(name);
    }

    @Override
    public String displayTitle() {
        return name + " (TA)";
    }

    @Override
    public String roleBadge() {
        return "TA";
    }
}
