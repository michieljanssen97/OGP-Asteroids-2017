package asteroids.model.programs;

import asteroids.model.Program;
import asteroids.model.Ship;
import asteroids.model.World;
import asteroids.part3.programs.SourceLocation;

public class DoubleExpression<T> extends Expression<T> {
	
	public DoubleExpression(Expression<T> left, Expression<T> right, String operator, SourceLocation sourceLocation) {
		super(null, sourceLocation);
		this.left = left;
		this.right = right;
		this.operator = operator;
	}
	
	private Expression<T> left;
	private Expression<T> right;
	private String operator;
	public String getOperator() {return this.operator;}
	
	public Expression<T> getLeftValue(){
		return this.left;
	}
	public Expression<T> getRightValue(){
		return this.right;
	}
	
	public T read(Ship ship, World world, Program program) throws FalseProgramException{
		Object result;
		switch (getOperator()) {
			case "+":		result = (Double) getLeftValue().read(ship, world, program) + (Double) getRightValue().read(ship, world, program);	
							break;
			case "*":		result = (Double) getLeftValue().read(ship, world, program) * (Double) getRightValue().read(ship, world, program);	    	
							break;
			case "<":		result = (Double) getLeftValue().read(ship, world, program) < (Double) getRightValue().read(ship, world, program);	
							break;
			case "==":		result = getLeftValue().read(ship, world, program) == getRightValue().read(ship, world, program);
							break;
			default:		throw new FalseProgramException("Double expression is not correct declared");
		}
		return (T) result;
	}
}
