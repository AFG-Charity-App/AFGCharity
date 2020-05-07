package com.example.afgcharity;

public class Apparel {
    private String clothing;
    private String user;
    private int amount;
    private String id;
    private String name;
    public Apparel(String user, String clothing, int amount, String id, String name){
        this.clothing=clothing;
        this.user=user;
        this.amount=amount;
        this.id=id;
        this.name=name;
    }

    public String getClothing() {
        return clothing;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
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
                ", name='"+name +'\''+
                ", user='" + user + '\'' +
                ", amount=" + amount +
                ", id='" + id + '\'' +
                '}';
    }
}
