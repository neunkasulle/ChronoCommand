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

        Regulations.startup(new GermanLawRegulations());

        // initialize roles
        Role administrator = UserDAO.getInstance().getRoleByName(ROLE_ADMINISTRATOR);
        if (administrator == null) {
            administrator = new Role(ROLE_ADMINISTRATOR, "Administrator", true);
            Set<String> administratorPermissions = new IdentitySet(1);
            administratorPermissions.add(Role.PERM_ADMINISTRATOR);
            administrator.setPermissions(administratorPermissions);
            UserDAO.getInstance().saveRole(administrator);
        }

        Role supervisor = UserDAO.getInstance().getRoleByName(ROLE_SUPERVISOR);
        if (supervisor == null) {
            supervisor = new Role(ROLE_SUPERVISOR, "Supervisor", true);
            Set<String> supervisorPermissions = new IdentitySet(2);
            supervisorPermissions.add(Role.PERM_SUPERVISOR);
            supervisorPermissions.add(Role.PERM_PROLETARIER);
            supervisor.setPermissions(supervisorPermissions);
            UserDAO.getInstance().saveRole(supervisor);
        }

        Role proletarier = UserDAO.getInstance().getRoleByName(ROLE_PROLETARIER);
        if (proletarier == null) {
            proletarier = new Role(ROLE_PROLETARIER, "HIWI", true);
            Set<String> proletarierPermissions = new IdentitySet(1);
            proletarierPermissions.add(Role.PERM_PROLETARIER);
            proletarier.setPermissions(proletarierPermissions);
            UserDAO.getInstance().saveRole(proletarier);
        }

        Role longhours = UserDAO.getInstance().getRoleByName(ROLE_LONGHOURS);
        if (longhours == null) {
            longhours = new Role(ROLE_LONGHOURS, "Long hours allowed", false);
            Set<String> longhourPermissions = new IdentitySet(1);
            longhourPermissions.add(Role.PERM_LONGHOURS);
            longhours.setPermissions(longhourPermissions);
            UserDAO.getInstance().saveRole(longhours);
        }

        SchedulerHandler.scheduleAll();

        // DEBUG fill database with data
        if (!productionMode) {
            try {
                CategoryDAO.getInstance().saveCategory(new Category("Programming"));
                CategoryDAO.getInstance().saveCategory(new Category("Procrastination"));
            } catch (ChronoCommandException e) {
                LOGGER.error("Saving categories failed: " + e.getReason().toString(), e);
            }

            try {
                User bigboss = new User(administrator, "admin", "the@big.boss", "admin", "BigBoss", null, 0);
                UserDAO.getInstance().saveUser(bigboss);

                User tom = new User(supervisor, "tom", "tom@chronocommand.eu", "cat", "Tom", null, 23);
                UserDAO.getInstance().saveUser(tom);
                tom.setSupervisor(tom);
                UserDAO.getInstance().saveUser(tom);

                User matt = new User(proletarier, "matt", "matt@example.com", "matt", "Matt", tom, 10);
                UserDAO.getInstance().saveUser(matt);

            }
            catch (ChronoCommandException e) {
                LOGGER.error("Saving users failed: " + e.getReason().toString(), e);
            }
            try {
                TimeSheet tomTimeSheet = new TimeSheet(UserDAO.getInstance().findUser("tom"), Month.JANUARY, 2016);
                TimeSheetDAO.getInstance().saveTimeSheet(tomTimeSheet);
                String taetigkeit = "codework for PSE";
                TimeRecord timeRecTom = new TimeRecord(LocalDateTime.of(2016, 1, 4, 11, 30),
                        LocalDateTime.of(2016, 1, 4, 15, 30), CategoryDAO.getInstance().findCategoryByString("Programming"), taetigkeit, tomTimeSheet);
                TimeRecord timeRecTom2 = new TimeRecord(LocalDateTime.of(2016, 1, 7, 8, 0),
                        LocalDateTime.of(2016, 1, 7, 10, 30), CategoryDAO.getInstance().findCategoryByString("Procrastination"), "abgehangen", tomTimeSheet);
                TimeSheetDAO.getInstance().saveTimeRecord(timeRecTom2);
                TimeSheetDAO.getInstance().saveTimeRecord(timeRecTom);

                TimeSheet mattTimeSheet = new TimeSheet(UserDAO.getInstance().findUser("matt"), Month.FEBRUARY, 2016);
                TimeSheetDAO.getInstance().saveTimeSheet(mattTimeSheet);

                TimeRecord timeRecMatt = new TimeRecord(LocalDateTime.of(2016, 2, 4, 10, 0), LocalDateTime.of(2016, 2, 4, 15, 23),
                        CategoryDAO.getInstance().findCategoryByString("Procrastination"), taetigkeit, mattTimeSheet);
                TimeSheetDAO.getInstance().saveTimeRecord(timeRecMatt);
            }
            catch (ChronoCommandException e) {
                    LOGGER.error("Save time record failed", e);
                }

            //stop deleting
        }
    }

    /**
     * Saves necessary data before shutting down
     */
    public void shutdown() {
        SchedulerHandler.getInstance().shutdownSched();
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
