/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author MITUSER-2
 */
public class Product {
    
    private int pid;
    private int qty;
    private int amount;
    private String category;
    private String brand;
    private int price;
    private String name;
//    private String name;
    private String size;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getqty() {
        return qty;
    }

    public void setqty(String qty) {
        this.name = qty;
    }
    
        public int getamount() {
        return amount;
    }

    public void setamount(String amount) {
        this.name = amount;
    }

    public Product(int pid, String category, int qty, int amount, String brand, int price, String name, String size) {
        this.pid = pid;
        this.qty = qty;
        this.amount = amount;
        this.brand = brand;
        this.price = price;
        this.name = name;
        this.size = size;
        this.category = category;
    }
    
    

    public Product(int pid, String category, String brand, String size, int price, int stock, String image_name) {
        this.pid = pid;
        this.category = category;
        this.brand = brand;
//        this.name = name;
        this.size = size;
        this.price = price;
        this.stock = stock;
//        this.image_name = image_name;

        File file = new File("D:\\Java Net Bin\\POS\\src\\images\\"+image_name);
        Image img = new Image(file.toURI().toString());
        
        imgView = new ImageView(img);
        imgView.setFitWidth(60);
        imgView.setFitHeight(60);
        imgView.setPreserveRatio(true);
    }
    
    private ImageView imgView;

    public ImageView getImgView() {
        return imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

//    public void setImage_name(String image_name) {
//        this.image_name = image_name;
//    }

    public int getPid() {
        return pid;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

//    public String getName() {
//        return name;
//    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

//    public String getImage_name() {
//        return image_name;
//    }
    private int stock;
    private String image_name;
    
    
}
