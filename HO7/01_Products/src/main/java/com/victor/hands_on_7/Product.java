package com.victor.hands_on_7;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Calendar;

@Entity
public class Product {

    public Product() {
    }

    public Product(String name, Double unitPrice) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.setLastUpdateTime(null);
    }

    public Product(Integer id, String name, Double unitPrice, Date lastUpdateTime) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Product(Integer id, String name, Double unitPrice) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.setLastUpdateTime(null);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private Double unitPrice;

    private Date lastUpdateTime;

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastUpdateTime(Date date) {
        this.lastUpdateTime = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
