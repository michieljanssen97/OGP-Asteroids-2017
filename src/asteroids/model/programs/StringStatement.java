package asteroids.model.programs;

import asteroids.model.Program;
import asteroids.model.Ship;
import asteroids.model.World;
import asteroids.part3.programs.SourceLocation;

public class StringStatement extends Statement {
	
	String statement;
	
	public StringStatement(String statement, SourceLocation sourceLocation) {
		super(sourceLocation);
		this.statement = statement;
	}
	
	public String getStatement() {
		return this.statement;
	}

	public void execute(Ship ship, World world, Program program, double deltaT) throws BreakException, FalseProgramException, NoMoreTimeException {
		doStuff(ship, world, program, deltaT); 
		switch (getStatement()) {
           case "break":  		throw new BreakException();
           case "thrust_on": 	program.setConsumedTime(program.getConsumedTime()+0.2);
			 					ship.toggleThruster();
			 					break;
           case "thrust_off":   program.setConsumedTime(program.getConsumedTime()+0.2);
			 					ship.toggleThruster();
			 					break;
           case "fire":         program.setConsumedTime(program.getConsumedTime()+0.2);
			 					ship.fireBullet();
			 					break;
           case "skip":			program.setConsumedTime(program.getConsumedTime()+0.2);
           						break;
           default: 			throw new FalseProgramException("Illegal string statement");
		 }
	}

}
