package com.example.afgcharity;

public class Apparel {
    private String clothing;
    private String user;
    private int amount;
    private String id;
    public Apparel(String user, String clothing, int amount, String id){
        this.clothing=clothing;
        this.user=user;
        this.amount=amount;
        this.id=id;
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
    public String getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Apparel{" +
                "clothing='" + clothing + '\'' +
                ", user='" + user + '\'' +
                ", amount=" + amount +
                ", id='" + id + '\'' +
                '}';
    }
}
