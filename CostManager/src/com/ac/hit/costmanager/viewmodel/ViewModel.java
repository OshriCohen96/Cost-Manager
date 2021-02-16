

package com.ac.hit.costmanager.viewmodel;
import com.ac.hit.costmanager.model.*;
import com.ac.hit.costmanager.view.IView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ac.hit.costmanager.model.DerbyDBModel.ExpensesList;
/**
 * ViewModel class handles all the operations transferred from the View to the Model.
 * @implemets IViewModel
 */

public class ViewModel implements IViewModel {
    public List<String> categories = new ArrayList<>();
    private IModel model;
    private IView view;
    private ExecutorService pool;


    public ViewModel() throws CostManagerException {
        model = new DerbyDBModel();
        pool = Executors.newFixedThreadPool(10);
    }

    /**
     * setting the View.
     * @param view will be assigned to the local view parameter.
     * @override setView from IViewModel Interface.
     */
    @Override
    public void setView(IView view) {
        this.view = view;
    }

    /**
     * setting the Model.
     * @param model will be assigned to the local model parameter.
     * @override setModel from IViewModel Interface.
     */
    @Override
    public void setModel(IModel model) {
        this.model = model;
    }


    /**
     * Deleting a deleteExpense by sending it to the model.
     * @param ExpenseId will be deleted by sending it to the model.
     * @override deleteExpense from IViewModel Interface.
     */
    @Override
    public void deleteExpense(String ExpenseId) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    model.deleteExpense(ExpenseId);
                } catch (CostManagerException e) {
                    view.showMessage("Delete failed");
                }
            }
        });
    }
    /**
     * This function Returns the Categories from the model.
     * @return Category names array with all the Categories found in the model.
     * @override getCategories from IViewModel Interface.
     */

    @Override
    public List<String> getCategories() throws CostManagerException {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    categories = model.getCategories();
                } catch (CostManagerException e) {
                    e.printStackTrace();
                }

            }
        });
        return categories;
    }
    /**
     * Adding a new Category by sending it to the model.
     * @param name will be added by sending it to the model.
     * @override addCategory from IViewModel Interface.
     */
    @Override
    public void addCategory(String name) throws CostManagerException {

                    model.addCategory(name);
                    model.getUserData();

    }

    /**
     * Get from the DB all the data about expenses for a certain period of time at the request of the user
     */
    @Override
    public ArrayList<Expense> getReport(Date date1, Date date2) throws CostManagerException {
        ArrayList<Expense> reportList = new ArrayList<>();
        model = new DerbyDBModel();
        reportList = model.getReport(date1, date2);
        return reportList;

    }

    /**
     * This function Returns the expenses from the model.
     * @return expenses array with all the expenses found in the model.
     * @override getExpensesFromDB from IViewModel Interface.
     */

    @Override
    public ArrayList<Expense> getExpensesFromDB() throws CostManagerException {
        ArrayList<Expense> expensesArrayList = new ArrayList<>();
        expensesArrayList = model.getExpensesFromDB();
        return expensesArrayList;

    }



    /**
     * Get from the DB all the data about the user
     */

    @Override
    public void getUserData() throws CostManagerException {

        model.getUserData();

    }
    /**
     * Adding a expense by sending it to the model.
     * @param expense will be added by sending it to the model.
     * @override  addExpense from IViewModel Interface.
     */
    @Override
    public void addExpense(Expense expense) throws CostManagerException
    {
        model.addExpense(expense);
        ExpensesList.add(expense);
    }


}








