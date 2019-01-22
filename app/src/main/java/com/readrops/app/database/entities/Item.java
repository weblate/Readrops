package com.readrops.app.database.entities;

import android.arch.persistence.room.*;

import com.readrops.readropslibrary.localfeed.atom.ATOMEntry;
import com.readrops.readropslibrary.localfeed.json.JSONItem;
import com.readrops.readropslibrary.localfeed.rss.RSSChannel;
import com.readrops.readropslibrary.localfeed.rss.RSSItem;

import java.util.ArrayList;
import java.util.List;



@Entity
(foreignKeys = @ForeignKey(entity = Feed.class, parentColumns = "id", childColumns = "feed_id"))
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private String link;

    @ColumnInfo(name = "image_link")
    private String imageLink;

    private String author;

    @ColumnInfo(name = "pub_date")
    private String pubDate;

    private String content;

    @ColumnInfo(name = "feed_id", index = true)
    private int feedId;

    @ColumnInfo(index = true)
    private String guid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public static List<Item> itemsFromRSS(List<RSSItem> items, Feed feed) {
        List<Item> dbItems = new ArrayList<>();

        for(RSSItem item : items) {
            Item newItem = new Item();

            newItem.setAuthor(item.getAuthor());
            newItem.setContent(item.getContent());
            newItem.setDescription(item.getDescription());
            newItem.setGuid(item.getGuid());
            newItem.setTitle(item.getTitle());
            newItem.setImageLink(item.getImageLink());
            newItem.setPubDate(item.getPubDate());
            newItem.setLink(item.getLink());

            newItem.setFeedId(feed.getId());

            dbItems.add(newItem);
        }

        return dbItems;
    }

    public static List<Item> itemsFromATOM(List<ATOMEntry> items, Feed feed) {
        List<Item> dbItems = new ArrayList<>();

        for (ATOMEntry item : items) {
            Item dbItem = new Item();

            dbItem.setContent(item.getContent());
            dbItem.setDescription(item.getSummary());
            dbItem.setGuid(item.getId());
            dbItem.setTitle(item.getTitle());
            dbItem.setPubDate(item.getUpdated());
            dbItem.setLink(item.getLink().getHref());

            dbItem.setFeedId(feed.getId());
        }

        return dbItems;
    }

    public static List<Item> itemsFromJSON(List<JSONItem> items, Feed feed) {
        List<Item> dbItems = new ArrayList<>();

        for (JSONItem item : items) {
            Item dbItem = new Item();

            dbItem.setAuthor(item.getAuthor().getName());
            dbItem.setContent(item.getContent());
            dbItem.setDescription(item.getSummary());
            dbItem.setGuid(item.getId());
            dbItem.setTitle(item.getTitle());
            dbItem.setPubDate(item.getPubDate());
            dbItem.setLink(item.getUrl());

            dbItem.setFeedId(feed.getId());
        }

        return dbItems;
    }
}
