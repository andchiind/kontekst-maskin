import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class fsminterpreterExt {

    private static StateExt currentState;
    private static HashSet<StateExt> states = new HashSet<>();
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

        while (!inputs.isEmpty()) {

            int size = inputs.size();

            //System.out.print(currentState.getOutputRec(inputs.peek()).get(0));

            StateExt temp = currentState;
            ConcurrentLinkedQueue tempQueue = inputs;

            //nonDeterministic(inputs, 0);

            rec(temp.getName());

            //currentState = temp;

            inputs = tempQueue;
            inputs.poll();

            /*for (StateExt state : states) {
                if (state.getName().equals(currentState.getNextStateRec(inputs.peek()[0]))) {
                    currentState = state;
                    inputs.poll();
                    break;
                }
            }*/

            if (size == inputs.size()) {
                System.out.println("weep");
                System.out.println("Bad input");
                System.exit(0);
            }
        }
    }

    private static void rec(String stateName) {

        StateExt state = null;

        for (StateExt currentState : states) {
            if (currentState.getName().equals(stateName)) {
                state = currentState;
            }
        }

        int option = 0;

        /*ConcurrentLinkedQueue nextItem = inputs;

        nextItem.poll();*/

        //System.out.println("name: " + state.getName() + " content: " + state.getOutputRec(inputs.peek()));

        assert state != null;
        assert inputs.size() > 0;
        while (option < state.getOutputRec(inputs.peek()).size()) {

            for (StateExt nextState : states) {

                if (state.getNextStateRec(inputs.peek()).size() > option && nextState.getName().equals(state.getNextStateRec(inputs.peek()).get(option))) {

                    //if (nextState.getOutputRec((String) nextItem.peek()).size() == 1) {

                        System.out.println(state.getOutputRec(inputs.peek()).get(option));
                        System.out.println(state.getOutputRec(inputs.peek()) + " " + inputs.peek() + " " + state.getName() + " " + option);

                        int tempOption = option;
                        StateExt tempState = state;
                        ConcurrentLinkedQueue tempQueue = inputs;

                        if (inputs.size() > 1) {

                            System.out.println("inputsize: " + inputs.size());

                            rec(state.getNextStateRec(inputs.poll()).get(option));

                        }

                        option = tempOption;
                        state = tempState;
                        inputs = tempQueue;

                        option++;
                }
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
