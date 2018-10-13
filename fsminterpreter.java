import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class fsminterpreter {

    private static State currentState;
    private static HashSet<State> states = new HashSet<>();
    private static ConcurrentLinkedQueue<String> inputs = new ConcurrentLinkedQueue<>();
    private static HashSet<String> inputSet = new HashSet<>();

    public static void main(String[] args) {

        readInstructions(args[0]);

        readInputs();

        testStates();

        simulate();

        System.out.print("\n");

    }

    private static void readInstructions(String fileName) {

        String fileContent = ReadFile.readFile(fileName);

        String[] fileLine = fileContent.split(",");

        int n = 0;

        HashSet<String> nextStates = new HashSet<>();

        for (String line : fileLine) {

            String[] elements = line.split(" ");

            boolean exists = false;

            if (elements.length != 4) {
                System.out.println("Bad description");
                System.exit(0);
            }

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
                    currentState = (State) states.toArray()[0];
                    n++;
                }
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

            state.checkInputSet();

            if (state != currentState) { //No other states need to be able to lead back to the initial state
                found = false;
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

    private static void simulate() {

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
    }

    public static HashSet<String> getInputSet() {
        return inputSet;
    }

    private static void readInputs() {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String input = reader.readLine();

            String[] line = input.split("");

            inputs.addAll(Arrays.asList(line));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
