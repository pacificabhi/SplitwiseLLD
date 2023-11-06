package services;

import Types.ExpenseType;
import models.expenses.EqualExpense;
import models.expenses.ExactExpense;
import models.expenses.Expense;
import models.expenses.PercentExpense;
import models.splits.PercentSplit;
import models.splits.Split;
import repositories.BalanceSheet;
import repositories.ExpenseRepository;

import java.util.List;

public class ExpenseService {
    ExpenseRepository expenseRepository;
    BalanceSheet balanceSheet;

    public ExpenseService(ExpenseRepository expenseRepository, BalanceSheet balanceSheet) {
        this.expenseRepository = expenseRepository;
        this.balanceSheet = balanceSheet;
    }

    public static Expense createExpense(ExpenseType expenseType, String paidBy, double amount, List<Split> splits) {
        switch (expenseType) {
            case EXACT -> {
                return new ExactExpense(amount, paidBy, splits);
            }
            case EQUAL -> {
                int totalSplits = splits.size();
                double splitAmount = ((double) Math.round(amount * 100 / totalSplits)) / 100.0;
                for (Split split : splits) {
                    split.setAmount(splitAmount);
                }
                splits.get(0).setAmount(splitAmount + (amount - splitAmount * totalSplits));
                return new EqualExpense(amount, paidBy, splits);
            }
            case PERCENT -> {
                for (Split split : splits) {
                    PercentSplit percentSplit = (PercentSplit) split;
                    split.setAmount((amount * percentSplit.getPercent()) / 100.0);
                }
                return new PercentExpense(amount, paidBy, splits);
            }
            default -> {
                return null;
            }
        }
    }

    public void saveExpense(Expense expense) throws Exception {
        boolean validatedExpense = expense.validate();
        if(!validatedExpense) {
            if(expense instanceof ExactExpense) {
                throw new Exception("Amount of all users must equal to total amount");
            }

            if(expense instanceof PercentExpense) {
                throw new Exception("Percent of all users must equal to 100");
            }
        }
        String expenseId = expenseRepository.addExpense(expense);
        for(Split split: expense.getSplits()) {
            balanceSheet.addTransaction(expense.getPaidBy(), split.getUser().getId(), split.getAmount());
        }
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.getExpenses();
    }
}
