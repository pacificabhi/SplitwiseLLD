package models.expenses;

import models.User;
import models.splits.ExactSplit;
import models.splits.Split;

import java.util.List;

public class ExactExpense extends Expense{
    public ExactExpense(Double amount, String paidBy, List<Split> splits) {
        super(amount, paidBy, splits);
    }

    @Override
    public boolean validate() {
        for(Split split: this.getSplits()) {
            if(!(split instanceof ExactSplit)) {
                return false;
            }
        }

        double splitTotal = 0;
        for(Split split: this.getSplits()) {
            ExactSplit exactSplit = (ExactSplit) split;
            splitTotal += exactSplit.getAmount();
        }

        return splitTotal == this.getAmount();
    }
}
