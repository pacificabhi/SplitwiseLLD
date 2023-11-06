package repositories;

import models.User;
import services.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceSheet {
    // user to <connectedUser, amount>
    Map<String, Transactions> balanceSheet;
    UserService userService;

    public BalanceSheet(UserService userService) {
        this.balanceSheet = new HashMap<>();
        this.userService = userService;
    }

    public void addTransaction(String paidBy, String paidTo, Double amount) {
        if(!balanceSheet.containsKey(paidBy)) {
            balanceSheet.put(paidBy, new Transactions());
        }
        if(!balanceSheet.containsKey(paidTo)) {
            balanceSheet.put(paidTo, new Transactions());
        }
        balanceSheet.get(paidBy).addTransaction(paidTo, amount);
        balanceSheet.get(paidTo).addTransaction(paidBy, -amount);
    }

    public List<String> getBalances() throws Exception {
        List<String> balances = new ArrayList<>();
        for (Map.Entry<String, Transactions> allBalances : balanceSheet.entrySet()) {
            for (Map.Entry<String, Double> userBalance : allBalances.getValue().getTransations().entrySet()) {
                if (userBalance.getValue() < 0) {
                    balances.add(
                            getBalanceText(
                                userService.getUser(allBalances.getKey()).getName(),
                                userService.getUser(userBalance.getKey()).getName(),
                                userBalance.getValue()
                            )
                    );
                }
            }
        }
        return balances;
    }

    private String getBalanceText(String nameFirst, String nameSecond, double amount) {
        return nameFirst + " owes " + nameSecond + ": " + Math.abs(amount);
    }

    public List<String> getBalance(String userId) throws Exception {
        User mainUser = userService.getUser(userId);
        if(!balanceSheet.containsKey(userId)) {
            balanceSheet.put(userId, new Transactions());
        }
        List<String> balances = new ArrayList<>();
        for(Map.Entry<String, Double> userBalance: balanceSheet.get(userId).getTransations().entrySet()) {
            if (userBalance.getValue() < 0) {
                balances.add(
                    getBalanceText(
                        mainUser.getName(),
                        userService.getUser(userBalance.getKey()).getName(),
                        userBalance.getValue()
                    )
                );
            } else if(userBalance.getValue() > 0) {
                balances.add(
                    getBalanceText(
                        userService.getUser(userBalance.getKey()).getName(),
                        mainUser.getName(),
                        userBalance.getValue()
                    )
                );
            }
        }

        return balances;
    }

    private class Transactions {
        Map<String, Double> transations;

        public Transactions() {
            this.transations = new HashMap<>();
        }

        public void addTransaction(String userId, double deltaAmount) {
            if(!this.transations.containsKey(userId)) {
                this.transations.put(userId, 0.0);
            }
            double oldBalance = this.transations.get(userId);
            this.transations.put(userId, oldBalance + deltaAmount);
        }


        public Map<String, Double> getTransations() {
            return transations;
        }
    }
}
