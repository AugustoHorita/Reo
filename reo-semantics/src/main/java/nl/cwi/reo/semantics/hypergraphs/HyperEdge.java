package nl.cwi.reo.semantics.hypergraphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.rulebasedautomata.Rule;

// TODO: Auto-generated Javadoc
/**
 * A hyperedge in a constraint hypergraph that connects a single port node with
 * a set of rule nodes.
 */
public class HyperEdge {

	/**
	 * Current total number of hyperedges. Used to provide each hyperedge with a
	 * unique identifier that avoids repeated computation of the hash value.
	 */
	static int N = 0;

	/**
	 * Unique identifier of this rule.
	 */
	private int id;

	/**
	 * The source node of this hyperedge.
	 */
	private Port port;

	/**
	 * The set of rules in the target of this hyperedge.
	 */
	private Set<RuleNode> rules;

	/**
	 * Constructs a new hyperedge from a given port and a set of rules.
	 * 
	 * @param root
	 *            port
	 * @param leaves
	 *            set of rules
	 */
	public HyperEdge(Port root, Set<RuleNode> leaves) {
		this.port = root;
		this.rules = new HashSet<>();
		id = ++N;
		for (RuleNode r : leaves)
			r.addToHyperedge(this); // TODO this object is not initialized and
									// cannot be used here
	}

	/**
	 * Gets the source port of this hyperedge.
	 * 
	 * @return source port of this hyperedge.
	 */
	public Port getSource() {
		return port;
	}
	
	/**
	 * Gets the set of rules in the target of this hyperedge.
	 * 
	 * @return set of rules in the target of this hyperedge.
	 */
	public Set<RuleNode> getTarget() {
		return rules;
	}

	/**
	 * Gets the disjunction over all rules in the target of this hyperedge.
	 * 
	 * @return disjunction over all rules in the target of this hyperedge.
	 */
	public Rule getRule() {
		List<Formula> list = new ArrayList<Formula>();
		for (RuleNode r : rules) {
			list.add(r.getRule().getFormula());
		}
		return new Rule(new Disjunction(list));
	}

	/**
	 * Adds a rule to the target of this hyperedge.
	 * 
	 * @param r
	 *            rule
	 * @return reference to this hyperedge.
	 */
	public void addLeave(RuleNode r) {
		rules.add(r);
	}

	/**
	 * Adds as a list of rules to the target of this hyperedge.
	 * 
	 * @param r
	 *            list of rules
	 * @return reference to this hyperedge.
	 */
	public void addLeaves(List<RuleNode> r) {
		rules.addAll(r);
	}

	/**
	 * Removes a set of rules from this hyperedge.
	 * 
	 * @param r
	 *            set of rules
	 * @return reference to this hyperedge.
	 */
	public void rmLeaves(Set<RuleNode> r) {
		for (RuleNode rule : r)
			rule.rmFromHyperedge(this);
	}

	/**
	 * Removes a rule from the target of this hyperedge.
	 * 
	 * @param r
	 *            rule
	 */
	public void rmLeave(RuleNode r) {
		rules.remove(r);
	}

	/**
	 * Construct a new instance of a hyperedge with the same ports and rules as
	 * this hyperedge.
	 * 
	 * @return new hyperedge with the same ports and rules as this hyperedge.
	 */
	public HyperEdge duplicate() {
		return new HyperEdge(this.port, this.rules);
	}

	/**
	 * Composes this hyperedge with another hyperedge with the same source port
	 * by distributing their rules.
	 * 
	 * @param h
	 *            hyperedge
	 * @return reference to this hyperedge.
	 * @throws IllegalArgumentException
	 *             if the hyperedges have different source ports.
	 */
	public void compose(HyperEdge h) {
		if (!port.equals(h.getSource()))
			new IllegalArgumentException("Hyperedges must have the same source.");

		/*
		 * Check if the hyperedge has a single leaf or several leaves
		 */
		if (h.getTarget().size() == 1) {

			/*
			 * Remove the leaf from the hypergraph, and store it in h_ruleNode.
			 */
			RuleNode h_ruleNode = h.getTarget().iterator().next();
			h_ruleNode.rmFromHyperedge(h);

			/*
			 * Get all rules of this.hyperedge, and compose all of them with the previous rule stored in h_ruleNode.
			 */
			Queue<RuleNode> rulesToCompose = new LinkedList<RuleNode>(rules);
			Boolean equal =false;
			while (!rulesToCompose.isEmpty()) {
				/*
				 * take the next rule to compose
				 */
				RuleNode r = rulesToCompose.poll();
//				if(!r.equals(h_ruleNode)){
					RuleNode rule = r.composeF(h_ruleNode); 
					if (rule!=null){
						if(!rule.equals(r))
							r.erase();
						else
							equal=true;
					}
					if(rule==null)
						r.erase();
//				}
			}
			if(!equal)
				h_ruleNode.erase();

		} else {

			Set<RuleNode> list = new HashSet<RuleNode>(rules);

			Queue<RuleNode> rulesToCompose = new LinkedList<RuleNode>(h.getTarget());
			Set<RuleNode> areEqual = new HashSet<>();

			// Compose This hyperedge with rulesToCompose (where size must be greater than 1).
			while (!rulesToCompose.isEmpty()) {
				/*
				 * Take out r1 from the hypergraph
				 */
				RuleNode r1 = rulesToCompose.poll();
				r1.rmFromHyperedge(h); // TODO r1 may be null
				Boolean equal = false;
				/*
				 * Compose r1 with all the rules of This rules (stored in list).
				 */
				for (RuleNode r2 : list) {
					/*
					 * compose both rules, and eventually add the new rule to the hypergraph
					 */
					RuleNode r = r1.composeF(r2);
					if (r != null && r.equals(r2)) {
						areEqual.add(r2);
						equal = true;
					}
				}
				/*
				 * remove old rules from the hypergraph.
				 */
				if (!equal)
					r1.erase();
				else
					r1.rmFromHyperedge(h);
			}

			/*
			 * remove duplicates
			 */
			for (RuleNode r : list)
				if (!areEqual.contains(r))
					r.erase();
		}
	}

	/**
	 * Hides a given port node from each rule in the target of this
	 * hyperedge.
	 * 
	 * @param p
	 *            port node
	 * @return reference to this hyperedge.
	 */
	public void hide(Port p) {
		for (RuleNode r : rules)
			r.hide(p);
		// TODO shouldn't we also hide the source of the transition, if it
		// equals p?
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof HyperEdge))
			return false;
		HyperEdge p = (HyperEdge) other;
		return Objects.equals(this.id, p.id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "";
		int i = rules.size();
		for(RuleNode r : rules){
			s=s+r.toString() + (i>1?" || \n ":"");
			i--;
		}
		return s;
	}

}
