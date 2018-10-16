import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class represents a state in a finite state machine. It stores a map of what outputs and what the next state should
 * be returned for each possible input. In this extension class, one input could have several outputs and next states associated
 * with it.
 */
public class StateExt {

    private String name;
    private HashMap<String, ArrayList<String>> outputMapRec = new HashMap<>();
    private HashMap<String, ArrayList<String>> nextStateMapRec = new HashMap<>();
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
    public StateExt(String name, String input, String output, String next) {

        this.name = name;

        addMapping(input, output, next);
    }

    /**
     * Here the given input, output and next state combinations are assigned to a relevant map. If the given input has
     * been mapped before, it is assumed that the output and next state represent a non deterministic alternative to
     * the input,  and are therefore added to an arrayList representing all of the options for each input.
     * @param input The input for the transition
     * @param output The output for the transition
     * @param next The next state for the transition
     */
    public void addMapping(String input, String output, String next) {

        if (inputSet.contains(input)) {

            outputMapRec.get(input).add(output);
            nextStateMapRec.get(input).add(next);
            return;
        }

        inputSet.add(input);

        outputMapRec.put(input, new ArrayList<>());
        outputMapRec.get(input).add(output);

        nextStateMapRec.put(input, new ArrayList<>());
        nextStateMapRec.get(input).add(next);

        nextStateSet.add(next);
    }

    /**
     * Returns the name of the state.
     * @return the name of the state
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list containing all of the possible next states associated with the given input.
     * @param input The input from the user
     * @return the list of each possible next state for the given input
     */
    public ArrayList<String> getNextState(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return nextStateMapRec.get(input);
    }

    /**
     * Returns the list containing all of the possible outputs associated with the given input.
     * @param input The input from the user
     * @return the list of each possible output for the given input
     */
    public ArrayList<String> getOutput(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return outputMapRec.get(input);
    }

    /**
     * Checks that the state given in the parameter could be reached from this state.
     * @param state The state that might be reached
     * @return true if it can be reached, false otherwise
     */
    public boolean checkNextState(StateExt state) {
        return nextStateSet.contains(state.getName()) && !state.getName().equals(name);
    }

    /**
     * Checks that all of the possible inputs for this finite state machine are represented in this state. If some are
     * not, the program will assume that if they are encountered the machine will not change state and not output anything.
     */
    public void checkInputSet() {
        for (String input : fsminterpreterExt.getInputSet()) {
            if (!inputSet.contains(input)) {
                addMapping(input, "", name);
            }
        }
    }

}