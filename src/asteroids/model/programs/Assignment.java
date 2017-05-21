package asteroids.model.programs;

import java.util.Arrays;

import asteroids.model.Entity;
import asteroids.model.Program;
import asteroids.model.Ship;
import asteroids.model.World;
import asteroids.part3.programs.SourceLocation;

public class Assignment extends Statement {
	
	public Assignment(String variableName, Expression<?> value, SourceLocation sourceLocation){
		super(sourceLocation);
		this.value = value;
		this.variableName = variableName;
	}
	
	private Expression<?> value;
	private String variableName;

	public String getVariableName() {return this.variableName;}
	public Expression<?> getExpression() {return this.value;}
	
	public void execute(Ship ship, World world, Program program, double deltaT) throws FalseProgramException, BreakException, NoMoreTimeException {
		doStuff(ship, world, program, deltaT); 
		if (getExpression() instanceof Expression){
			 Object assignedValue = getExpression().read(ship, world, program);
			 
			 if (!(program.getVariables().containsKey(getVariableName())) 
					 && Arrays.asList(Boolean.class, Double.class, Entity.class).contains(assignedValue.getClass())) {
				 program.getVariables().put(getVariableName(), assignedValue);
			 } else {
				 throw new FalseProgramException("Not a correct assignment");
			 }
		 }
		 else
			 throw new FalseProgramException("Not a correct assignment");

	}	 
}
