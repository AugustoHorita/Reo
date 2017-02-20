package nl.cwi.reo.interpret;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerPA;
import nl.cwi.reo.interpret.oldstuff.SemanticsType;
import nl.cwi.reo.semantics.portautomata.PortAutomaton;

public class InterpreterPA extends Interpreter<PortAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for Port Automaton semantics.
	 * @param dirs		list of directories of Reo components
	 */
	public InterpreterPA(List<String> dirs, List<String> params) {
		super(SemanticsType.PA, new ListenerPA(), dirs, params);	
	}	
}
