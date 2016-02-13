package com.github.neunkasulle.chronocommand.model;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 *  Data Access Object to store categories
 */
public class CategoryDAO {
    private static final CategoryDAO instance = new CategoryDAO();
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDAO.class);

    private CategoryDAO() {

    }

    /**
     * Gets the CategoryDAO instance
     * @return the one instance of CategoryDAO
     */
    public static CategoryDAO getInstance() {
        return instance;
    }

    /**
     * gets all categories
     * @return A list of all categories
     */
    public List<Category> getAllCategories() {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria all = session.createCriteria(Category.class);

        List<Category> categories = new ArrayList<>(all.list().size());
        for (Object obj : all.list()) {
            categories.add((Category) obj);
        }

        return categories;
    }

    /**
     * Adds a new category to the DB
     * @param category the category which is to be added
     */
    public void saveCategory(Category category) throws ChronoCommandException {
        if (findCategoryByString(category.getName()) != null) {
            LOGGER.warn("Category with the same name already exists.");
            throw new ChronoCommandException(Reason.CATEGORYALREADYSPECIFIED);
        }

        LOGGER.info("Saving category {}", category.getName());
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(category);
        tx.commit();
        session.flush();
        session.close();
    }

    /**
     * Finds a category by its representing string
     * @param catAsString the string of category which should be found
     * @return The found category Object
     */
    public Category findCategoryByString(String catAsString) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Category.class).add(Restrictions.eq("name", catAsString));
        Object obj = criteria.uniqueResult();
        return (Category) obj;
    }

}
