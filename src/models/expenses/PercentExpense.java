package models.expenses;

import models.User;
import models.splits.PercentSplit;
import models.splits.Split;

import java.util.List;

public class PercentExpense extends Expense{
    public PercentExpense(Double amount, String paidBy, List<Split> splits) {
        super(amount, paidBy, splits);
    }

    @Override
    public boolean validate() {
        for(Split split: this.getSplits()) {
            if(!(split instanceof PercentSplit)) {
                return false;
            }
        }

        double totalPercent = 100;
        double splitPercent = 0;

        for(Split split: this.getSplits()) {
            splitPercent += ((PercentSplit)split).getPercent();
        }

        return totalPercent == splitPercent;
    }
}
