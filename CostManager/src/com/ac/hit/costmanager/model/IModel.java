
package com.ac.hit.costmanager.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * IModel interface represents the model(data)
 */
public interface IModel
{
    /**
     * Adding a expense to the expenses DataBase.
     * @param expense  which will be inserted to the DataBase.
     */
    public void addExpense(Expense expense) throws CostManagerException;
    /**
     * Deleting a expense from the expense DataBase.
     * @param ExpenseId  which will be deleted from the DataBase.
     */
    public void deleteExpense(String ExpenseId) throws CostManagerException;
    /**
     * Getting the expenses from the DataBase.
     * @return arraylist of all expenses provided by the DataBase;
     */
    public ArrayList<Expense> getExpensesFromDB() throws CostManagerException;
    /**
     * Getting report off all expenses between 2 dates from the DataBase.
     * @return arraylist of all expenses in this period provided by the DataBase;
     */
    public ArrayList<Expense> getReport(Date date1, Date date2) throws CostManagerException;
    /**
     * Adding a Category to the Category DataBase.
     * @param name category the Category which will be inserted to the DataBase.
     */
    public void addCategory(String name) throws CostManagerException;
    /**
     * Getting the Categories from the Categories DataBase.
     * @return list of Categories provided by the Categories DataBase;
     */
    public List<String> getCategories() throws CostManagerException;

    /**
     * get all the data about the user from both of DataBases.
     */
    public void getUserData() throws CostManagerException;


    }
