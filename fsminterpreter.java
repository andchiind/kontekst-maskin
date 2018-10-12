import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class fsminterpreter {

    private static State currentState;
    private static HashSet<State> states = new HashSet<>();
    private static ConcurrentLinkedQueue<String> inputs = new ConcurrentLinkedQueue<>();
    private static HashSet<String> inputSet = new HashSet<>();

    private static void readInstructions(String fileName) {

        String fileContent = ReadFile.readFile(fileName);

        String[] fileLine = fileContent.split(",");

        int n = 0;

        HashSet<String> nextStates = new HashSet<>();

        for (String line : fileLine) {

            String[] elements = line.split(" ");

            boolean exists = false;

            if (elements.length == 4) {

                inputSet.add(elements[1]);

                nextStates.add(elements[3]);

                for (State state : states) {
                    if (state.getName().equals(elements[0])) {
                        state.addMapping(elements[1], elements[2], elements[3]);
                        exists = true;
                    }
                }

                if (!exists) {
                    states.add(new State(elements[0], elements[1], elements[2], elements[3]));
                    if (n == 0) {
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        currentState = (State) states.toArray()[0];
                        n++;
                    }
                }
            } else {
                System.out.println("Bad description");
                System.exit(0);
            }
        }
        for (State state : states) {
            if (!nextStates.contains(state.getName())) {
                System.out.println("Bad description");
                System.exit(0);
            }
        }
        if (nextStates.size() != states.size()) {
            System.out.println("Bad description");
            System.exit(0);
        }
    }

    private static void testStates() {

        boolean found = false;

        for (State state : states) {

            if (state != currentState) {
                found = false;
                state.checkInputSet();
                for (State state1 : states) {
                    if (state1.checkNextState(state)) {

                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Bad description");
                    System.exit(0);
                }
            }
        }
    }

    public static HashSet<String> getInputSet() {
        return inputSet;
    }

    public static void main(String[] args) {

        readInstructions(args[0]);

        if (args[args.length - 1].startsWith("<")) {

            String commands = ReadFile.readFile(args[args.length - 1].replaceFirst("<", ""));
            commands = commands.substring(0, commands.length() - 1);
            String[] inputsFile = commands.split("");
            inputs.addAll(Arrays.asList(inputsFile));

        } else {

            inputs.addAll(Arrays.asList(args).subList(1, args.length));

        }

        testStates();

        while (!inputs.isEmpty()) {

            int size = inputs.size();

            System.out.print(currentState.getOutput(inputs.peek()));

            for (State state : states) {
                if (state.getName().equals(currentState.getNextState(inputs.peek()))) {
                    currentState = state;
                    inputs.poll();
                    break;
                }
            }

            if (size == inputs.size()) {
                System.out.println("Bad input");
                System.exit(0);
            }
        }
        System.out.print("\n");

    }
}
