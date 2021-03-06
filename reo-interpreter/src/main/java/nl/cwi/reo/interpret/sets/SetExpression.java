package nl.cwi.reo.interpret.sets;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.instances.InstanceExpression;
import nl.cwi.reo.interpret.variables.Identifier;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of an atomic/composite set definition.
 */
public interface SetExpression extends InstanceExpression {

	/**
	 * Gets the component.
	 * 
	 * @return component name, or null if this component is nameless.
	 */
	@Nullable
	public String getName();

	/**
	 * Checks whether this set can be evaluated, given a set of defined
	 * variables. This method assumes that all variables that have indices are
	 * defined, and they are ignored.
	 *
	 * @param deps
	 *            the deps
	 * @return true, if the set can be evaluated.
	 */
	public boolean canEvaluate(Set<Identifier> deps);

}
