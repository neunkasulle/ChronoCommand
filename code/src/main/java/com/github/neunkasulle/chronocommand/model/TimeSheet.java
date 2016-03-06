package com.github.neunkasulle.chronocommand.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Janze on 16.01.2016.
 *
 */
@Entity
@Table(name="cc_timesheets")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class TimeSheet implements Comparable<TimeSheet> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated
    @JoinTable(name = "cc_timesheet_messages")
    private List<Message> messages;

    @Basic(optional = false)
    private TimeSheetState state;

    @Basic(optional = false)
    private Month month;

    @Basic(optional = false)
    private int year;

    @Basic(optional = false)
    private int requiredHoursPerMonth;

    private int currentMinutesThisMonth = 0;

    protected TimeSheet() {
        // hibernate needs this
    }

    public TimeSheet(User user, Month month, int year) throws ChronoCommandException {
        if (user == null) {
            throw new ChronoCommandException(Reason.NOSUCHUSER);
        }
        this.user = user;
        this.month = month;
        this.year = year;
        this.requiredHoursPerMonth = this.user.getHoursPerMonth();
        this.state = TimeSheetState.UNLOCKED;
        this.messages = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public int getCurrentMinutesThisMonth() {
        return currentMinutesThisMonth;
    }

    public void setCurrentMinutesThisMonth(int currentMinutesThisMonth) {
        this.currentMinutesThisMonth = currentMinutesThisMonth;
    }

    public boolean setTimeSheetState(TimeSheetState state) {
        this.state = state;

        return true;
    }

    public User getUser() {
        return user;
    }

    public TimeSheetState getState() {
        return state;
    }

    public Month getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public int getRequiredHoursPerMonth() {
        return this.requiredHoursPerMonth;
    }


    @Override
    public String toString() {
        String monthStr;

        monthStr = month.toString();
        return monthStr + " " + year;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessage(Message message) {
        this.messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TimeSheet && id.equals(((TimeSheet) o).getId());
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param comparedTimeSheet the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(TimeSheet comparedTimeSheet) {

        return this.getLocalDate().compareTo(comparedTimeSheet.getLocalDate());
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(this.getYear(), this.getMonth(), 0);
    }
}
