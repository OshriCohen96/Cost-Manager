

package com.ac.hit.costmanager.model;
import java.util.ArrayList;

/**
 * Category class is the category of the expense
 */
public class Category
{

    private String name;
    private ArrayList<Expense> expensesList;
    private float categorySum;


    /**
     * Class constructors that handle all the types of categories.
     */
    public Category(String name)
    {
        setName(name);
        this.expensesList = expensesList;
        setCategorySum(0);
    }
    public Category(String name, float category_expenses) {
        setName(name);
        this.expensesList = new ArrayList<>();
        setCategorySum(category_expenses);
    }

    public Category(String name, ArrayList<Expense> expensesList, float categorySum) {
        setName(name);
        setCategorySum(categorySum);
        this.expensesList = expensesList;
    }
    public Category() { }

//getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name!=null)
        { this.name = name; }
    }
    public ArrayList<Expense> getExpensesList() {
        return expensesList;
    }

    public void setExpensesList(ArrayList<Expense> expensesList) {
        this.expensesList = expensesList;
    }
    public void setCategorySum(float categorySum)
    {
        this.categorySum = categorySum;
    }
    public float getCategorySum() { return categorySum; }

    /**
     * addNewExpense summarize all expenses to total Expense for each category
     */
    public void addNewExpense(Expense expense)
    {
        //  Determine the default currency in the user sum expenses to ILS
        if(expense.getCurrency()==Currency.USD){
            categorySum+= (expense.getPrice()*3.2);
        }
        if(expense.getCurrency()==Currency.EURO){
            categorySum+= (expense.getPrice()*3.5);
        }
        if(expense.getCurrency()==Currency.ILS){
            categorySum+= expense.getPrice();
        }
    }

    @Override
    public String toString()
    {
        return "category{" +
                "name='" + name + '\'' +
                ", expensesList=" + expensesList +
                ", categorySum=" + categorySum +
                '}';
    }
}
