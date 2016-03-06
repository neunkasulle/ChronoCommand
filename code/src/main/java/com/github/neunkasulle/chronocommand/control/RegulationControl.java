package com.github.neunkasulle.chronocommand.control;

/**
 * Created by Janze on 17.01.2016.
 * Checks Time Sheets for regulation violations
 */
public class RegulationControl {

    private static RegulationControl ourInstance = new RegulationControl();

    private RegulationControl() {

    }

    /**
     * Gets the one Instance of the RegulationControl.
     *
     * @return The one RegulationControl instance.
     */
    public static RegulationControl getInstance() {
        return ourInstance;
    }

}
