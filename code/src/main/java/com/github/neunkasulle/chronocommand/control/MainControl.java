package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.hibernate.internal.util.collections.IdentitySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;


/**
 * Created by Janze on 18.01.2016.
 * Controling startup and shutdown
 */
public class MainControl {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainControl.class);

    public static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";
    public static final String ROLE_SUPERVISOR = "SUPERVISOR";
    public static final String ROLE_PROLETARIER = "PROLETARIER";
    public static final String ROLE_LONGHOURS = "LONGHOURS";

    private boolean initialStartup = true;

    private static MainControl ourInstance = new MainControl();

    private MainControl() {

    }

    /**
     * Gets the one Instance of the MainControl.
     * @return The one MainControl instance.
     */
    public static MainControl getInstance() {
        return ourInstance;
    }

    /**
     * Initializes the application with all the needed
     */
    public void startup(boolean productionMode) {
        if (productionMode) {
            DAOHelper.getInstance().startup();
        } else {
            DAOHelper.getInstance().startup("hibernate-inmemory.cfg.xml");
        }

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // initialize roles
        Role administrator = UserDAO.getInstance().getRoleByName(ROLE_ADMINISTRATOR);
        if (administrator == null) {
            administrator = new Role(ROLE_ADMINISTRATOR, true);
            Set<String> administratorPermissions = new IdentitySet(1);
            administratorPermissions.add(Role.PERM_ADMINISTRATOR);
            administrator.setPermissions(administratorPermissions);
            UserDAO.getInstance().saveRole(administrator);
        }

        Role supervisor = UserDAO.getInstance().getRoleByName(ROLE_SUPERVISOR);
        if (supervisor == null) {
            supervisor = new Role(ROLE_SUPERVISOR, true);
            Set<String> supervisorPermissions = new IdentitySet(2);
            supervisorPermissions.add(Role.PERM_SUPERVISOR);
            supervisorPermissions.add(Role.PERM_PROLETARIER);
            supervisor.setPermissions(supervisorPermissions);
            UserDAO.getInstance().saveRole(supervisor);
        }

        Role proletarier = UserDAO.getInstance().getRoleByName(ROLE_PROLETARIER);
        if (proletarier == null) {
            proletarier = new Role(ROLE_PROLETARIER, true);
            Set<String> proletarierPermissions = new IdentitySet(1);
            proletarierPermissions.add(Role.PERM_PROLETARIER);
            proletarier.setPermissions(proletarierPermissions);
            UserDAO.getInstance().saveRole(proletarier);
        }

        Role longhours = UserDAO.getInstance().getRoleByName(ROLE_LONGHOURS);
        if (longhours == null) {
            longhours = new Role(ROLE_LONGHOURS, false);
            Set<String> longhourPermissions = new IdentitySet(1);
            longhourPermissions.add(Role.PERM_LONGHOURS);
            longhours.setPermissions(longhourPermissions);
            UserDAO.getInstance().saveRole(longhours);
        }

        // DEBUG fill database with data
        if (!productionMode) {
            try {
                CategoryDAO.getInstance().saveCategory(new Category("Programming"));
                CategoryDAO.getInstance().saveCategory(new Category("Procrastination"));
            } catch (ChronoCommandException e) {
                LOGGER.error("Saving categories failed: " + e.getReason().toString());
            }

            try {
                User bigboss = new User(administrator, "admin", "the@big.boss", "admin", "BigBoss", null, 0);
                UserDAO.getInstance().saveUser(bigboss);

                User tom = new User(supervisor, "tom", "tom@chronocommand.eu", "cat", "Tom", null, 23);
                UserDAO.getInstance().saveUser(tom);

                User matt = new User(proletarier, "matt", "matt@example.com", "matt", "Matt", tom, 10);
                UserDAO.getInstance().saveUser(matt);
            } catch (ChronoCommandException e) {
                LOGGER.error("Saving users failed: " + e.getReason().toString());
            }

            TimeSheet tomTimeSheet = new TimeSheet(UserDAO.getInstance().findUser("tom"), Month.JANUARY, 2016);
            TimeSheetDAO.getInstance().saveTimeSheet(tomTimeSheet);

            String taetigkeit = "codework for PSE";
            TimeRecord timeRecTom = new TimeRecord(LocalDateTime.of(2016, 1, 1, 11, 30), LocalDateTime.of(2016, 1, 1, 15, 30), CategoryDAO.getInstance().findCategoryByString("Programming"), taetigkeit, tomTimeSheet);
            TimeRecord timeRecTom2 = new TimeRecord(LocalDateTime.of(2016, 1, 2, 8, 0), LocalDateTime.of(2016, 1, 2, 10, 30), CategoryDAO.getInstance().findCategoryByString("Procrastination"), "abgehangen", tomTimeSheet);

            //test for more than 27 entries
            TimeSheet t = new TimeSheet(UserDAO.getInstance().findUser("tom"), Month.DECEMBER, 2016);
            TimeSheetDAO.getInstance().saveTimeSheet(t);
            LocalDateTime date1 = LocalDateTime.of(2016, 1, 1, 11, 30);
            LocalDateTime date2 = LocalDateTime.of(2016, 1, 1, 15, 30);
            TimeRecord t1 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 1", t);
            TimeRecord t2 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 2", t);
            TimeRecord t3 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 3", t);
            TimeRecord t4 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 4", t);
            TimeRecord t5 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 5", t);
            TimeRecord t6 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 6", t);
            TimeRecord t7 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 7", t);
            TimeRecord t8 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 8", t);
            TimeRecord t9 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 9", t);
            TimeRecord t0 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 10", t);
            TimeRecord t11 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 11", t);
            TimeRecord t22 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 12", t);
            TimeRecord t33 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 13", t);
            TimeRecord t44 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 14", t);
            TimeRecord t66 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 15", t);
            TimeRecord t55 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 16", t);
            TimeRecord t77 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 17", t);
            TimeRecord t88 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 18", t);
            TimeRecord t99 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 19", t);
            TimeRecord t09 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 20", t);
            TimeRecord t111 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 21", t);
            TimeRecord t12 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 22", t);
            TimeRecord t13 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 23", t);
            TimeRecord t14 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 24", t);
            TimeRecord t15 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 25", t);
            TimeRecord t16 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 26", t);
            TimeRecord t17 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 27", t);
            TimeRecord t21 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 28", t);
            TimeRecord t222 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 29", t);
            TimeRecord t23 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 30", t);
            TimeRecord t24 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 31", t);
            TimeRecord t25 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 32", t);
            TimeRecord t26 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 33", t);
            TimeRecord t27 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 34", t);
            TimeRecord t28 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 35", t);
            TimeRecord t29 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 36", t);
            TimeRecord t20 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 37", t);
            TimeRecord t08 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 38", t);
            TimeRecord t06 = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), "done something 39", t);

            try {
                TimeSheetDAO.getInstance().saveTimeRecord(t1);
                TimeSheetDAO.getInstance().saveTimeRecord(t2);
                TimeSheetDAO.getInstance().saveTimeRecord(t3);
                TimeSheetDAO.getInstance().saveTimeRecord(t4);
                TimeSheetDAO.getInstance().saveTimeRecord(t5);
                TimeSheetDAO.getInstance().saveTimeRecord(t6);
                TimeSheetDAO.getInstance().saveTimeRecord(t7);
                TimeSheetDAO.getInstance().saveTimeRecord(t8);
                TimeSheetDAO.getInstance().saveTimeRecord(t9);
                TimeSheetDAO.getInstance().saveTimeRecord(t0);
                TimeSheetDAO.getInstance().saveTimeRecord(t11);
                TimeSheetDAO.getInstance().saveTimeRecord(t111);
                TimeSheetDAO.getInstance().saveTimeRecord(t12);
                TimeSheetDAO.getInstance().saveTimeRecord(t13);
                TimeSheetDAO.getInstance().saveTimeRecord(t14);
                TimeSheetDAO.getInstance().saveTimeRecord(t15);
                TimeSheetDAO.getInstance().saveTimeRecord(t16);
                TimeSheetDAO.getInstance().saveTimeRecord(t17);
                TimeSheetDAO.getInstance().saveTimeRecord(t21);
                TimeSheetDAO.getInstance().saveTimeRecord(t22);
                TimeSheetDAO.getInstance().saveTimeRecord(t222);
                TimeSheetDAO.getInstance().saveTimeRecord(t23);
                TimeSheetDAO.getInstance().saveTimeRecord(t24);
                TimeSheetDAO.getInstance().saveTimeRecord(t25);
                TimeSheetDAO.getInstance().saveTimeRecord(t26);
                TimeSheetDAO.getInstance().saveTimeRecord(t27);
                TimeSheetDAO.getInstance().saveTimeRecord(t28);
                TimeSheetDAO.getInstance().saveTimeRecord(t29);
                TimeSheetDAO.getInstance().saveTimeRecord(t09);
                TimeSheetDAO.getInstance().saveTimeRecord(t33);
                TimeSheetDAO.getInstance().saveTimeRecord(t44);
                TimeSheetDAO.getInstance().saveTimeRecord(t55);
                TimeSheetDAO.getInstance().saveTimeRecord(t66);
                TimeSheetDAO.getInstance().saveTimeRecord(t20);
                TimeSheetDAO.getInstance().saveTimeRecord(t08);
                TimeSheetDAO.getInstance().saveTimeRecord(t09);
                TimeSheetDAO.getInstance().saveTimeRecord(t77);
                TimeSheetDAO.getInstance().saveTimeRecord(t88);
                TimeSheetDAO.getInstance().saveTimeRecord(t99);
                TimeSheetDAO.getInstance().saveTimeRecord(t06);
            } catch (Exception e) {
                LOGGER.error("LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOL");//serious error message
            }
            //end test

            try {
                TimeSheetDAO.getInstance().saveTimeRecord(timeRecTom);
                TimeSheetDAO.getInstance().saveTimeRecord(timeRecTom2);
            } catch (ChronoCommandException e) {
                LOGGER.error("Save time record failed", e);
            }
        }
    }

    /**
     * Saves necessary data before shutting down
     */
    public void shutdown() {
        DAOHelper.getInstance().shutdown();

    }

    /**
     * @return true if there is no administrator
     */
    public boolean isInitialStartup() {
        if (!initialStartup) {
            return false;
        }
        Role administrator = UserDAO.getInstance().getRoleByName(ROLE_ADMINISTRATOR);
        List<User> adminList = UserDAO.getInstance().getUsersByRole(administrator);
        initialStartup = adminList.isEmpty();
        return initialStartup;
    }
}
