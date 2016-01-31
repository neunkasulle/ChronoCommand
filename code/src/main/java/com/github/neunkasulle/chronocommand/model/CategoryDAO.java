package com.github.neunkasulle.chronocommand.model;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 *
 */
public class CategoryDAO {
    private static final CategoryDAO instance = new CategoryDAO();
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDAO.class);

    private CategoryDAO() {

    }

    public static CategoryDAO getInstance() {
        return instance;
    }

    public List<Category> getAllCategories() {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria all = session.createCriteria(Category.class);

        List<Category> categories = new ArrayList<>(all.list().size());
        for (Object obj : all.list()) {
            if (obj instanceof Category) {
                categories.add((Category) obj);
            } else {
                LOGGER.error("Element not instance of Category: {}", obj.toString());
            }
        }

        return categories;
    }

    public boolean saveCategory(Category category) {
        LOGGER.info("Saving category {}", category.getName());
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(category);
        tx.commit();
        session.flush();
        return true;
    }

    public Category findCategoryByString(String catAsString) {
        org.hibernate.Session session = DAOHelper.getInstance().getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Category.class).add(Restrictions.eq("name", catAsString));
        Object obj = criteria.uniqueResult();
        if (obj instanceof Category) {
            return (Category) obj;
        } else {
            return null;
        }
    }

}
