package com.unit5app;

/**
 * This class holds the title and description of an article.
 * @author Andrew
 * @version 2/22/16
 */
public class Article {

    private String title, description;

    /**
     * creates a new blank Article, The title and description are set to null.
     */
    public Article() {
        this.title = null;
        this.description = null;
    }

    /**
     * Creates a new Article
     * @param title - the Title of the Article
     * @param description - the Description, or body, of the Article.
     */
    public Article(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Set/change the title of the Article.
     * @param title - title to set/change the article to.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set/change the description of the Article.
     * @param description - description to set/change the areticle to.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
