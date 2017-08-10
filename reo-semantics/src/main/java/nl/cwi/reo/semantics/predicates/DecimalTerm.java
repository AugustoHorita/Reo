package nl.cwi.reo.semantics.predicates;

import nl.cwi.reo.interpret.typetags.TypeTag;

// TODO: Auto-generated Javadoc
/**
 * Constant that represents absence of data. This value is used to encode
 * synchronization constraints and empty memory cells.
 */
public class DecimalTerm extends Function {
 
	/**
	 * Constructs an null value.
	 */
	public DecimalTerm(double d) {
		super("" + d, new Double(d), null, false, new TypeTag("decimal"));
	}
}