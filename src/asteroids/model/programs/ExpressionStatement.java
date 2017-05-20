package asteroids.model.programs;

import asteroids.model.Program;
import asteroids.model.Ship;
import asteroids.model.World;
import asteroids.part3.programs.SourceLocation;

public class ExpressionStatement<E, F> extends Statement<E,F> {

	public ExpressionStatement(Expression<?> expression, String stating, SourceLocation sourceLocation){
		super(sourceLocation);
		this.expression = expression;
		this.stating = stating;
	}
	private Expression<?> expression;
	private String stating;
	
	public String getStating() {return this.stating;}

	public Expression<?> getExpression() {return this.expression;}
	
	public void setExpression(Expression<?> expression) {
		this.expression = expression;
	}

	public void execute(Ship ship,World world, Program<F,?> program, double deltaT) throws FalseProgramException, FalseReturnException {
		 if (getStating().equals("print")) {
			Object expressionResult = getExpression().read(ship, world, program);
			program.getPrintedObjects().add(expressionResult);
			System.out.println(expressionResult);
			
		 } else if(getStating().equals("turn")){
			 program.setConsumedTime(program.getConsumedTime()+0.2);
			 Double angle = (Double) getExpression().read(ship, world, program);
			 try {
				 ship.turn(angle);
			 } catch (AssertionError e){
				 throw new IllegalArgumentException();
			 }
		 } else if(getStating().equals("return")){
			 if (!program.getIsInFunction()){
				 throw new FalseReturnException("Return outside function body");
			 }
		 }
		 
	}
}
