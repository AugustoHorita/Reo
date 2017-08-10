package nl.cwi.reo.semantics.predicates;

import java.util.Map;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

/**
 * Constant that represents absence of data. This value is used to encode
 * synchronization constraints and empty memory cells.
 */
public class NullValue extends Function {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean isnull = true;

	/**
	 * Constructs an null value.
	 */
	public NullValue() {
		super("*", "null", null, false, new TypeTag(""));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		return new NullValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term substitute(Term t, Variable x) {
		return new NullValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term evaluate(Scope s, Monitor m) {
		return new NullValue();
	}

}
