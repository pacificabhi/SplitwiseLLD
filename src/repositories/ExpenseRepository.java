package repositories;

import models.expenses.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    List<Expense> expenses;

    public ExpenseRepository() {
        this.expenses = new ArrayList<>();
    }

    public String addExpense(Expense expense) {
        expenses.add(expense);
        return expense.getId();
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
