package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 30.01.2016.
 * Enum for internal exceptions reasons
 */
public enum Reason {
    BADCREDENTIALS("Username or password incorrect"),
    PROJECTNOTFOUND("Project not found"),
    MISSINGPROJECT("Missing project"),
    MISSINGDESCRIPTION("Missing description"),
    CATEGORYALREADYSPECIFIED("Category already specified"),
    USERALREADYEXISTS("User already exists"),
    EMAILALREADYINUSE("Email already "),
    NOSUCHUSER("No such user"),
    INVALIDNUMBER("Invalid number"),
    INVALIDSTRING("Invalid string"),
    INVALIDSOMETHING("Invalid value"),
    NOTLOGGEDIN("Not logged in"),
    TIMESHEETLOCKED("Timesheet locked"),
    NOTPERMITTED("Performed action not permitted"),
    INVALIDEMAIL("Invalid email address"),
    USERNAMETAKEN("Username already taken"),
    STRINGTOOLONG("Input string is too long"),
    INVALIDHOURSPERMONTH("Invalid number of hours per month"),
    USERDISABLED("User account is disabled"),
    TIMESHEETINCOMPLETE("Timesheet is incomplete"),
    EMPTYDESC("Description is empty"),
    ;

    private final String text;

    Reason(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
