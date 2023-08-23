package my.project;

import dao.FormulaDAO;
import entity.Formula;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ValidationResult;
import util.ExpressionUtil;
import util.InputChecker;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 */
public class App {

    public static final FormulaDAO FORMULA_DAO = FormulaDAO.getInstance();

    public static void main(String[] args) {
        start();
    }

    private static Formula formula;
    private static final Scanner scanner = new Scanner(System.in);

    private static void start() {
        while (true) {
            System.out.print("Take your action:\n" +
                    "1. - Add formula\n" +
                    "2. - Search formula by the root\n" +
                    "3. - List all roots\n" +
                    "0. - Exit\n");

            String command = scanner.nextLine();

            try {
                int choice = Integer.parseInt(command.substring(0, 1));

                switch (choice) {
                    case 1 -> addFormula();
                    case 2 -> searchByRoot();
                    case 3 -> listRoots();
                    case 0 -> {
                        exit();
                        return;
                    }
                    default -> System.out.println("Wrong action.");
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                System.out.println("Invalid input. Please enter a valid action number.");
            }

            System.out.print("Any other action? (y/n) [y] ");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("n")) {
                break;
            }
        }
    }

    private static void listRoots() {
        String listRoots = FORMULA_DAO.getFormulas(false).stream()
                .map(f -> {
                    String roots = f.getRoot().toString();
                    return "Formula: " + f.getFormula() +
                            " - Roots: " + roots;
                })
                .collect(Collectors.joining("\n"));
        System.out.println(listRoots.isEmpty() ?
                "There are no roots. " : listRoots);
    }

    private static void exit() {
        System.out.println("Goodbye");

    }

    private static void addFormula() {
        formula = new Formula();
        System.out.print("Enter the formula: ");
        String input = scanner.nextLine();


        Expression expression = null;
        String checkedInput = "";

        try {
            checkedInput = InputChecker.check(input);
            expression = ExpressionUtil.build(checkedInput);
        } catch (EmptyStackException e) {
            System.out.println("Check your parenthesis.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }


        if (expression == null || checkedInput.isEmpty()) {
            return;
        }

        if (checkIfFormulaAlreadyExists(checkedInput)) {
            System.out.println("Such formula already exists.");
            return;
        }

        ValidationResult validationResult = ExpressionUtil.validate(expression);
        if (validationResult.isValid()) {
            System.out.println("Formula " + input + " is valid.");
            prepareToSave(checkedInput);

            System.out.print("Add roots? (y/n) [n] ");
            String add = scanner.nextLine().substring(0, 1);
            if (add.equalsIgnoreCase("y")) {
                addRootsFor(expression);
            }
            flushToDB();

        } else {
            validationResult.getErrors().forEach(System.out::println);
            System.out.println("Expression is invalid. ");
        }
    }

    private static boolean checkIfFormulaAlreadyExists(String expression) {
        return FORMULA_DAO.getFormulaByExpression(expression) != null;
    }

    private static void flushToDB() {
        FORMULA_DAO.saveFormula(formula);
    }

    private static void addRootsFor(Expression expression) {
        System.out.print("Enter the root: ");
        double root = Double.parseDouble(scanner.nextLine());
        boolean isRoot = ExpressionUtil.validate(expression, root).isValid();
        if (isRoot) {
            System.out.println("Root is correct. ");
            formula.getRoot().add(root);
        }
    }

    private static void prepareToSave(String expression) {
        formula.setFormula(expression);
    }

    private static void searchByRoot() {
        System.out.print("Enter the root to search: ");

        double root = Double.parseDouble(scanner.nextLine());
        List<Formula> formulasByRoot = FORMULA_DAO.getFormulasByRoot(root);

        if (formulasByRoot.isEmpty()) {
            System.out.println("No formulas were found. ");
            return;
        }
        System.out.println(
                formulasByRoot.stream()
                        .map(f -> {
                            String roots = f.getRoot().toString();
                            return "Formula: " + f.getFormula() +
                                    " - Roots: " + roots;
                        })
                        .collect(Collectors.joining("\n"))
        );

    }
}
