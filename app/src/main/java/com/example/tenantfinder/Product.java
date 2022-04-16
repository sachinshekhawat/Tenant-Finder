package com.example.tenantfinder;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    /*private int id;*/
    private String title="", shortdesc="",price="",rating="";

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    private String coverPhoto;






    public Product(){};

    public Product( String title, String shortdesc, String rating, String price,String coverPhoto){

        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating ;
        this.price = price;;
        setCoverPhoto(coverPhoto);
    }




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


    public String getCoverPhoto() {
        return coverPhoto;
    }
}
