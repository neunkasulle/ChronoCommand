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
            LocalDateTime date1 = LocalDateTime.of(2016, 1, 1, 11, 30);
            LocalDateTime date2 = LocalDateTime.of(2016, 1, 1, 15, 30);
            TimeRecord timeRecTom = new TimeRecord(date1, date2, CategoryDAO.getInstance().findCategoryByString("Programming"), taetigkeit, tomTimeSheet);
            TimeRecord timeRecTom2 = new TimeRecord(LocalDateTime.of(2016, 1, 2, 8, 0), LocalDateTime.of(2016, 1, 2, 10, 30), CategoryDAO.getInstance().findCategoryByString("Procrastination"), "abgehangen", tomTimeSheet);

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

    private boolean isInitialStartup() {
        return false;
    }
}
