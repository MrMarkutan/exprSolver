package util;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class InputChecker {

    public static String check(String input) {
        if (!input.contains("x")) {
            throw new IllegalArgumentException("There is no x in the formula.");
        }
        if (input.contains("=")) {
            countNumbers(input);

            StringBuilder builder = new StringBuilder();
            String[] split = input.split("=");
            builder.append(split[0])
                    .append("+(-1)*(")
                    .append(split[1])
                    .append(")");

            return builder.toString();
        }
        return input;
    }

    private static void countNumbers(String input) {
        Pattern nonDigitPattern = Pattern.compile("[^0-9]+");

        long count = Stream.of(input.split(String.valueOf(nonDigitPattern)))
                .filter(s -> !s.isEmpty())
                .count();

        System.out.println("Numbers in formula: " + count);
    }
}
