import java.util.HashMap;
class State {
    HashMap<Character, State> transition = new HashMap<>();
    int tokenType = -1;

    public State addTransition(char ch, State nextState) {
        transition.put(ch, nextState);
        return this;
    }

}

// not asked to implement in homework file
public class TransitionDiagram {
    private int[][] transitionTable;

    public int getNextState(int state, char c) {
        int nextState = -1;
        for (int i = 0; i < transitionTable.length; i++) {
            if (transitionTable[i][0] == state && transitionTable[i][1] == c) {
                nextState = transitionTable[i][2];
                break;
            }
        }
        return nextState;
    }

    static State startState;

    static {
        // Define the states
        State numberState = new State();
        State idState = new State();
        State opState = new State();
        State relopState = new State();
        State assignState = new State();
        State lparenState = new State();
        State rparenState = new State();
        State semiState = new State();
        State commaState = new State();
        State funcretState = new State();
        State beginState = new State();
        State endState = new State();
        startState = new State();

        // Define the transitions
        numberState.tokenType = Parser.NUM;
        for (char ch = '0'; ch <= '9'; ch++) {
            numberState.addTransition(ch, numberState);
        }
        numberState.addTransition('.', numberState);

        idState.tokenType = Parser.ID;
        for (char ch = 'a'; ch <= 'z'; ch++) {
            idState.addTransition(ch, idState);
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            idState.addTransition(ch, idState);
        }

        opState.tokenType = Parser.OP;
        opState.addTransition('+', opState);
        opState.addTransition('-', opState);
        opState.addTransition('*', opState);
        opState.addTransition('/', opState);

        relopState.tokenType = Parser.RELOP;
        relopState.addTransition('<', relopState);
        relopState.addTransition('>', relopState);
        relopState.addTransition('=', relopState);
        relopState.addTransition('!', relopState);

        assignState.tokenType = Parser.ASSIGN;
        assignState.addTransition('-', assignState);
        assignState.addTransition('>', assignState);

        lparenState.tokenType = Parser.LPAREN;
        lparenState.addTransition('(', lparenState);

        rparenState.tokenType = Parser.RPAREN;
        rparenState.addTransition(')', rparenState);

        semiState.tokenType = Parser.SEMI;
        semiState.addTransition(';', semiState);

        commaState.tokenType = Parser.COMMA;
        commaState.addTransition(',', commaState);

        funcretState.tokenType = Parser.FUNCRET;
        funcretState.addTransition('-', funcretState);
        funcretState.addTransition('>', funcretState);

        beginState.tokenType = Parser.BEGIN;
        beginState.addTransition('{', beginState);

        endState.tokenType = Parser.END;
        endState.addTransition('}', endState);

        startState.addTransition('0', numberState);
        for (char ch = '1'; ch <= '9'; ch++) {
            startState.addTransition(ch, numberState);
        }
        startState.addTransition('.', numberState);

        startState.addTransition('+', opState);
        startState.addTransition('-', opState);
        startState.addTransition('*', opState);
        startState.addTransition('/', opState);

        startState.addTransition('<', relopState);
        startState.addTransition('>', relopState);
        startState.addTransition('=', relopState);
        startState.addTransition('!', relopState);

        startState.addTransition('<', assignState);
        startState.addTransition('-', assignState);

        startState.addTransition('(', lparenState);

        startState.addTransition(';', semiState);

        startState.addTransition(',', commaState);

        startState.addTransition('-', funcretState);
        startState.addTransition('>', funcretState);

        startState.addTransition('{', beginState);

        startState.addTransition('}', endState);

        for (char ch = 'a'; ch <= 'z'; ch++) {
            startState.addTransition(ch, idState);
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            startState.addTransition(ch, idState);
        }
        startState.addTransition('_', idState);
    }
}