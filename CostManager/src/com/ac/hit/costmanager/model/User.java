

package com.ac.hit.costmanager.model;

import java.util.ArrayList;
/**
 * The general Class that contains all the data
 */
public class User
{
    public ArrayList<Category> categoryArrayList;
    public float sumExpenses;

    public User(ArrayList<Category> categoryArrayList, float sumExpenses)throws  CostManagerException
    {
        setCategoryArrayList(categoryArrayList);
        setSumExpenses(sumExpenses);
        totalExpenses();


    }

    public void totalExpenses()
    {
        sumExpenses = 0;
        for (int i = 0; i < categoryArrayList.size(); i++)
            sumExpenses += categoryArrayList.get(i).getCategorySum();

    }
    public float getSumExpenses() {
        return sumExpenses;
    }

    public void setSumExpenses(float sumExpenses) throws CostManagerException {
        if (sumExpenses>=0)
            this.sumExpenses = sumExpenses;
        else throw new CostManagerException("Sum expenses must be positive");
    }


    public ArrayList<Category> getCategoryArrayList()
    {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }


    public void addNewCategory(String name)
    {
        Category category =new Category(name);
        this.categoryArrayList.add(category);
    }



}
