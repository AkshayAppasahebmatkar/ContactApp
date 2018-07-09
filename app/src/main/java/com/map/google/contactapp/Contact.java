package com.map.google.contactapp;

/**
 * Created by levia4 on 25-05-2017.
 */

public class Contact {
    int contact_id;
    String contact_name;
    String contact_number;
    String images;
    String favourite_item;
    String deleted_item;
    // Default Constructor
    public Contact() {
    }

    // Constructor with parameter list
    public Contact(int contact_id, String contact_name, String contact_number, String images, String favourite_item, String deleted_item) {
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.images = images;
        this.favourite_item = favourite_item;
        this.deleted_item = deleted_item;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getFavourite_item() {
        return favourite_item;
    }

    public void setFavourite_item(String favourite_item) {
        this.favourite_item = favourite_item;
    }

    public String getDeleted_item() {
        return deleted_item;
    }

    public void setDeleted_item(String deleted_item) {
        this.deleted_item = deleted_item;
    }

}
