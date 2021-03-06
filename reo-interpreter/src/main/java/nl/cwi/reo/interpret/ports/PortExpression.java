package nl.cwi.reo.interpret.ports;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a port expression.
 */
public final class PortExpression extends VariableExpression {

	/**
	 * Priority type: ampersand, plus, none.
	 */
	private final PrioType prio;

	/**
	 * Constructs a new port expression.
	 * 
	 * @param prio
	 *            priority type of port
	 * @param var
	 *            variable defining port
	 */
	public PortExpression(PrioType prio, VariableExpression var) {
		super(var.getName(), var.getIndices(), var.getLocation());
		this.prio = prio;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Port> evaluate(Scope s, Monitor m) {
		List<Port> ports = new ArrayList<Port>();
		List<? extends Identifier> list = super.evaluate(s, m);
		if (list == null)
			return null;
		for (Identifier x : list)
			ports.add(new Port(x.toString(), PortType.NONE, prio, null, true));
		return ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return prio + super.toString();
	}
}
