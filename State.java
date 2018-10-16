import java.util.HashMap;
import java.util.HashSet;

/**
 * This class represents a state in a finite state machine. It stores a map of what outputs and what the next state should
 * be returned for each possible input.
 */
public class State {

    private String name;
    private HashMap<String, String> outputMap = new HashMap<>();
    private HashMap<String, String> nextStateMap = new HashMap<>();
    private HashSet<String> inputSet = new HashSet<>();
    private HashSet<String> nextStateSet = new HashSet<>();

    /**
     * This constructor assigns the name of the class, and assigns the first input, output and next state tuple using
     * the addMapping() method.
     * @param name The name of the state
     * @param input The input for the transition
     * @param output The output for the transition
     * @param next The next state for the transition
     */
    public State(String name, String input, String output, String next) {

        this.name = name;

        addMapping(input, output, next);
    }

    /**
     * Here the given input, output and next state combinations are assigned to a relevant map. If the given input has
     * been mapped before a bad description message is sent. This is because this program does not deal with
     * non-deterministic states.
     * @param input The input for the transition
     * @param output The output for the transition
     * @param next The next state for the transition
     */
    public void addMapping(String input, String output, String next) {

        if (!inputSet.contains(input)) {
            inputSet.add(input);
            outputMap.put(input, output);
            nextStateMap.put(input, next);
            nextStateSet.add(next);
        } else {
            System.out.println("Bad description");
            System.exit(0);
        }

    }

    /**
     * Returns the name of the state.
     * @return the name of the state
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the String name of the next state associated with the given input.
     * @param input The input from the user
     * @return the name of the next state
     */
    public String getOutput(String input) {

        if (!fsminterpreter.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }

        return outputMap.get(input);
    }

    /**
     * Returns the String output associated with the given input.
     * @param input The input from the user
     * @return the output
     */
    public String getNextState(String input) {

        if (!fsminterpreter.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return nextStateMap.get(input);
    }

    /**
     * Checks that the state given in the parameter could be reached from this state.
     * @param state The state that might be reached
     * @return true if it can be reached, false otherwise
     */
    public boolean checkNextState(State state) {
        return nextStateSet.contains(state.getName()) && !state.getName().equals(name);
    }

    /**
     * Checks that all of the possible inputs for this finite state machine are represented in this state. If some are
     * not, the program will output a bad description message and then close.
     */
    public void checkInputSet() {
        for (String input : fsminterpreter.getInputSet()) {
            if (!inputSet.contains(input)) {
                System.out.println("Bad description");
                System.exit(0);
            }
        }
    }

}