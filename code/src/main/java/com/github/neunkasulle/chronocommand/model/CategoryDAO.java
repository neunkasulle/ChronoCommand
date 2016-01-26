package com.github.neunkasulle.chronocommand.model;

/**
 * Created by jannis on 19.01.16.
 */
public class CategoryDAO {
    private static final CategoryDAO instance = new CategoryDAO();

    private CategoryDAO() {}

    public static CategoryDAO getInstance() {
        return instance;
    }

    public Category[] getAllCategories() {
        throw new UnsupportedOperationException();
    }

    public boolean newCategory(Category newCategory) {
        throw new UnsupportedOperationException();
    }
}
