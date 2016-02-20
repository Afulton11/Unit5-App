package com.unit5app.com.unit5app.parsers;

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

    private String[] titles, descritions;

    /**
     * initializes the titles and descriptions for the Searcher, these are the items that the Searcher searches through when given a string.
     * @param titles - unparsed html titles
     * @param descriptions - unparsed html descriptions
     */
    public Searcher(String[] titles, String[] descriptions) {
        this.titles = titles;
        this.descritions = descriptions;
    }

    /**
     * initializes the titles and descriptions for the Searcher, these are the items that the Searcher searches through when given a string.
     * @param titles - unparsed html titles
     * @param descriptions - unparsed html descriptions
     */
    public Searcher(List<String> titles, List<String> descriptions) {
        this.titles = new String[titles.size() - 1];
        for(int i = 0; i < this.titles.length; i++) {
            this.titles[i] = titles.get(i);
        }

        this.descritions = new String[descriptions.size() - 1];
        for(int i = 0; i < this.descritions.length; i++) {
            this.descritions[i] = descriptions.get(i);
        }
    }

    /**
     * searches for a string in the searcher's titles and descriptions
     * @param string - the string to search for
     * @param titlesOnly - search through the titles only
     * @param descriptionsOnly - search through the descriptions only
     * @return
     * TODO: (backlog item?) searchFor() function.
     *              To search through both titles and descriptions, leave titlesOnly and descripionsOnly as true.
     */
    public List<String> searchFor(String string, boolean titlesOnly, boolean descriptionsOnly) {
        if(titlesOnly) {

        }
        if(descriptionsOnly) {

        }
        return null;
    }

    public String[] getTitles() {
        return titles;
    }

    public String[] getDescritions() {
        return descritions;
    }

}
