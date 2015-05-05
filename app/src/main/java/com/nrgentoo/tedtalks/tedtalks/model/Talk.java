package com.nrgentoo.tedtalks.tedtalks.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Talk object
 */
@DatabaseTable(tableName = "talks")
@Root(name = "item", strict = false)
public class Talk implements Serializable {

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------
    @DatabaseField(id = true)
    @Element
    long talkId;

    @DatabaseField
    @Element
    String title;

    @DatabaseField
    @Element
    String author;

    @DatabaseField
    @Element
    String description;

    @DatabaseField
    @Element
    String subtitle;

    @DatabaseField
    @Element
    String summary;

    @DatabaseField
    @Path("enclosure")
    @Attribute(name = "url")
    String fileUrl;

    @DatabaseField
    @Element
    String pubDate;

    @DatabaseField
    long unixPubDate;

    String shortPubDate;

    @DatabaseField
    @Element
    String category;

    @DatabaseField
    @Path("image")
    @Attribute(name = "href")
    String image;

    @DatabaseField
    @Path("thumbnail")
    @Attribute(name = "url")
    String thumbnail;

    @DatabaseField
    @Element
    String duration;

    // --------------------------------------------------------------------------------------------
    //      CONSTRUCTOR
    // --------------------------------------------------------------------------------------------
    public Talk() {
        // do nothing
    }

    // --------------------------------------------------------------------------------------------
    //      PUBLIC METHODS
    // --------------------------------------------------------------------------------------------
    public void initUnixPubDate() {
        try {
            Date date = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z").parse(pubDate);
            unixPubDate = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------------------------------------
    //      ACCESSORS
    // --------------------------------------------------------------------------------------------
    public long getTalkId() {
        return talkId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getSummary() {
        return summary;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getPubDate() {
        return pubDate;
    }

    public long getUnixPubDate() {
        return unixPubDate;
    }

    public String getShortPubDate() {
        if (shortPubDate == null) {
            shortPubDate = new SimpleDateFormat("E, d MMM yyy").format(new Date(unixPubDate));
        }
        return shortPubDate;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDuration() {
        return duration;
    }
}
