package services;

import Types.ExpenseType;
import models.User;
import models.expenses.Expense;
import models.splits.EqualSplit;
import models.splits.ExactSplit;
import models.splits.PercentSplit;
import models.splits.Split;
import repositories.BalanceSheet;
import repositories.ExpenseRepository;
import repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SplitWise {
    UserRepository userRepository;
    ExpenseRepository expenseRepository;
    UserService userService;
    ExpenseService expenseService;
    BalanceSheet balanceSheet;
    public SplitWise() {
        userRepository = new UserRepository();
        expenseRepository = new ExpenseRepository();

        userService = new UserService(userRepository);
        balanceSheet = new BalanceSheet(userService);
        expenseService = new ExpenseService(expenseRepository, balanceSheet);
    }

    private void printMenu() {
        System.out.println("Available Commands");
        System.out.println("1. SHOW");
        System.out.println("2. SHOW <userId>");
        System.out.println("3. EXPENSE");
        System.out.println("4. ADD");
        System.out.println("5. ALL");
    }

    private void listenForCommands() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            printMenu();
            String command = scanner.nextLine();
            String[] commands = command.split(" ");
            String commandType = commands[0];

            switch (commandType) {
                case "SHOW":
                    if(commands.length == 1) {
                        showBalances();
                    } else {
                        showBalance(commands[1]);
                    }
                    break;
                case "EXPENSE":
                    handleExpenseCommand(commands);
                    break;

                case "ADD":
                    // ADD name email phone
                    User newUser = UserService.createUser(commands[1], commands[2], commands[3]);
                    userService.saveUser(newUser);
                    System.out.println("User added: " + newUser.getId());
                    break;

                case "ALL":
                    List<User> users = userService.getAllUsers();
                    showAllUsers(users);
            }
        }
    }

    private void showAllUsers(List<User> users) {
        if(users == null || users.size() == 0) {
            System.out.println("No Users");
        }
        for(User user: users) {
            System.out.println(user.getId() + ", " + user.getName());
        }
    }

    private void handleExpenseCommand(String[] commands) throws Exception {
        String paidBy = commands[1];
        double amount = Double.parseDouble(commands[2]);
        int noOfUsers = Integer.parseInt(commands[3]);
        String expenseType = commands[4 + noOfUsers];

        List<Split> splits = new ArrayList<>();
        switch (expenseType) {
            case "EQUAL":
                for (int i = 0; i < noOfUsers; i++) {
                    splits.add(new EqualSplit(userService.getUser(commands[4 + i])));
                }
                Expense expenseEqual = ExpenseService.createExpense(
                        ExpenseType.EQUAL,
                        paidBy,
                        amount,
                        splits
                );
                expenseService.saveExpense(expenseEqual);
                break;
            case "EXACT":
                for (int i = 0; i < noOfUsers; i++) {
                    splits.add(new ExactSplit(userService.getUser(commands[4 + i]), Double.parseDouble(commands[5 + noOfUsers + i])));
                }
                Expense expenseExact = ExpenseService.createExpense(ExpenseType.EXACT, paidBy, amount, splits);
                expenseService.saveExpense(expenseExact);
                break;
            case "PERCENT":
                for (int i = 0; i < noOfUsers; i++) {
                    splits.add(new PercentSplit(userService.getUser(commands[4 + i]), Double.parseDouble(commands[5 + noOfUsers + i])));
                }
                Expense expensePercent = ExpenseService.createExpense(ExpenseType.PERCENT, paidBy, amount, splits);
                expenseService.saveExpense(expensePercent);
                break;
        }
    }

    public void startApp() {
        userService.saveUser(UserService.createUser("Abhishek", "abhishek@gmail.com", "1234567890"));
        userService.saveUser(UserService.createUser("Vivek", "vivek@gmail.com", "1234567890"));
        userService.saveUser(UserService.createUser("Nisha", "nisha@gmail.com", "1234567890"));
        listen();
    }

    private void listen() {
        try {
            listenForCommands();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            listen();
        }
    }

    public void showBalance(String userId) throws Exception {
        List<String> balances = balanceSheet.getBalance(userId);
        if(balances.size() == 0) {
            System.out.println("Nothing to show for " + userId);
        }
        for(String balance: balances) {
            System.out.println(balance);
        }

    }

    public void showBalances() throws Exception {
        List<String> balances = balanceSheet.getBalances();
        if(balances.size() == 0) {
            System.out.println("Nothing to show");
        }
        for(String balance: balances) {
            System.out.println(balance);
        }
    }
}
