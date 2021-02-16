
package com.ac.hit.costmanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
void setUp()throws CostManagerException
{
    Expense expense = new Expense("test", 0, Currency.ILS,"test");
}
    @Test
    void setName() {
        // Test names of expense that they are not empty/NULL and throw Exception

        assertThrows(CostManagerException.class, () -> {
            Expense expense = new Expense("test", 20, Currency.ILS,"test");
            expense.setName("");
        });
    }

    @Test
    void getPrice() {
    // Test expenses that they are not positive and throw Exception
        assertThrows(CostManagerException.class, () -> {
            Expense expense = new Expense("test", 20, Currency.ILS,"test");
            expense.setPrice(-1);
        });
    }
}