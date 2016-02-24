package com.unit5app.com.unit5app.parsers;

import com.unit5app.Article;

import java.util.List;

/**
 * Created by Andrew on 2/19/2016.
 * This class is given 2 arrayLists of Strings which it then can be used to search through with a given String and boolean arguments such as 'titles only' or 'descriptions only'
 */
public class Searcher {

    /*
    The resulting found titles from a user search
     */
    private List<String> serachViewTitles;

    private Article[] articles;

    /**
     * initializes the titles and descriptions for the Searcher, these are the items that the Searcher searches through when given a string.
     * @param articles - parsed html articles
     */
    public Searcher(Article[] articles) {
        this.articles = articles;
    }

    /**
     * initializes the titles and descriptions for the Searcher, these are the items that the Searcher searches through when given a string.
     * @param articles - parsed html articles
     */
    public Searcher(List<Article> articles) {
        this.articles = new Article[articles.size()];
        for(int index = 0; index < this.articles.length; index++) {
            this.articles[index] = articles.get(index);
        }
    }

    /**
     * searches for a string in the searcher's titles and descriptions
     * @param string - the string to search for
     * @param titlesOnly - search through the titles only
     * @param descriptionsOnly - search through the descriptions only
     * @return
     * TODO: (backlog item) searchFor() function.
     *              To search through both titles and descriptions, leave titlesOnly and descripionsOnly as true.
     */
    public List<String> searchFor(String string, boolean titlesOnly, boolean descriptionsOnly) {
        if(titlesOnly) {

        }
        if(descriptionsOnly) {

        }
        return null;
    }

    public Article[] getArticles() {
        return articles;
    }

}
