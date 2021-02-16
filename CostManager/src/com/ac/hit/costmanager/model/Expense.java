

package com.ac.hit.costmanager.model;
import java.util.Date;
import java.util.UUID;
/**
 * Expense class is the item that the user will insert his items
 * and it  stored it in the expenses table
 */
public class Expense
{
    private String name;
    private double price;
    private Date date;
    private Currency currency;
    private String categoryName;
    private String ExpenseId;

    /**
     * Class constructor with auto generate id,and copy constructor
     */
    public Expense(String name, double price,Currency currency,String categoryName) throws CostManagerException
    {
        setName(name);
        setPrice(price);
        setCategoryName(categoryName);
        setCurrency(currency);
        Date date1=new Date();
        date1.getTime();
        setDate(date1);
        //auto generate id
        String expenseId = UUID.randomUUID().toString();
        setExpenseId(expenseId);
    }
    public Expense(String expenseId,String name, double price,Currency currency,Date date,String categoryName) throws CostManagerException
    {
        setName(name);
        setPrice(price);
        setDate(date);
        setCurrency(currency);
        setExpenseId(expenseId);
        setCategoryName(categoryName);

    }


//Getters and setters
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) throws CostManagerException
    {        if(name != null && !name.trim().isEmpty())
                    this.categoryName = name;
                    else throw new CostManagerException("Category name can't be empty");
    }

    public String getExpenseId() {
        return ExpenseId;
    }

    public void setExpenseId(String expenseId) {
        ExpenseId = expenseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws CostManagerException {
        if(name != null && !name.trim().isEmpty())
            this.name = name;
        else throw new CostManagerException("Name can't be empty");

    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws CostManagerException {
        if(price>0) {
            this.price = price;
        }
        else throw new CostManagerException("Price must be positive");

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }




    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", currency=" + currency +
                ", categoryName='" + categoryName + '\'' +
                ", ExpenseId='" + ExpenseId + '\'' +
                '}';
    }
}
