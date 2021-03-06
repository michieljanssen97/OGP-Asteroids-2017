package asteroids.model.programs.statements;

import asteroids.model.Program;
import asteroids.model.Ship;
import asteroids.model.World;
import asteroids.model.programs.BreakException;
import asteroids.model.programs.FalseProgramException;
import asteroids.model.programs.FalseReturnException;
import asteroids.model.programs.NoMoreTimeException;
import asteroids.model.programs.expressions.Expression;
import asteroids.part3.programs.SourceLocation;

public class WhileStatement extends Statement {

	private Expression<Boolean> condition;
	private Statement body;
	
	public WhileStatement(Expression<Boolean> condition, Statement body, SourceLocation sourceLocation) {
		super(sourceLocation);
		this.condition = condition;
		this.body = body;
	}

	public Statement getBody() {return this.body;}
	public Expression<Boolean> getCondition() {return this.condition;}
	
	public void execute(Ship ship, World world, Program program, double deltaT)
			throws FalseProgramException, BreakException, NoMoreTimeException, FalseReturnException {
		checkTimeLeft(ship, world, program, deltaT);
		while (getCondition().read(ship, world, program, deltaT)){
			 try {
				 body.execute(ship, world, program, deltaT);
			 } catch (BreakException e) {
				 break;
			 }
		 }	
		
	}	
}
