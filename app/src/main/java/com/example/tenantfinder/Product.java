package com.example.tenantfinder;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    /*private int id;*/
    private String title="", shortdesc="",price="",rating="";

//    private int image;
    public Product(){};

    public Product( String title, String shortdesc, String rating, String price){
//        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating ;
        this.price = price;
//        this.image = image;
    }

//    public int getId() {
//        return id;
//    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public String getRating() {
        return  rating;
    }

    public String getPrice() {
        return price;
    }

    /*public int getImage() {
        return image;
    }*/
}
