package asteroids.model.programs;

public class SingleStatement<E,S,F> {

	public SingleStatement(Expression expression, Statement statement,String stating){
		this.expression = expression;
		this.stating = stating;
		this.statement = statement;
	}

	private Expression expression;
	private String stating;
	private Statement statement;
	
	public Statement<E,F> getStatement() {return this.statement;}
	public String getStating() {return this.stating;}
	public Expression<?> getExpression() {return this.expression;}	
}
