package numbers;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Amazing Numbers!");
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows how many consecutive numbers are to be processed;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");
        long inputNumber = 0;
        int iterator = 0;
        while (true) {
            ArrayList<String> commands = new ArrayList<>();
            System.out.println("Enter a request: > ");
            String[] input = scanner.nextLine().split(" ");
            if (input.length >= 3) {
                inputNumber = Long.parseLong(input[0]);
                iterator = Integer.parseInt(input[1]);
                for (int i = 2; i < input.length; i++) {
                    commands.add(input[i].toUpperCase());
                }
            } else if (input.length == 2) {
                inputNumber = Long.parseLong(input[0]);
                iterator = Integer.parseInt(input[1]);
            } else {
                try {
                    inputNumber = Long.parseLong(input[0]);
                } catch (NumberFormatException e) {
                    System.out.println("The first parameter should be a natural number or zero.");
                    continue;
                }
            }

            if (inputNumber < 0) {
                System.out.println("The first parameter should be a natural number or zero.");
            } else if (inputNumber == 0) {
                System.out.println("Goodbye!");
                return;
            } else if (input.length == 1) {
                printSingleNumProperty(inputNumber);
            } else if (input.length > 1) {
                processIteratorProperties(inputNumber, iterator, commands);
            } else {
                System.out.println("The first parameter should be a natural number or zero.");
            }

        }

    }

    public static void printSingleNumProperty(long inputNumber) {
        System.out.println("Properties of " + inputNumber);
        System.out.printf("%10s: %b\n", "even", isEven(inputNumber));
        System.out.printf("%10s: %b\n", "odd", !isEven(inputNumber));
        System.out.printf("%10s: %b\n", "buzz", isBuzz(inputNumber));
        System.out.printf("%10s: %b\n", "duck", isDuck(inputNumber));
        System.out.printf("%10s: %b\n", "palindromic", isPalindrome(inputNumber));
        System.out.printf("%10s: %b\n", "gapful", isGapFul(inputNumber));
        System.out.printf("%10s: %b\n", "spy", isSpy(inputNumber));
        System.out.printf("%10s: %b\n", "square", isPerfectSquare(inputNumber));
        System.out.printf("%10s: %b\n", "sunny", isPerfectSquare(inputNumber+1));
        System.out.printf("%10s: %b\n", "jumping", isJumping(inputNumber));
        System.out.printf("%10s: %b\n", "happy", isHappy(inputNumber));
        System.out.printf("%10s: %b\n", "sad", !isHappy(inputNumber));
    }

    public static void processIteratorProperties(long inputNumber, int iterator, ArrayList<String> commands) {

        String[] availableCommands = new String[] {"EVEN", "ODD", "BUZZ", "DUCK", "PALINDROMIC", "GAPFUL", "SPY", "SUNNY", "SQUARE", "JUMPING", "HAPPY", "SAD"};
        if (iterator <= 0) {
            System.out.println("The second parameter should be a natural number.");
            return;
        }
        if (commands.size() == 0) {
            printIteratorProperties(inputNumber, iterator, availableCommands.length);
            return;
        }

        ArrayList<String> notValidCommands = new ArrayList<>();
        ArrayList<String> inputCommands = new ArrayList<>();
        for (String c: commands) {
            String cleansedCommand = c;
            if (c.contains("-")) {
                cleansedCommand = c.substring(1);
            }


            if (c != null && !Arrays.asList(availableCommands).contains(cleansedCommand.toUpperCase())) {
                notValidCommands.add(c.toUpperCase());
            }

            if (c != null) {
                inputCommands.add(c.toUpperCase());
            }
        }

        if (notValidCommands.size() > 0) {
            if (notValidCommands.size() == 1) {
                System.out.printf("The property %s is wrong.\n", Arrays.toString(notValidCommands.toArray()));
            } else {
                System.out.printf("The properties %s are wrong.\n", Arrays.toString(notValidCommands.toArray()));
            }
            System.out.printf("Available properties: %s\n", Arrays.toString(availableCommands));
            return;
        }

        if (inputCommands.size() > 1) {
            int flag = checkMutualExclusive(availableCommands, inputCommands);
            if (flag == 1) {
                return;
            }

//            boolean isMutual = false;
//            if (isMutualExclusive(inputCommands, "EVEN", "ODD")) {
//                isMutual = true;
//            } else if (isMutualExclusive(inputCommands, "SUNNY", "SQUARE")) {
//                isMutual = true;
//            } else if (isMutualExclusive(inputCommands, "DUCK", "SPY")) {
//                isMutual = true;
//            } else if (isMutualExclusive(inputCommands, "HAPPY", "SAD")) {
//                isMutual = true;
//            }
//
//            if (isMutual) {
//                System.out.printf("The request contains mutually exclusive properties: %s\n", Arrays.toString(inputCommands.toArray()));
//                System.out.println("There are no numbers with these properties.");
//                return;
//            }
        }

        printConsectiveCommand(inputNumber, iterator, commands, availableCommands.length);
    }

    public static int checkMutualExclusive(String[] availableCommands, ArrayList<String> inputCommands) {
        HashMap<String, String> mutualExclusive = new HashMap<>();

        mutualExclusive.put("EVEN", "ODD");
        mutualExclusive.put("DUCK", "SPY");
        mutualExclusive.put("SUNNY", "SQUARE");
        mutualExclusive.put("HAPPY", "SAD");
        mutualExclusive.put("-EVEN", "-ODD");
        mutualExclusive.put("-DUCK", "-SPY");
        mutualExclusive.put("-SUNNY", "-SQUARE");
        mutualExclusive.put("-HAPPY", "-SAD");

        boolean excludePropertyFlag = inputCommands.stream().anyMatch(x -> x.contains("-"));
        String[] mutualExclusiveKeys = mutualExclusive.keySet().toArray(new String[mutualExclusive.size()]);

        if (excludePropertyFlag) {
            List<String> commandsToInclude = Arrays.asList(availableCommands);
            for (int i = 0; i < mutualExclusiveKeys.length; i++) {
                boolean flag = isMutualExclusive(inputCommands, mutualExclusiveKeys[i], mutualExclusive.get(mutualExclusiveKeys[i]));
                if (flag) {
                    System.out.printf("The request contains mutually exclusive properties: [%s, %s]\n", mutualExclusiveKeys[i], mutualExclusive.get(mutualExclusiveKeys[i]));
                    System.out.println("There are no numbers with these properties.");
                    return 1;
                }
            }

            for (int i = 0; i < availableCommands.length; i++) {
                boolean flag = isMutualExclusive(inputCommands, availableCommands[i], "-" + availableCommands[i]);
                if (flag) {
                    System.out.printf("The request contains mutually exclusive properties: [%s, -%s]\n", availableCommands[i], availableCommands[i]);
                    System.out.println("There are no numbers with these properties.");
                    return 1;
                }
            }


        } else {
//            String[] mutualExclusiveKeys = mutualExclusive.keySet().toArray(new String[mutualExclusive.size()]);
            for (int i = 0; i < mutualExclusiveKeys.length; i++) {
                boolean flag = isMutualExclusive(inputCommands, mutualExclusiveKeys[i], mutualExclusive.get(mutualExclusiveKeys[i]));
                    if (flag) {
                        System.out.printf("The request contains mutually exclusive properties: [%s, %s]\n", mutualExclusiveKeys[i], mutualExclusive.get(mutualExclusiveKeys[i]));
                        System.out.println("There are no numbers with these properties.");
                        return 1;

                }
            }
        }


        return 0;
    }


    public static boolean isMutualExclusive(ArrayList<String> inputCommands, String property1, String property2) {
//        boolean isFirstPropertyAvailable = inputCommands.stream().anyMatch(x -> x.contains(property1.toUpperCase()));
//        boolean isSecondPropertyAvailable = inputCommands.stream().anyMatch(x -> x.contains(property2.toUpperCase()));
        return inputCommands.contains(property1.toUpperCase()) && inputCommands.contains(property2.toUpperCase());
    }


    public static void printConsectiveCommand(long inputNumber, int iterator, ArrayList<String> commands, int propertiesLength) {
        int n = 0;
        long num = inputNumber;
        HashMap<String, Function<Long, Boolean>> callableHashMap = new HashMap<>();

        callableHashMap.put("EVEN", (Long i) -> isEven(i));
        callableHashMap.put("ODD", (Long i) -> !isEven(i));
        callableHashMap.put("BUZZ", (Long i) -> isBuzz(i));
        callableHashMap.put("DUCK", (Long i) -> isDuck(i));
        callableHashMap.put("PALINDROMIC", (Long i) -> isPalindrome(i));
        callableHashMap.put("GAPFUL", (Long i) -> isGapFul(i));
        callableHashMap.put("SPY", (Long i) -> isSpy(i));
        callableHashMap.put("SQUARE", (Long i) -> isPerfectSquare(i));
        callableHashMap.put("SUNNY", (Long i) -> isPerfectSquare(i+1));
        callableHashMap.put("JUMPING", (Long i) -> isJumping(i));
        callableHashMap.put("HAPPY", (Long i) -> isHappy(i));
        callableHashMap.put("SAD", (Long i) -> !isHappy(i));

        while (n < iterator) {
            ArrayList<Boolean> arevalidNumbers = new ArrayList<>();
            for (String s: commands) {
                String cleansedCommand = s;
                if (s.contains("-")) {
                    cleansedCommand = s.substring(1);
                    arevalidNumbers.add(!callableHashMap.get(cleansedCommand.toUpperCase()).apply(num));
                } else {
                    arevalidNumbers.add(callableHashMap.get(cleansedCommand.toUpperCase()).apply(num));
                }

            }
            if (arevalidNumbers.stream().allMatch(x -> x)) {
                printIteratorProperties(num, 1, propertiesLength);
                n++;
            }
            num++;
        }

    }

    public static void printIteratorProperties(long inputNumber, int iterator, int propertiesLength) {

        for (long i = inputNumber; i < (inputNumber + iterator); i++) {
            String[] properties = new String[propertiesLength];

            properties[0] = (isEven(i) ? "even" : "");
            properties[1] = (!isEven(i) ? "odd" : "");
            properties[2] = (isBuzz(i) ? "buzz" : "");
            properties[3] = (isDuck(i) ? "duck" : "");
            properties[4] = (isPalindrome(i) ? "palindromic" : "");
            properties[5] = (isGapFul(i) ? "gapful" : "");
            properties[6] = (isSpy(i) ? "spy" : "");
            properties[7] = (isPerfectSquare(i) ? "square" : "");
            properties[8] = (isPerfectSquare(i+1) ? "sunny" : "");
            properties[9] = (isJumping(i) ? "jumping" : "");
            properties[10] = (isHappy(i) ? "happy" : "");
            properties[11] = (!isHappy(i) ? "sad" : "");

            String numberProperties = String.join(", ", (Arrays.stream(properties).filter(x -> x != "")).collect(Collectors.toList()));
            System.out.printf("%15d is %s\n", i, numberProperties);
        }

    }

    public static boolean isEven(long inputNumber) {
        return inputNumber % 2 == 0;
    }

    public static boolean isBuzz(long inputNumber) {
        boolean isDivisible = (inputNumber % 7) == 0;
        boolean isEndsWith = (inputNumber % 10) == 7;
        return isDivisible || isEndsWith;
    }

    public static boolean isDuck(long inputNumber) {
        while (inputNumber > 0) {
            if (inputNumber % 10 == 0) {
                return true;
            }
            inputNumber = inputNumber / 10;
        }
        return false;
    }

    public static boolean isPalindrome(long inputNumber) {
        char[] tempArr = String.valueOf(inputNumber).toCharArray();
        char[] finalArr = new char[tempArr.length];
        for (int i = 0; i < finalArr.length; i++) {
            finalArr[i] = tempArr[(tempArr.length - 1) - i];
        }
//        long reverseInt = Long.parseLong(String.valueOf(finalArr));
        return Objects.equals(String.valueOf(finalArr), String.valueOf(tempArr));
    }

    public static boolean isGapFul(long inputNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] tempArr = String.valueOf(inputNumber).toCharArray();

        if (tempArr.length < 3) {
            return false;
        }

        stringBuilder.append(tempArr[0]);
        stringBuilder.append(tempArr[tempArr.length - 1]);

        int divisor = Integer.parseInt(stringBuilder.toString());
        return inputNumber % divisor == 0;
    }

    public static boolean isSpy(long inputNumber) {
        long sum = 0;
        long product = 1;

        char[] tempArr = String.valueOf(inputNumber).toCharArray();

        for(char element: tempArr) {
            int temp = Character.digit(element, 10);
            sum += temp;
            product *= temp;
        }
        return sum == product;
    }

    public static boolean isPerfectSquare(long inputNumber) {
        double squareRoot = Math.sqrt((double) inputNumber);
        return Math.ceil(squareRoot) == Math.floor(squareRoot);
    }

    public static boolean isJumping(long inputNumber) {
        char[] tempArr = String.valueOf(inputNumber).toCharArray();

        int firstNumber = Character.digit(tempArr[0], 10) - 1;

        for (char element: tempArr) {
            int elementIntValue = Character.digit(element, 10);
            if (Math.abs(elementIntValue - firstNumber) != 1) {
                return false;
            }
            firstNumber = elementIntValue;
        }
        return true;
    }

    public static boolean isHappy(long inputNumber) {
        long comparedNum = inputNumber;
        ArrayList<Long> calculatedSumOfSquares = new ArrayList<>();
        calculatedSumOfSquares.add(inputNumber);
        while (true) {
            char[] tempArr = String.valueOf(comparedNum).toCharArray();
            int sumOfSquares = 0;

            if (tempArr.length == 1 && Character.digit(tempArr[0], 10) == 1) {
                return true;
            }

            for (char c: tempArr) {
                sumOfSquares += (int) Math.pow(Character.digit(c, 10), 2);
            }


            if (calculatedSumOfSquares.contains((long) sumOfSquares)) {
                break;
            }
            calculatedSumOfSquares.add((long) sumOfSquares);
            comparedNum = sumOfSquares;
        }

        return false;
    }
}
