import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class creates and simulates a finite state machine, based on a description of the machine and inputs given
 * by the user. It also checks that the description of the machine and the inputs are valid.
 */
public class fsminterpreter {

    private static State currentState;
    private static HashSet<State> states = new HashSet<>();
    private static ConcurrentLinkedQueue<String> inputs = new ConcurrentLinkedQueue<>();
    private static HashSet<String> inputSet = new HashSet<>();

    /**
     * The main method creates and checks the finite state machine using the .fsm file specified in the arguments. It
     * then runs over the machine using the inputs specified in the input file, and then prints out the output.
     * @param args The filepath of the finite state machine description
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java fsminterpreter <FSM_filepath> <\"<input_filepath\">");
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

            for (State state : states) { //If the state already exists, the new transitions are simply added to the object
                if (state.getName().equals(elements[0])) {
                    state.addMapping(elements[1], elements[2], elements[3]);
                    exists = true;
                }
            }

            if (!exists) { //Here a new state object is created and assigned the first transition described in the .fsm file
                states.add(new State(elements[0], elements[1], elements[2], elements[3]));
                if (n == 0) {
                    currentState = (State) states.toArray()[0];
                    n++;
                }
            }
        }

        for (State state : states) {
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

    /**
     * This method goes through the entire queue of inputs. At each output it prints out the output associated with
     * the current input, and then transitions to the next state associated with the current input. This is done until
     * the queue is empty.
     */
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

            if (size == inputs.size()) { //This checks that the next state was successfully found
                System.out.println("Bad input");
                System.exit(0);
            }
        }
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
