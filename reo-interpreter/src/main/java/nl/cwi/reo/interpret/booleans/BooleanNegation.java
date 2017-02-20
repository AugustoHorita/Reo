package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.oldstuff.Expression;

public final class BooleanNegation implements BooleanExpression {
	
	private final BooleanExpression e;
	
	public BooleanNegation(BooleanExpression e) {
		if (e == null)
			throw new NullPointerException();
		this.e = e;
	}
	
	public BooleanExpression evaluate(Map<String, Expression> params) throws CompilationException {
		BooleanExpression x = e.evaluate(params);
		if (x instanceof BooleanValue)
			return BooleanValue.negation((BooleanValue)x);
		return new BooleanNegation(x);
	}
	
	@Override
	public String toString() {
		return "!(" + e + ")";
	}
}
