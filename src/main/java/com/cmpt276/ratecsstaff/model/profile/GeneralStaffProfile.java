package com.cmpt276.ratecsstaff.model.profile;

public class GeneralStaffProfile extends StaffMemberProfile {

    public GeneralStaffProfile(String name) {
        super(name);
    }

    @Override
    public String displayTitle() {
        return name + ", Staff";
    }

    @Override
    public String roleBadge() {
        return "STAFF";
    }
}
