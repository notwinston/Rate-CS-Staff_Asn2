package com.cmpt276.ratecsstaff.model.profile;

public abstract class StaffMemberProfile {

    protected final String name;

    protected StaffMemberProfile(String name) {
        this.name = name;
    }

    public abstract String displayTitle();

    public abstract String roleBadge();

    public String getName() {
        return name;
    }

    public String getDisplayTitle() {
        return displayTitle();
    }

    public String getRoleBadge() {
        return roleBadge();
    }
}
