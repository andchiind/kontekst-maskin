import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class creates and simulates a finite state machine, based on a description of the machine and inputs given
 * by the user. It also checks that the description of the machine and the inputs are valid, and in this case it does
 * allow for non deterministic states as well as lacking description of inputs for a state.
 */
public class fsminterpreterExt {

    private static StateExt currentState;
    private static HashSet<StateExt> states = new HashSet<>();
    private static ConcurrentLinkedQueue<String> inputs = new ConcurrentLinkedQueue<>();
    private static HashSet<String> inputSet = new HashSet<>();
    private static Stack<ConcurrentLinkedQueue<String>> queueStack = new Stack<>();

    /**
     * The main method creates and checks the finite state machine using the .fsm file specified in the arguments. It
     * then runs over the machine using the inputs specified in the input file, and then prints out the output.
     * @param args The filepath of the finite state machine description
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java fsminterpreterExt <FSM_filepath> <\"<input_filepath\">");
            System.exit(0);
        }

        readInstructions(args[0]);

        readInputs();

        testStates();

        simulate();

        System.out.print("\n");

    }

    /**
     * This method reads the description of the finite state machine and creates a representation of it in memory. Each
     * state is represented with a StateExt object. Some checks are also done to make sure that the description of the
     * machine is legal.
     * @param fileName the filepath of the finite state machine description
     */
    private static void readInstructions(String fileName) {

        String fileContent = ReadFile.readFile(fileName);

        //The line is split on commas since this is generally more reliable than using "\n" in my experience
        String[] fileLine = fileContent.split(",");

        int n = 0;

        HashSet<String> nextStates = new HashSet<>();

        for (String line : fileLine) {

            String[] elements = line.split(" ");

            boolean exists = false;

            if (elements.length != 4) { //This checks that there are 4 values per line in the .fsm file
                System.out.println("Bad description");
                System.exit(0);
            }

            inputSet.add(elements[1]);

            nextStates.add(elements[3]);

            for (StateExt state : states) { //If the state already exists, the new transitions are simply added to the object
                if (state.getName().equals(elements[0])) {
                    state.addMapping(elements[1], elements[2], elements[3]);
                    exists = true;
                }
            }

            if (!exists) { //Here a new state object is created and assigned the first transition described in the .fsm file
                states.add(new StateExt(elements[0], elements[1], elements[2], elements[3]));
                if (n == 0) {
                    currentState = (StateExt) states.toArray()[0];
                    n++;
                }
            }
        }

        for (StateExt state : states) {
            if (!nextStates.contains(state.getName())) { //Checks that all of the states that a state can lead to exist
                System.out.println("Bad description");
                System.exit(0);
            }
        }
        if (nextStates.size() != states.size()) { //Checks that the number of states referenced are the same as the number of states
            System.out.println("Bad description");
            System.exit(0);
        }
    }

    /**
     * Tests that all states can be reached from another state, except for the initial state.
     */
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
                if (!found) { //If no states lead to a given state, the machine has a bad description
                    System.out.println("Bad description");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * This method starts the recursive call which retrieves all possible outputs, and then prints out the result in
     * an easier to understand format.
     */
    private static void simulate() {

        queueStack.add(inputs);

        //Here the recursive method which retrieves all possible outputs is called and the output is placed in a String
        String output = rec(currentState.getName()).trim();

        String[] outputBranches = output.split("\n");

        String prevLine = null;

        for (String line : outputBranches) {

            //The content in the previous line is added to the current one, in order to represent the entire branch of outputs
            if (prevLine != null) {
                line = prevLine.substring(0, prevLine.length() - line.length()) + line;
            }

            System.out.println(line);
            prevLine = line;
        }

    }

    /**
     * This is a recursive method which gathers the output from all of the possible branches of the tree created by
     * non-deterministic states. It first gets all of the output from one branch and then returns the the next nearest
     * branch and retrieves all of the outputs from there. This process is repeated until all possible outputs are gathered.
     * The results are then returned as a single string to the initial method call. The first call should use the initial
     * state name as the parameter.
     * @param stateName The initial state name
     * @return all possible outputs from the given inputs
     */
    private static String rec(String stateName) {

        StateExt state = null;

        StringBuilder builder = new StringBuilder();

        //The current state is located based on the state name given in the parameter
        for (StateExt currentState : states) {
            if (currentState.getName().equals(stateName)) {
                state = currentState;
            }
        }

        int option = 0;

        assert state != null;
        assert inputs.size() > 0;
        while (option < state.getOutput(inputs.peek()).size()) { //This loops until each option for a input has been checked

            for (StateExt nextState : states) {

                if (state.getNextState(inputs.peek()).size() > option && nextState.getName().equals(state.getNextState(inputs.peek()).get(option))) {

                    if (inputs.size() > 1) {

                        int tempOption = option;
                        StateExt tempState = state;

                        ConcurrentLinkedQueue tempQueue = inputs;

                        //This stack keeps track of the input queue from earlier in the branch. The queue is then
                        //retrieved when other branches are checked and a value is returned.
                        queueStack.add(new ConcurrentLinkedQueue<String>(tempQueue));

                        builder.append(state.getOutput(inputs.peek()).get(option));

                        //Here the next state in the branch is explored if the input queue is not empty. 
                        String next = rec(state.getNextState(inputs.poll()).get(option));

                        builder.append(next + "\n");

                        option = tempOption;
                        state = tempState;
                        inputs = queueStack.pop();

                    } else {

                        //If there is only one more input in the input queue, the recursive method is no longer called.
                        builder.append(state.getOutput(inputs.peek()).get(option));

                        return builder.toString();
                    }
                    option++;
                }
            }
        }
        return builder.toString().trim(); //Here, each output leading from the input in the previous loop is returned
    }

    /**
     * Returns the set of all possible inputs described by the machine.
     * @return the set of all possible inputs described by the machine
     */
    public static HashSet<String> getInputSet() {
        return inputSet;
    }

    /**
     * Here, the inputs described in in the command-line are added to the queue of inputs.
     */
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
