package asteroids.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class World implements ICollidable {
	
	private static final double MAX_WIDTH = Double.MAX_VALUE;
	private static final double MAX_HEIGHT = Double.MAX_VALUE;
	
	private static final double DEFAULT_WIDTH = 1000;
	private static final double DEFAULT_HEIGHT = 1000;
	
	private Set<Ship> ships = new HashSet<Ship>();
	private Set<Bullet> bullets = new HashSet<Bullet>();
	
	private boolean isTerminated;
	
	private double width;
	private double height;
	
	public World(double width, double height) {
		setSize(width,height);
	}
	
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	public void terminate() {
		this.isTerminated = true;
	}

	public double getWidth(){
		return this.width;
	}
	public double getHeight(){
		return this.height;
	}
	
	private boolean isValidWidth(double width) {
		return (0 <= width &&  width <= MAX_WIDTH);
	}
	
	private boolean isValidHeight(double height) {
		return (0 <= height &&  height <= MAX_HEIGHT);
	}
	
	private void setSize(double width, double height){
		
		if (isValidWidth(width)) {
			this.width = width;
		} else {
			this.width = DEFAULT_WIDTH;
		}
		
		if (isValidHeight(height)) {
			this.height = height;
		} else {
			this.height = DEFAULT_HEIGHT;
		}
	}
	
	
	public Set<Ship> getShips(){
		return this.ships;
	}
	
	public Set<Bullet> getBullets(){
		return this.bullets;
	}
	
	public Set<Entity> getEntities(){
		Set<Entity> entities = new HashSet<Entity>();
		entities.addAll(this.bullets);
		entities.addAll(this.ships);
		return entities;
	}
	
	// implement defensively.
	
	public void addEntity(Entity entity) throws NullPointerException {
		if (entity == null){
			throw new NullPointerException();
		} else if (entity.isPartOfWorld() || significantOverlap(entity) || !entity.withinBoundaries(this)){
			throw new IllegalArgumentException();
		} else {
			if (entity instanceof Ship) {
			ships.add((Ship) entity);
			} else if (entity instanceof Bullet) {
			bullets.add((Bullet) entity);
			}
		}
		
	}
	public void removeEntity(Entity entity) throws NullPointerException {
		if (entity == null){
			throw new NullPointerException();
		} else {
			if (entity instanceof Ship) {
			ships.remove(entity);
			} else if (entity instanceof Bullet) {
			bullets.remove(entity);
			}
		}
	}
	
	public double getNextCollisionTime() {
		Entity[] entities = getNextCollisionEntities();
		
		double entityCollisionTime = entities[0].getTimeToCollision(entities[1]);
		double boundaryCollisionTime = entities[0].getTimeToCollision(entities[0].getWorld());
		
		return Math.min(entityCollisionTime, boundaryCollisionTime);
	}
	
	public double[] getNextCollisionPosition() {
		Entity[] entities = getNextCollisionEntities();
		
		double entityCollisionTime = entities[0].getTimeToCollision(entities[1]);
		double boundaryCollisionTime = entities[0].getTimeToCollision(entities[0].getWorld());
		
		double[] entityCollisionPosition = entities[0].getCollisionPosition(entities[1]);
		double[] boundaryCollisionPosition = entities[0].getCollisionPosition(entities[0].getWorld());
		
		if (entityCollisionTime <= boundaryCollisionTime) {
			return entityCollisionPosition;
		} else if (entityCollisionTime > boundaryCollisionTime) {
			return boundaryCollisionPosition;
		} else {
			return null;
		}
		
		
	}
	
	public Entity[] getNextCollisionEntities() {

		Map<Entity[], Double> collisionMap = new HashMap<Entity[], Double>();
		Double collisionTime;
		
		for (Entity entity1 : this.getEntities()) {
			for (Entity entity2: this.getEntities()) {
				try {
			    collisionTime = entity1.getTimeToCollision(entity2);
				} catch (Exception e) {
					continue;
				}
			    if (collisionTime == Double.POSITIVE_INFINITY){continue;}
			    Entity[] collisionArray = { entity1, entity2 };
		    	collisionMap.put(collisionArray, collisionTime);
			}
		}
		
		// Get collision from collisionMap with lowest collisionTime
		Entity[] firstCollisionArray = Collections.min(collisionMap.entrySet(), Map.Entry.comparingByValue()).getKey();
		return firstCollisionArray;
	}
	
	public void evolve(double duration) throws Exception {
		try {
		while (duration > 0) {
		
			// 1. Get first collision, if any
			// Calculate all collisions, immediately continue if an apparent Collision is found
			
			Double firstCollisionTime = getNextCollisionTime();
			
			if (firstCollisionTime > duration || firstCollisionTime.isNaN()) {
				// Advance all bullets and ships delta t seconds
				for (Entity entity : this.getEntities()) {
					entity.move(duration);
				}
				break;
			} else {
				// Advance all bullets and ships until right before delta firstCollisionTime
				for (Entity entity : this.getEntities()) {
					entity.move(firstCollisionTime);
				}
				// resolve collision
				resolveCollision(getNextCollisionEntities()[0], getNextCollisionEntities()[1]);
				//  Subtract firstTimeCollision from delta t and go to step 1.
				duration -= firstCollisionTime;
			}
			
		} 
		} catch (Exception e) {
			throw new Exception();
		}
			
			
	}
	
	public Entity getEntityAtPosition(double x, double y) {
		for (Entity entity : this.getEntities()) {
		    if ((entity.getPositionX() == x) && (entity.getPositionY() == y)) {
		    	return entity;
		    }
		} 
		return null;
	}
	
	private boolean significantOverlap(Entity entity) {
		for (Entity other : this.getEntities()) {
		    if (entity.significantOverlap(other) && (entity != other)) {
		    	return true;
		    }
		} 
		return false;
	}

	private void resolveCollision(ICollidable entity1, ICollidable entity2) {
		if (entity1 instanceof Ship && entity2 instanceof Ship) {
			
			entity1 = (Entity) entity1;
			entity2 = (Entity) entity2;
			
			double deltaPosX = entity2.getPositionX()-entity1.getPositionX();
			double deltaPosY = entity2.getPositionY()-entity1.getPositionY();

			double deltaVelX = entity2.getVelocityX()-entity1.getVelocityX();
			double deltaVelY = entity2.getVelocityY()-entity1.getVelocityY();
			
			double deltaVR = (deltaVelX*deltaPosX)  + (deltaVelY*deltaPosY);
			
			double radiusSum = entity1.getRadius() + entity2.getRadius();
			double J = (2*entity1.getMass()*entity2.getMass()*deltaVR)/((entity1.getMass()+entity2.getMass())*radiusSum);
			
			double Jx = (J*deltaPosX)/radiusSum;	
			double Jy = (J*deltaPosY)/radiusSum;
			
			
			double newVelocityX1 = entity1.getVelocityX() + (Jx/entity1.getMass());
			double newVelocityY1 = entity1.getVelocityY() + (Jy/entity1.getMass());
			
			double newVelocityX2 = entity2.getVelocityX() - (Jx/entity2.getMass());
			double newVelocityY2 = entity2.getVelocityY() - (Jy/entity2.getMass());
			
			entity1.setVelocity(newVelocityX1, newVelocityY1);
			entity2.setVelocity(newVelocityX2, newVelocityY2);
			
		} else if ((entity1 instanceof Ship && entity2 instanceof Bullet) ||
				(entity2 instanceof Ship && entity1 instanceof Bullet)) {
			
		}
	}


	@Override
	public double getTimeToCollision(ICollidable other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getCollisionPosition(ICollidable other) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
