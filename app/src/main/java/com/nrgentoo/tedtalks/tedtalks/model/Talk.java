package com.nrgentoo.tedtalks.tedtalks.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Talk object
 */
@DatabaseTable(tableName = "talks")
@Root(name = "item", strict = false)
public class Talk {

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

    // --------------------------------------------------------------------------------------------
    //      CONSTRUCTOR
    // --------------------------------------------------------------------------------------------
    public Talk() {
        // do nothing
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

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
