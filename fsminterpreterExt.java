import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class fsminterpreterExt {

    private static StateExt currentState;
    private static HashSet<StateExt> states = new HashSet<>();
    private static ConcurrentLinkedQueue<String> inputs = new ConcurrentLinkedQueue<>();
    private static HashSet<String> inputSet = new HashSet<>();
    private static Stack<ConcurrentLinkedQueue<String>> queueList = new Stack<>();

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

            for (StateExt state : states) {
                if (state.getName().equals(elements[0])) {
                    state.addMapping(elements[1], elements[2], elements[3]);
                    exists = true;
                }
            }

            if (!exists) {
                states.add(new StateExt(elements[0], elements[1], elements[2], elements[3]));
                if (n == 0) {
                    currentState = (StateExt) states.toArray()[0];
                    n++;
                }
            }
        }

        for (StateExt state : states) {
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

        for (StateExt state : states) {

            state.checkInputSet();

            if (state != currentState) { //No other states need to be able to lead back to the initial state
                found = false;
                for (StateExt state1 : states) {
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

        StateExt temp = currentState;
        ConcurrentLinkedQueue tempQueue = inputs;

        queueList.add(inputs);

        System.out.print(rec(temp.getName()).trim());

        inputs = tempQueue;
        inputs.poll();

    }

    private static String rec(String stateName) {

        StateExt state = null;

        StringBuilder builder = new StringBuilder();

        for (StateExt currentState : states) {
            if (currentState.getName().equals(stateName)) {
                state = currentState;
            }
        }

        int option = 0;

        assert state != null;
        assert inputs.size() > 0;
        while (option < state.getOutputRec(inputs.peek()).size()) {

            for (StateExt nextState : states) {

                if (state.getNextStateRec(inputs.peek()).size() > option && nextState.getName().equals(state.getNextStateRec(inputs.peek()).get(option))) {

                    if (inputs.size() > 1) {

                        int tempOption = option;
                        StateExt tempState = state;

                        ConcurrentLinkedQueue tempQueue = inputs;

                        queueList.add(new ConcurrentLinkedQueue<String>(tempQueue));

                        builder.append(state.getOutputRec(inputs.peek()).get(option));

                        String next = rec(state.getNextStateRec(inputs.poll()).get(option));

                        builder.append(next + "\n");

                        option = tempOption;
                        state = tempState;
                        inputs = queueList.pop();

                    } else {

                        builder.append(state.getOutputRec(inputs.peek()).get(option));

                        return builder.toString();
                    }
                    option++;
                }
            }
        }
        return builder.toString().trim();
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
