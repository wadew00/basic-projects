import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        
        System.out.println("Welcome to my Calculator!");
        // Holders for operands
        double num1 = 0.0;
        double num2 = 0.0;
        String operation = "+";

        System.out.println(num1);
        System.out.println(num2);
        System.out.println(operation);

        System.out.println("Please enter first number: ");
        try (Scanner input = new Scanner(System.in)) {
            num1 = input.nextDouble();
            System.out.println("Please enter second number: ");
            num2 = input.nextDouble();
            System.out.println("Please enter operand: ");
            operation = input.next();
        } catch (Exception e) {
            System.out.println("Something went wrong");
            System.exit(0);
        }
        switch (operation) {
            case "+" -> System.out.println(num1+num2);
            case "-" -> System.out.println(num1-num2);
            case "*" -> System.out.println(num1*num2);
            case "/" -> System.out.println(num1/num2);
            default -> System.out.println("There is an error please check");
        }
        
    }
}
