package com.github.neunkasulle.chronocommand.model;

import org.hibernate.*;
import org.hibernate.Session;
import org.hibernate.cfg.CreateKeySecondPass;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 */
public class TimeSheetDAO {
    private static final TimeSheetDAO instance = new TimeSheetDAO();

    private TimeSheetDAO() {}

    public static TimeSheetDAO getInstance() {
        return instance;
    }

    public TimeSheet getTimeSheet(Month month, int year, User user) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TimeSheet.class);
        criteria.add(Restrictions.eq("month", month)).add(Restrictions.eq("year", year)).add(Restrictions.eq("user", user));
        Object obj = criteria.uniqueResult();
        if (obj instanceof TimeSheet) {
            return (TimeSheet) obj;
        } else {
            return null;
        }

    }

    public List<TimeRecord> getTimeRecords(TimeSheet timeSheet) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TimeRecord.class).add(Restrictions.eq("timeSheet", timeSheet ));
        List<TimeRecord> timeRecordList = new ArrayList<>();
        for (Object obj : criteria.list()) {
            if (obj instanceof TimeRecord) {
                timeRecordList.add((TimeRecord) obj);
            }
        }
        return timeRecordList;
    }

    public List<TimeRecord> getTimeRecordsByDay(TimeSheet timeSheet, int dayOfMonth) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TimeRecord.class);
        LocalDateTime day = LocalDateTime.of(timeSheet.year, timeSheet.month, dayOfMonth, 0,0);
        LocalDateTime nextDay = day.plusDays(1);
        criteria.add(Restrictions.ge("beginning", day)).add(Restrictions.lt("beginning", nextDay));
        List<TimeRecord> list = new ArrayList<>();
        for (Object obj : criteria.list()) {
            list.add((TimeRecord) obj);
        }
        return list;
    }

    public List<TimeSheet> getAllUnlockedTimeSheets() {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TimeSheet.class).add(Restrictions.eq("state", TimeSheetState.UNLOCKED));
        List<TimeSheet> list = new ArrayList<>();
        for (Object obj : criteria.list()) {
            list.add((TimeSheet) obj);
        }
        return list;
    }

    public boolean saveTimeSheet(TimeSheet timeSheet) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(timeSheet);
        tx.commit();
        session.flush();
        boolean red = true; // das ist nur da, damit intellij nicht wegen code dublikaten rummotz!
        return red;
    }

    public List<TimeSheet> getAllTimeSheets(Month month, int year) {
        UserDAO userDAO = UserDAO.getInstance();
        List<User> users = userDAO.getAllUsers();

        List<TimeSheet> timeSheets = new LinkedList<>();

        for(User user: users) {

             timeSheets.add(getTimeSheet(month, year, user));
        }
        return timeSheets;
    }

    public List<TimeSheet> getTimeSheetsFromUser(User user) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TimeSheet.class).add(Restrictions.eq("user", user));
        List<TimeSheet> list = new ArrayList<>();
        for (Object obj : criteria.list()) {
            list.add((TimeSheet) obj);
        }

        return list;
    }

    public TimeSheet getLatestTimeSheet(User user) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(TimeSheet.class).add(Restrictions.eq("user", user));
        criteria.addOrder(Order.desc("year")).addOrder(Order.desc("month"));
        if (criteria.list().isEmpty()) {
            return null;
        }

        Object obj = criteria.list().get(0);
        return (TimeSheet) obj;
    }

    public TimeRecord getLatestTimeRecord(User user) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        TimeSheet latestTimeSheet = getLatestTimeSheet(user);
        Criteria criteria = session.createCriteria(TimeRecord.class).add(Restrictions.eq("timeSheet", latestTimeSheet)).addOrder(Order.desc("beginning"));
        if (criteria.list().isEmpty()) {
            return null;
        }

        Object obj = criteria.list().get(0);
        return (TimeRecord) obj;
    }

    public void saveTimeRecord(TimeRecord timeRecord) {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(timeRecord);
        tx.commit();
        session.flush();
    }

    public TimeSheetHandler getTimeSheetHandler() throws ChronoCommandException {
        Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Object obj = session.createCriteria(TimeSheetHandler.class).uniqueResult();
        if (obj instanceof TimeSheetHandler) {
            return (TimeSheetHandler) obj;
        }
        throw new ChronoCommandException();
    }
}
