package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Iterates over the Cartesian product of a non-empty list of local clauses.
 * Returns a satisfiable conjunction of local clauses.
 */
public class ClausesIterator implements Iterator<List<Formula>> {

	/**
	 * List of conjuncts of clauses
	 */
	private final List<List<Formula>> conjuncts;

	/**
	 * List of iterators of each conjunct
	 */
	private final List<Iterator<Formula>> iters;

	/**
	 * Current combination of local transitions.
	 */
	private List<Formula> tuple;

	/**
	 * Indicates whether the current tuple is a new tuple.
	 */
	private boolean isNext;

	/**
	 * Constructs an iterator that enumerates all global transitions originating
	 * from the global initial state.
	 * 
	 * @param automata
	 *            list of automata
	 * @throws NullPointerException
	 *             if the list is null or empty, or if the list contains a null
	 *             automaton.
	 */
	public ClausesIterator(List<List<Formula>> conjunction) {
		if (conjunction == null)
			throw new NullPointerException("Undefined conjunction.");
		this.conjuncts = new ArrayList<List<Formula>>();
		this.iters = new ArrayList<Iterator<Formula>>();
		this.tuple = new ArrayList<Formula>();
		this.isNext = false;
		for (List<Formula> conjunct : conjunction) {
			if (conjunct == null)
				throw new NullPointerException("Undefined conjunct in conjunction.");
			conjuncts.add(conjunct);
			Iterator<Formula> iter = conjunct.iterator();
			iters.add(iter);
			tuple.add(iter.next());
		}
		isNext = true;
	}

	/**
	 * Checks if there exists another satisfiable combination of local clauses.
	 */
	@Override
	public boolean hasNext() {
		findNext();
		return isNext;
	}

	/**
	 * Gets the next satisfiable combination of local clauses, if possible.
	 * 
	 * @return satisfiable combination of local clauses.
	 * @throws NoSuchElementException
	 *             if there is no next element.
	 */
	@Override
	public List<Formula> next() {
		findNext();
		if (!isNext)
			throw new NoSuchElementException();
		isNext = false;
		return new ArrayList<Formula>(tuple);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Updates, if possible and necessary, the current combination of local
	 * clauses to the next satisfiable combination and sets the isNext flag to
	 * true. If no satisfiable combination exists, the isNext flag is set to
	 * false.
	 */
	private void findNext() {

		// Nothing to do: current tuple is a new combination.
		if (isNext)
			return;

		boolean hasNext = findNext(0);

		while (hasNext) {
			int i;

			loop: for (i = 0; i < conjuncts.size() - 1; i++) {
				for (int j = i + 1; j < conjuncts.size(); j++) {
					if (!satisfiable(tuple.get(i), tuple.get(j)))
						break loop;
				}
			}

			if (i + 1 == conjuncts.size()) {
				isNext = true;
				return;
			} else {
				// if current tuple is not composable, increment i-th iterator,
				// because the tuple remains incomposable after incrementing any
				// k-th iterator (k < i). Note that this optimization does not
				// skip any tuples: Increment the first iterator at index j
				// greater or equal to i, the problematic index, and reset all
				// iterators prior to j. The usual procedure would increment the
				// first iterator that has a next value. This general procedure
				// would continue to yield a composability conflict at index i,
				// until all iterators before index i would not have a next
				// value. In this case, the general procedure would increment
				// the first iterator at index j starting from index i that has
				// a next value and reset all iterators before index j. Thus,
				// simply incrementing the first iterator at index j greater or
				// equal to i and resetting all iterators before index j yields
				// the same result as the general procedure.
				hasNext = findNext(i);
			}
		}
	}
	
	private boolean satisfiable(Formula f, Formula g) {
		Map<Variable, Integer> map = f.getEvaluation();
		for (Map.Entry<Variable, Integer> entry : g.getEvaluation().entrySet())
			if (map.get(entry.getKey())!=null && !entry.getValue().equals(map.get(entry.getKey())))
				return false;
		return true;
	}

	/**
	 * Increments, if possible, the first index j starting from index i and
	 * resets all indices before j.
	 * 
	 * @param i
	 *            lower bound of incremented index.
	 * @return true if the increment was possible, and false if there is no next
	 *         tuple.
	 */
	private boolean findNext(int i) {

		// Find first iterator j the has a next element, starting from i.
		int j;
		for (j = i; j < iters.size(); j++)
			if (iters.get(j).hasNext())
				break;

		// if there is no next j, return false
		if (j == iters.size())
			return false;

		// Reset all iterators before index.
		for (int k = 0; k < j; k++)
			iters.set(k, conjuncts.get(k).iterator());

		// Update the tuple at all indices up to j.
		for (int k = 0; k <= j; k++)
			tuple.set(k, iters.get(k).next());

		return true;
	}
}
