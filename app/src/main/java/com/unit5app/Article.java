package com.unit5app;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class holds the title and description of an article.
 * @author Andrew
 * @version 2/22/16
 */
public class Article implements Parcelable{

    private String title, description, pubDate, link;

    /**
     * creates a new blank Article, The title description, and publishing date are all set to null.
     */
    public Article() {
        this.title = "";
        this.description = "";
        this.pubDate = "";
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

    /**
     * Creates a new Article
     * @param title - the Title of the Article
     * @param description - the Description, or body, of the Article.
     * @param pubDate - the publishing date of the article
     */
    public Article(String title, String description, String pubDate) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() { return pubDate; }

    /**
     * The link is used only for things like loading westNews from the homepage!
     * @return -- returns the first link that was found in the article when parsing.
     */
    public String getLink() { return link; }

    /**
     * Set/change the title of the Article.
     * @param title - title to set/change the article to.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set/change the description of the Article.
     * @param description - description to set/change the article to.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set/change the publishing date of the Article.
     * @param pubDate - publishing date to set/change the article to.
     */
    public void setPubDate(String pubDate) {
        if(pubDate != null) {
            if (pubDate.contains("/")) {
                this.pubDate = pubDate;
            } else {
                SimpleDateFormat format;
                if(pubDate.contains(",")) {
                    format = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss z");
                } else {
                    format = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy");
                }
                Date date = null;
                try {
                    date = format.parse(pubDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date != null) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST")); //forces Central Standard Time, the date automatically changes time to CST anyways.
                    calendar.setTime(date);
                    int year = calendar.get(Calendar.YEAR) - 2000;
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); //may have to add one to the day, not really sure.
                    int month = calendar.get(Calendar.MONTH) + 1;
                    this.pubDate = month + "/" + dayOfMonth + "/" + year;
                }
            }
        }
    }

    public void setLink(String link) { this.link = link; }

    /**
     * returns true if the article is full.
     * @return - true if the article is full --> An article is considered 'full' when the article contains both a non-empty title and description.
     */
    public boolean isArticleFull() {
        return (title != null && description != null && !title.equals("") && !description.equals(""));
    }

    /**
     * returns true if the article contains a pubDate
     * @return - true if the article contains a pubDate --> the pubDate is not equal to null or blank text ("") it will return true.
     */
    public boolean hasPubDate() {
        return (pubDate != null && !pubDate.equals(""));
    }


    /*
    CODE BELOW IS FOR SAVED INSTANCE STATES IN THE TITLE LIST ACTIVITY!
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString("%-%-%" + description);
        if (this.hasPubDate()) dest.writeString("%-%-%" + pubDate);
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            String[] parcelArray = in.createStringArray();
            String pubDate = (parcelArray.length > 1) ? parcelArray[2] : null;
            return new Article(parcelArray[0], parcelArray[1], pubDate);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
