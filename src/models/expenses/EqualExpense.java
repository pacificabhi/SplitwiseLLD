package models.expenses;

import models.User;
import models.splits.EqualSplit;
import models.splits.Split;

import java.util.List;

public class EqualExpense extends Expense{
    public EqualExpense(Double amount, String paidBy, List<Split> splits) {
        super(amount, paidBy, splits);
    }

    @Override
    public boolean validate() {
        for(Split split: this.getSplits()) {
            if(!(split instanceof EqualSplit)) {
                return false;
            }
        }
        return true;
    }
}
