package com.github.neunkasulle.chronocommand.model;

public class RegulationRejectionReason implements Comparable {
    public enum RejectionReason {
        NIGHTWORK("Nightwork not allowed"),
        BREAK("After working for 6 hours, you have to take a break"),
        SUNDAY_WORK("Working on sundays is not allowed"),
        HOLIDAY_WORK("Working on holidays is not allowed"),
        MAXIMUM_WORKTIME("Maximum worktime exceeded"),
        ;

        private final String text;

        RejectionReason(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private TimeRecord timeRecord;
    private RejectionReason rejectionReason;

    RegulationRejectionReason(TimeRecord timeRecord, RejectionReason rejectionReason) {
        this.timeRecord = timeRecord;
        this.rejectionReason = rejectionReason;
    }

    public TimeRecord getTimeRecord() {
        return timeRecord;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof RegulationRejectionReason) {
            return timeRecord.compareTo(((RegulationRejectionReason) o).getTimeRecord());
        }
        return 0;
    }
}
