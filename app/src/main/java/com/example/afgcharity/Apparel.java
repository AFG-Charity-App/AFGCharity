package com.example.afgcharity;

public class Apparel {
    private String clothing;
    private String user;
    private int amount;
    public Apparel(String user, String clothing, int amount){
        this.clothing=clothing;
        this.user=user;
        this.amount=amount;
    }

    public String getClothing() {
        return clothing;
    }

    public String getUser() {
        return user;
    }

    public int getAmount() {
        return amount;
    }
}
