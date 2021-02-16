

package com.ac.hit.costmanager.viewmodel;
import com.ac.hit.costmanager.model.CostManagerException;
import com.ac.hit.costmanager.model.Expense;
import com.ac.hit.costmanager.model.IModel;
import com.ac.hit.costmanager.view.IView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface IViewModel
{
    /**
     * setting the View.
     * @param view will be assigned to the local view parameter.
     */
    public void setView(IView view);
    /**
     * setting the Model.
     * @param model will be assigned to the local model parameter.
     */
    public void setModel(IModel model);

    /**
     * Adding a Expense by sending it to the model.
     * @param expense will be added by sending it to the model.
     */
    public void addExpense(Expense expense) throws CostManagerException;


    /**
     * Deleting a Expense by sending it to the model.
     * @param ExpenseId will be deleted by sending it to the model.
     */
    public void deleteExpense(String ExpenseId) throws CostManagerException;


    /**
     * This function Returns the expenses from the model.
     * @return expenses arrayList with all the expenses found in the model.
     */
    public ArrayList<Expense> getExpensesFromDB() throws CostManagerException;

    /**
     * Adding a new Category by sending it to the model.
     * @param name category will be added by sending it to the model.
     */
    public void addCategory(String name) throws CostManagerException;


    /**
     * This function Returns the Categories from the model.
     * @return Category list with all the Categories found in the model.
     */
    public List<String> getCategories() throws CostManagerException;
    /**
     * This function update the data from the model.
\     */
    public void getUserData() throws CostManagerException;
    /**
     * This function Returns expenses from the model.
     * @return Expenses list with al the expenses between 2 dates that found in the model.
     */
    public ArrayList<Expense> getReport(Date date1, Date date2) throws CostManagerException;

}
