package com.example.afgcharity;

/**
 * apparel for charity needs list
 */
public class Apparel {
    private String clothing;
    private String user;
    private int amount;
    private String id;
    private String name;

    /**
     * details on apparel that charity adds to list
     * @param user
     * @param clothing
     * @param amount
     * @param id
     * @param name
     */
    public Apparel(String user, String clothing, int amount, String id, String name){
        this.clothing=clothing;
        this.user=user;
        this.amount=amount;
        this.id=id;
        this.name=name;
    }

    /**
     * gets clothing type
     * @return clothing type
     */
    public String getClothing() {
        return clothing;
    }

    /**
     * gets name of user
     * @return name of user
     */
    public String getUser() {
        return user;
    }

    /**
     * gets name of charity
     * @return name of charity
     */
    public String getName() {
        return name;
    }

    /**
     * gets amount of item needed
     * @return amount of item needed
     */
    public int getAmount() {
        return amount;
    }

    /**
     * gets firebase id
     * @return firebase id
     */
    public String getId(){
        return id;
    }

    /**
     * makes list of information for each item in needs list
     * @return information for list as a string
     */
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
