package asteroids.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * 
 * A class that defines a entity for the Asteroids game.
 *
 * @author Michiel Janssen & Jelle Pelgrims
 *
 */

public abstract class Entity implements ICollidable {
	
	protected static double MAX_SPEED = 300000;
	protected static double MIN_RADIUS = 0;
	protected static double MIN_DENSITY = 0;
	
	protected double x;
	protected double y;
	
	protected double velocityX;
	protected double velocityY;
	
	protected double radius;
	protected double orientation;
	protected double mass;
	
	protected World world;
	
	protected boolean isTerminated = false;
	
	/**
	 * Initialize this new entity with a given position, velocity, radius.
	 * 
	 * @param x
	 *        The x-coordinate for this new entity.
	 * @param y
	 * 		  The y-coordinate for this new entity.
	 * @param xVelocity
	 *        The velocity in the x direction for this new entity.
	 * @param yVelocity
	 *        The velocity in the y direction for this new entity.
	 * @param radius
	 * 		  The given radius for this new entity.
	 * @post  The position of this new entity is equal to the given x- and y-coordinate.
	 * 		  | new.getPositionX() == x
	 * 	      | new.getPositionY() == y
	 * @post  The velocity of this new entity is equal to the given x- and y-velocity.
	 *        | new.getVelocityX() == xVelocity
	 *        | new.getVelocityY() == yVelocity
	 * @post  The radius of this new entity is equal to the given radius.
	 *        | new.getRadius() == radius
	 */
	public Entity(double x, double y, double xVelocity, double yVelocity, double radius) {
		setPosition(x, y);
		setVelocity(xVelocity, yVelocity);
		setRadius(radius);
	}
	
	/**
	 * Returns a boolean indicating whether the entity is terminated
	 * @see implementation
	 */
	@Basic @Immutable
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	/**
	 * Terminates the entity
	 * @post ...
	 * 		 | new.isTerminated() == true
	 */
	public void terminate() {
		this.isTerminated = true;
	}
	
	/**
	 * Check whether a given position is valid by returning a boolean indicating validness.
	 * 
	 * @param x
	 *        The x-coordinate for this new entity.
	 * @param y
	 * 		  The y-coordinate for this new entity.
	 * @invar Both x and y parameters must be a valid number
	 * 		  | Double.isNaN(x) != true
	 * 		  | Double.isNaN(y) != true
	 * @return result == !(Double.isNaN(x) || Double.isNaN(y))
	 */
	protected static boolean isValidPosition(double x, double y) {
		return !(Double.isNaN(x) || Double.isNaN(y));
	}
	
	/**
	 * Return the x-coordinate of this entity.
	 */
	@Basic @Immutable
	public double getPositionX() {return this.x;}
	
	/**
	 * Return the y-coordinate of this entity.
	 */
	@Basic @Immutable
	public double getPositionY() {return this.y;}
	
	/**
	 * Set the entity to a valid position.
	 * 
	 * Implement defensively
	 * 
	 * @param x
	 *        The x-coordinate for this new entity.
	 * @param y
	 *        The y-coordinate for this new entity.
	 * @invar x and y are real numbers. 
	 * 		  | Double.isNaN(x) != true
	 * 		  | Double.isNaN(y) != true
	 * @invar The given coordinate values x and y must be valid
	 * 		  | isValidPosition(x, y) == true
	 * @post  The position of the entity is equal to the given x- and y-coordinate.
	 *        | new.getPositionX() == x
	 *        | new.getPositionY() == y
	 * @throws IllegalArgumentException
	 * 		   The given coordinate values are not valid
	 *         | ! isValidPosition(x,y)
	 */
	@Raw
	public void setPosition(double x, double y) throws IllegalArgumentException{
		if (isValidPosition(x, y)) {
			this.x = x;
			this.y = y;
		} else {
			throw new IllegalArgumentException("Position must not be NaN.");
		}
	}

	/**
	 * Check whether the given duration is valid.
	 * 
	 * @param  duration
	 * 		   The duration of this ship.
	 * @invar The given duration must be a valid number
	 * 		| Double.isNaN(duration) != true
	 * @return result == duration >= 0
	 */
	protected boolean isValidDuration(double duration) {
		return duration >= 0;
	}
	
	/**
	 * Check whether a given velocity is a valid velocity by 
	 * returning a boolean indicating validness.
	 * 
	 * @param x   
	 *        The x-velocity of this entity.
	 * @param y
	 *        The y-velocity of this entity.
	 * @invar x and y are valid numbers. 
	 * 		  | Double.isNaN(x) != true
	 * 		  | Double.isNaN(y) != true
	 * @return True if and only if the speed is greater then zero 
	 *         and the speed is less or equal to MAX_SPEED.
	 *         | result == 
	 *         |       (0 <= speed && speed <= MAX_SPEED)
	 */
	protected boolean isValidVelocity(double x, double y) {
		if (Double.isNaN(x) || Double.isNaN(y)) {
			return false;
		} else {
			double speed = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
			return 0 <= speed && speed <= getMaxSpeed();
		}
	}

	/**
	 * Return the horizontal velocity of this entity.
	 */
	@Basic @Immutable
	public double getVelocityX() {
		return this.velocityX;
	}

	/**
	 * Return the vertical velocity of this entity.
	 */
	@Basic @Immutable
	public double getVelocityY() {
		return this.velocityY;
	}
	
	/**
	 * Set the velocity to a given valid velocity. If the magnitude of the velocity is larger than the allowed maximum speed (MAX_SPEED)
	 * it will be reduced until it is valid (=MAX_SPEED). If the given velocity is NaN the velocity will be set to zero.
	 * 
	 * Implemented totally.
	 * 
	 * @param x
	 * 	      The x-coordinate of this entity.
	 * @param y
	 *        The y-coordinate of this entity.
	 * @invar x and y are real numbers. 
	 * 		  | Double.isNaN(x) != true
	 * 		  | Double.isNaN(y) != true
	 * @post  If the magnitude of the velocity is less then MAX_SPEED 
	 *        then we set the velocity to the given velocity.
	 *        | new.getVelocityX() == x
	 *        | new.getVelocityY() == y
	 * @post  If the magnitude of the velocity is greater then MAX_SPEED
	 *        then we reduce the magnitude until it becomes the MAX_SPEED.
	 *        The tangent of the enclosed angle should remain constant 
	 *        when reducing xVelocity and yVelocity.
	 *        | tan(alfa) = this.getVelocityY/this.getVelocityX = new.getVelocityY/new.getVelocityX = MAX_SPEED
	 *        | new.getVelocityX = sqrt((MAX_SPEED^2)/(1+tan(alfa)^2))
	 *        | new.getVelocityY = sqrt(new.getVelocityX*tan(alfa))
	 */
	public void setVelocity(double x, double y) {
		if (isValidVelocity(x, y)) {
			this.velocityX = x;
			this.velocityY = y;
		} else {
			if (! (Double.isNaN(x) || Double.isNaN(y))) {
				double constantAngle = Math.atan(y/x);
			    this.velocityX = Math.sqrt((Math.pow(getMaxSpeed(),2))/(1+Math.pow(Math.tan(constantAngle), 2)));
				this.velocityY = this.velocityX * Math.tan(constantAngle) ; 
			} else {
				this.velocityX = 0;
				this.velocityY = 0;
			}
		}
	}

	/**
	 * Returns the entities maximum speed
	 */
	@Basic @Immutable
	public abstract double getMaxSpeed();
	
	/**
	 * Returns the entities minimum radius
	 */
	@Basic @Immutable
	public abstract double getMinRadius();
	
	/**
	 * Returns the entities minimum density
	 */
	@Basic @Immutable
	public abstract double getMinDensity();
	
	/**
	 * Returns the radius of this entity.
	 */
	@Basic @Immutable
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Check whether a radius is valid. The radius must be greater then MIN_RADIUS.
	 * 
	 * @param radius
	 * @invar radius is a real number
	 * 		| Double.isNaN(radius) == false
	 * @see implementation
	 */
	protected boolean isValidRadius(double radius) {
		return (radius > getMinRadius()) && (!Double.isNaN(radius));
	}
		
	/**
	 * Set the radius to a given valid radius, throws an error if the radius is not valid.
	 * 
	 * Implement defensively.
	 * 
	 * @param radius
	 * @invar radius must be valid
	 * 		  | isValidRadius(radius) == true
	 * @post  The radius of the entity is equal to the given radius.
	 *        | new.getRadius() == radius
	 * @throws IllegalArgumentException
	 * 		   The given radius is not a valid radius.
	 *         | ! isValidRadius(radius)
	 */
	@Raw
	public void setRadius(double radius) throws IllegalArgumentException {
		if (isValidRadius(radius)) {
			this.radius = radius;
		} else {
			throw new IllegalArgumentException("Radius must be larger than the minimum given radius.");
		}
	}
	
	/**
	 * Return the orientation of this entity.
	 */
	@Basic @Immutable
	public double getOrientation() {
		return this.orientation;
	}

	/**
	 * Sets the orientation to the given angle;
	 * 
	 * Implement nominally.
	 * 
	 * @pre The angle must be between 0 and 2*pi.
	 * 		| 0 <= angle < 2*pi
	 * @post The orientation of the entity is equal to the given angle.
	 *       | new.getOrientation() == angle
	 * @param angle
	 */
	public void setOrientation(double angle) {
		this.orientation = angle;
	}
	
	/**
	 * Returns the entities mass
	 */
	@Basic @Immutable
	public double getMass() {
		return this.mass;
	}
	
	protected boolean isValidMass(double mass){return true;};
	
	/**
	 * Set the mass to a given valid mass, otherwise set to the minimum valid mass
	 * 
	 * @invar The mass must be valid
	 * 		| isValidMass(mass) == true
	 * @param mass
	 * @post The entities mass is set to the given mass if valid, otherwise it is set to the minimum valid mass
	 * 		| if (isValidMass(mass))
	 * 	    | 	new.getMass() == mass
	 * 		| else 
	 * 		| 	new.getMass() == (4.0/3.0)*Math.PI*Math.pow(this.getRadius(), 3)*MIN_DENSITY;
	 */
	@Raw
	protected void setMass(double mass) {
		if (isValidMass(mass)) {
			this.mass = mass;
		} else {
			this.mass = (4.0/3.0)*Math.PI*Math.pow(this.getRadius(), 3)*getMinDensity();
		}
	}
	
	/**
	 * Returns the world this entity is associated with
	 * @see implementation
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Returns a boolean indicating whether an entity belongs to a world
	 * @see implementation
	 */
	public boolean isPartOfWorld() {
		return this.getWorld() != null;
	}
	
	/**
	 * Returns a boolean indicating whether an entity can be part of a world
	 * @see implementation
	 */
	public boolean canBePartOfWorld() {
		return !isPartOfWorld();
	}

	/**
	 * Associates this entity with a particular world
	 * @param world
	 * @invar The given world must be a valid World
	 * 		  | !(world == null) == true
	 * @post The entity is associated with the given world
	 * 		| if (!isPartOfWorld())
	 * 		|		new.world == world
	 */		
	public void makePartOfWorld(World world) {
		if (canBePartOfWorld()) {
			this.world = world;
		}
	}
	
	/**
	 * Removes any association with a world
	 * @see implementation
	 */
	public void removeFromWorld() {
		this.world = null;
	}
	
	/**
	 * This method measures the distance between two given entities.
	 * 
	 * Implement defensively. 
	 * 
	 * @param  other
	 * 		   The second (other) entity. We use this entity to measure the distance.
	 * @invar Other must be a valid entity
	 * 		  | !(other = null) == true
	 * @return The distance between two given entities
	 *         | if (other == this)
	 *         |	result = 0.0 
	 *         | else
	 *         |	result = Math.sqrt(Math.pow((other.getPositionX()-this.getPositionX()), 2)
	 *         |						+ Math.pow((other.getPositionY()-this.getPositionY()), 2))
	 * @throws NullPointerException
	 *         The other entity does not exist.
	 *         | other == null
	 */
	public double getDistanceBetween(Entity other) throws NullPointerException {
		if (other == this) {
			return new Double(0);
		} else if (other == null) {
			throw new NullPointerException();
		}
		else {
			double distance = Math.sqrt(Math.pow((other.getPositionX()-this.getPositionX()), 2)+Math.pow((other.getPositionY()-this.getPositionY()), 2));
			return distance;
		}
	}
	
	/**
	 * This method determines whether there is overlap between two entities.
	 * 
	 * Implement defensively.
	 *  
	 * @param  other
	 *         The second (other) entity. We use this entity to determine the possibility of overlap.
	 * @invar Other must be a valid entity
	 * 		  | !(other = null) == true
	 * @return True if and only if two entities overlap.
	 *         This means that the other entity must not be null.
	 *         Two entities will overlap (return true) if the sum of their radii is 
	 *         greater then the distance between these entities.
	 *          -> r1+r2 >= sqrt( (x2-x1)**2 + (y2-y1)**2 )    
	 *         | result ==
	 *         |       (getRadius()+ other.getRadius) >=
	 *         |       (getDistanceBetween(other))   
	 * @throws NullPointerException
	 *         The other entity does not exist.
	 *         | other == null
	 */
	public boolean overlap(Entity other) throws NullPointerException {
		if (other == null) {
			throw new NullPointerException();
		} else {
			double radiusSum = this.getRadius() + other.getRadius();
			double distance = getDistanceBetween(other);
			return radiusSum >= distance;
		}
	}
	
	/**
	 * Helper function to use the correct getTimeToCollision function
	 * 
	 * @param collidable
	 * @invar collidable must be a valid ICollidable
	 * 		  | !(collidable == null) == true
	 * @post Returns the getTimeToCollision function associated with the given ICollidable
	 * 		 | if (ICollidable instanceof Entity)
	 * 		 | 		result == getTimeToCollision((Entity) collidable)
	 * 		 | else if (ICollidable instanceof World)
	 * 		 | 		result == getTimeToCollision((World) collidable)
	 * 		 | else 
	 * 		 | 		result == 0
	 */
	@Override
	public double getTimeToCollision(ICollidable collidable) {
		if (collidable instanceof Entity) {return this.getTimeToCollision((Entity) collidable);}
		else if (collidable instanceof World) {return this.getTimeToCollision((World) collidable);}
		else {return 0;}
	}
	
	/**
	 * This method calculates when, if ever, two entities will collide. 
	 * 
	 * Implement defensively.
	 * 
	 * @param other
	 *        The second (other) entity. We use this entity to determine the time to collision.
	 * @return This function returns a number indicating the time to collision between two entities,
	 * 		   such that: 
	 * 				* If the two entities are on a collision course a positive number is returned indicating the time to collision
	 * 				* If the two entities overlap a negative number is returned indicating the time until the two entities do not overlap anymore
	 * 				* If the two entities are not on a collision course positive infinity is returned
	 * @throws NullPointerException
	 *         The other entity does not exist.
	 *         | other == null
	 */
	public double getTimeToCollision(Entity other) throws NullPointerException, IllegalArgumentException {
		if (other == null){
			throw new NullPointerException();
		} else if (other == this){
			throw new IllegalArgumentException();
		} else {
			
			double deltaPosX = other.getPositionX()-this.getPositionX();
			double deltaPosY = other.getPositionY()-this.getPositionY();

			double deltaVelX = other.getVelocityX()-this.getVelocityX();
			double deltaVelY = other.getVelocityY()-this.getVelocityY();
			
			double deltaRR = Math.pow(deltaPosX, 2) + Math.pow(deltaPosY, 2);
			double deltaVV = Math.pow(deltaVelX, 2) + Math.pow(deltaVelY, 2);
			double deltaVR = (deltaVelX*deltaPosX)  + (deltaVelY*deltaPosY);
			double d = Math.pow(deltaVR, 2) - ((deltaVV)*(deltaRR - Math.pow(this.getRadius()+other.getRadius(), 2)));
			
			
			if (deltaVR >= 0){
				return Double.POSITIVE_INFINITY;
			} else if (d <=0){
				return Double.POSITIVE_INFINITY;
			} else {
				return - ((deltaVR + Math.sqrt(d))/(deltaVV));
			}

		}
	}
	
	/**
	 * This method calculates when, if ever, an entity will collide with a barrier
	 * 
	 * Implement defensively.
	 * 
	 * @param world
	 *        The world within which the entity lies
	 * @return This function returns a number indicating the time to collision between an entity and the boundaries of a world,
	 * 		   such that: 
	 * 				* If the entity is on a collision course with a boundary a positive number is returned indicating the time to collision
	 * 				* If the entity is not on a collision course with a boundary (e.g. its velocity in both directions is zero) 
	 * 				  positive infinity is returned
	 * @throws NullPointerException
	 *         The other entity does not exist.
	 *         | other == null
	 */
	public double getTimeToCollision(World world) throws NullPointerException, IllegalArgumentException {
		
		double distanceToHorizontalWall = 0;
		double distanceToVerticalWall = 0;
		double verticalCollisionTime = 0;
		double horizontalCollisionTime = 0;
		
		if (!withinBoundaries(world)) {
			return 0.0;
		} 
		
		if (getVelocityX() > 0) {
			distanceToVerticalWall = world.getWidth()-(getPositionX()+getRadius());
		} else if (getVelocityX() < 0) {
			distanceToVerticalWall = getPositionX()-getRadius();
		} else if(getVelocityX() == 0){
			distanceToVerticalWall = Double.POSITIVE_INFINITY;
		}
		
		if (getVelocityY() >= 0) {
			distanceToHorizontalWall = world.getHeight()-(getPositionY()+getRadius());
		} else if (getVelocityY() < 0) {
			distanceToHorizontalWall = getPositionY()-getRadius();
		} else if (getVelocityY() == 0) {
			distanceToHorizontalWall = Double.POSITIVE_INFINITY;
		}
		
		verticalCollisionTime = Math.abs(distanceToVerticalWall)/Math.abs(getVelocityX());
		horizontalCollisionTime = Math.abs(distanceToHorizontalWall)/Math.abs(getVelocityY());
		
		return Math.min(verticalCollisionTime, horizontalCollisionTime);
	}
	
	/**
	 * Helper function to use the correct getCollisionPosition function
	 * 
	 * @param collidable
	 * @invar Collidable must be a valid ICollidable
	 * 		  | !(collidable == null) == true
	 * @post Returns the getCollisionPosition function associated with the given ICollidable
	 * 		 | if (ICollidable instanceof Entity)
	 * 		 | 		result == getCollisionPosition((Entity) collidable)
	 * 		 | else if (ICollidable instanceof World)
	 * 		 | 		result == getCollisionPosition((World) collidable)
	 * 		 | else 
	 * 		 | 		result == null
	 */
	@Override
	public double[] getCollisionPosition(ICollidable collidable) {
		if (collidable instanceof Entity) {return this.getCollisionPosition((Entity) collidable);}
		else if (collidable instanceof World) {return this.getCollisionPosition((World) collidable);}
		else {return null;}
	}
	
	/**
	 * Returns the position of the next boundary collision, if any
	 * 
	 * @param world
	 * @return ...
	 * 		| if (getTimeToCollision(world) != Double.POSITIVE_INFINITY )
	 * 		|		result == {this.getPositionX() + this.getTimeToCollision(world)*this.getVelocityX(), 
	 * 		|				   this.getPositionY() + this.getTimeToCollision(world)*this.getVelocityY()}
	 * 		| else
	 * 		|		result == null
	 * @throws NullPointerException
	 * 		| world == null
	 */
	public double[] getCollisionPosition(World world) throws NullPointerException {      
		if (world == null){
			throw new NullPointerException();
		} else {
			if (getTimeToCollision(world) != Double.POSITIVE_INFINITY ){
				double posX = this.getPositionX() + this.getTimeToCollision(world)*this.getVelocityX();
				double posY = this.getPositionY() + this.getTimeToCollision(world)*this.getVelocityY();
				double[] pos =  {posX, posY};
				return pos;	
			} else {
				return null;
			}
		}
	}
	
	/**
	 * This method calculates where, if ever, two entities will collide.
	 * 
	 * The Collision position is the current position plus the time to collision multiplied by its velocity.
	 * In math: x(t) = this.getPostionX() + this.getVelocityX() * this.getTimeToCollision(other)
	 *          y(t) = this.getPostionY() + this.getVelocityY() * this.getTimeToCollision(other)
	 * In case the entity gets an acceleration, we measure the new velocity after the thrust has stopped.
	 * We use this new velocity to measure the new time to collision and the new collision position. 
	 * 
	 * Implement defensively.
	 * 
	 * @param other
	 *        The second (other) entity. We use this entity to determine the position of collision.
	 * @return A position if and only if the other entity is not null, the time 
	 *         to collision is not equal to infinity and the two entities 
	 *         are not overlapping.
	 * @return null
	 * 		   | this.overlap(other)
	 * @throws NullPointerException
	 *         The other entity does not exist.
	 *         | other == null
	 */
	public double[] getCollisionPosition(Entity other) throws NullPointerException {      
		if (other == null){
			throw new NullPointerException();
		} else if (this.overlap(other)) {
			return null;
		} 
		else {
			
			if (getTimeToCollision(other) != Double.POSITIVE_INFINITY ){
				
				double thisposX = this.getPositionX() + this.getTimeToCollision(other)*this.getVelocityX();
				double thisposY = this.getPositionY() + this.getTimeToCollision(other)*this.getVelocityY();
				double otherposX = other.getPositionX() + other.getTimeToCollision(this)*other.getVelocityX();
				double otherposY = other.getPositionY() + other.getTimeToCollision(this)*other.getVelocityY();
				
				double posX =0;
				double posY=0;
				
				double angle = Math.atan2(Math.abs(otherposY-thisposY),Math.abs(otherposX-thisposX));
				
				if (thisposX<=otherposX && thisposY<=otherposY){
					posX = thisposX+this.getRadius()*Math.cos(angle);
					posY = thisposY+this.getRadius()*Math.sin(angle);
				} else if (thisposX>=otherposX && thisposY<=otherposY){
					posX = thisposX-this.getRadius()*Math.cos(angle);
					posY =thisposY+this.getRadius()*Math.sin(angle);
				} else if (thisposX>=otherposX && thisposY>=otherposY){
					posX= thisposX-this.getRadius()*Math.cos(angle);
					posY = thisposY-this.getRadius()*Math.sin(angle);
				} else if (thisposX<=otherposX && thisposY>=otherposY){
					posX = thisposX+this.getRadius()*Math.cos(angle);
					posY = thisposY-this.getRadius()*Math.sin(angle);
				}
				
				double[] pos = {posX,posY};
				return pos;
				
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Indicates whether this entity apparently collides with another entity
	 * 
	 * @define apparently collide
	 * 		| Two objects A and B apparently collide if the distance between the centres 
	 * 		| of A and B is between 99% and 101% of the sum of the objects’ radii σ_A and σ_B .
	 * 
	 * @param other
	 * @return ...
	 * 		 | if (apparent collision)
	 * 		 | 		result == true
	 * 		 | else 
	 * 		 | 		result == false
	 * 		
	 */
	public boolean apparentlyCollide(Entity other) {
		double radiiSum = this.getRadius() + other.getRadius();
		
		if (((radiiSum * 0.99) <= this.getDistanceBetween(other))
			&& (this.getDistanceBetween(other) <= (radiiSum * 1.01))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Indicates whether this entity significantly overlaps with another entity
	 * 
	 * @define significant overlap
	 * 		| Two objects A and B overlap significantly if the distance between the centres of A and B 
	 * 		| is ≤ 99% of the sum of the objects radii σ_A and σ_B .
	 * 
	 * @param other
	 * @return ...
	 * 		 | if (significant overlap)
	 * 		 | 		result == true
	 * 		 | else 
	 * 		 | 		result == false
	 * 		
	 */
	public boolean significantOverlap(Entity entity) {
		if (this.getDistanceBetween(entity) <= (0.99 * (this.getRadius() + entity.getRadius()))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates whether this entity lies within the boundaries of another entity
	 * 
	 * @define within boundaries
	 * 		| An object A apparently lies within the boundaries of a container C if the distance 
	 * 		| between each boundary of C and the centre of A is ≥ 99% of the radius of A.
	 * @param other
	 * @return ...
	 * 		 | if (within boundaries)
	 * 		 | 		result == true
	 * 		 | else 
	 * 		 | 		result == false
	 * 		
	 */
	public boolean withinBoundaries(Entity other) {
		// Check if this entity within other entity (user radius)
		if ((this.getDistanceBetween(other) <= Math.abs(this.getRadius() - other.getRadius()))) {
			return true;
		}
		return false;
	}

	/**
	 * Indicates whether this entity lies within the boundaries of a given world
	 * 
	 * @define within boundaries
	 * 		| An object A apparently lies within the boundaries of a container C if the distance 
	 * 		| between each boundary of C and the centre of A is ≥ 99% of the radius of A.
	 * @param world
	 * @return ...
	 * 		 | if (within boundaries)
	 * 		 | 		result == true
	 * 		 | else 
	 * 		 | 		result == false
	 * 		
	 */
	public boolean withinBoundaries(World world) {
		// Check if this entity within borders of world (upper, lower, left, right)
		if ((this.getPositionX() > (0.99*getRadius())) && (this.getPositionY() > (0.99*getRadius()))) {}
		else {return false;}
		
		if (((this.getPositionX() + (0.99*getRadius()) < world.getWidth()) && ((this.getPositionX() + (0.99*getRadius()) < world.getWidth())))) {}
		else {return false;}
		
		return true;
	}
	
	/**
	 * A function that moves the entity. Implemented only in subclasses.
	 * @param duration
	 */
	
	public void move(double duration){
		if (isValidDuration(duration)) {
			double deltaX = getVelocityX()*duration;
			double deltaY = getVelocityY()*duration;
			setPosition(getPositionX()+deltaX, getPositionY()+deltaY);
		} else {
			throw new IllegalArgumentException();
		}
	}
	

	/**
	 * A function that resolves a collision event between an entity and a boundary of a world
	 * 
	 * @param this
	 * @param world
	 * @post This function executes in such a manner that ensures that, at the end of the function:
	 *			* In the case that the entity is a ship or bullet, its velocity in the direction of the collision is reversed
	 *			* In the case that the entity is a bullet that has already collided with a boundary two times, the entity is
	 *			  removed from the world
	 */
	private void boundaryCollide(World world) {

		double distanceToLeftWall = this.getPositionX()-this.getRadius();
		double distanceToRightWall = world.getWidth() - this.getPositionX()-this.getRadius();
		double distanceToUpperWall = world.getHeight() - this.getPositionY()-this.getRadius();
		double distanceToBottomWall = this.getPositionY()-this.getRadius();
		
		double minDistance = Math.min(Math.min(distanceToUpperWall, distanceToBottomWall), Math.min(distanceToLeftWall, distanceToRightWall));
		if (minDistance == distanceToLeftWall || minDistance == distanceToRightWall) {
			this.setVelocity(-this.getVelocityX(), this.getVelocityY());
		} else if (minDistance == distanceToUpperWall || minDistance == distanceToBottomWall) {
			this.setVelocity(this.getVelocityX(), -this.getVelocityY());
		}
		
		if (this instanceof Bullet){
			if (((Bullet) this).Counter() == true){
				world.removeEntity(((Bullet) this));
			}
		}
	}
	
	/**
	 * A function that resolves a collision event between two entities
	 * 
	 * @param this
	 * @param other
	 * @post This function executes in such a manner that ensures that, at the end of the function:
	 * 			* In the case that both entities are Ships, both their velocities are changed according to the
	 * 			  formula as found in the task specification
	 * 			* In the case that one entity is a ship and another is a bullet:
	 * 					* If the bullet originates from the ship it is loaded by the ship
	 * 					* If the bullet does not originate from the ship both the ship and the bullet are removed
	 * 					  from the world
	 * 			* In the case that both entities are bullets, both bullets are removed form the world
	 */
	private void objectCollide(Entity other) {
		if ((this instanceof Ship && other instanceof Ship) || (this instanceof MinorPlanet && other instanceof MinorPlanet) ) {
			
			double deltaPosX = other.getPositionX()-this.getPositionX();
			double deltaPosY = other.getPositionY()-this.getPositionY();

			double deltaVelX = other.getVelocityX()-this.getVelocityX();
			double deltaVelY = other.getVelocityY()-this.getVelocityY();
			
			double deltaVR = (deltaVelX*deltaPosX)  + (deltaVelY*deltaPosY);
			
			double radiusSum = this.getRadius() + other.getRadius();
			double J = (2*this.getMass()*other.getMass()*deltaVR)/((this.getMass()+other.getMass())*radiusSum);
			
			double Jx = (J*deltaPosX)/(radiusSum);	
			double Jy = (J*deltaPosY)/(radiusSum);
			
			double newVelocityX1 = this.getVelocityX() + (Jx/this.getMass());
			double newVelocityY1 = this.getVelocityY() + (Jy/this.getMass());
			
			double newVelocityX2 = other.getVelocityX() - (Jx/other.getMass());
			double newVelocityY2 = other.getVelocityY() - (Jy/other.getMass());
			
			this.setVelocity(newVelocityX1, newVelocityY1);
			other.setVelocity(newVelocityX2, newVelocityY2);
			
		} else if ((this instanceof Entity && other instanceof Bullet)) {
			
			if (((Bullet) other).getSource() == this) {
				((Bullet) other).setCounter(0);
				other.removeFromWorld();
				other.setPosition(this.getPositionX(), this.getPositionY());
				((Ship) this).loadBullets((Bullet) other);
				world.removeEntity(other);
			} else {
				world.removeEntity(this);
				world.removeEntity(other);
				this.terminate();
				other.terminate();
		  } 
		} else if ((other instanceof Entity && this instanceof Bullet)) {
			
			if (((Bullet) this).getSource() == other) {
				((Bullet) this).setCounter(0);
				this.removeFromWorld();
				this.setPosition(other.getPositionX(), other.getPositionY());
				((Ship) other).loadBullets((Bullet) this);
				world.removeEntity(this);
			} else {
				world.removeEntity(other);
				world.removeEntity(this);
				this.terminate();
				other.terminate();
			}
		} else if (other instanceof Ship && this instanceof Asteroid) {
			world.removeEntity(other);
			
		} else if (this instanceof Ship && other instanceof Asteroid) {
			world.removeEntity(this);
			
		} else if (this instanceof Ship && other instanceof Planetoid) {
			double[] randomPosition = {(Math.random())*(world.getWidth()-this.getRadius()),(Math.random())*(world.getHeight()-this.getRadius())};
			this.setPosition(randomPosition[0], randomPosition[1]);
			if (world.significantOverlap(this))
				world.removeEntity(this);
		} else if (other instanceof Ship && this instanceof Planetoid) {
			double[] randomPosition = {(Math.random())*(world.getWidth()-other.getRadius()),(Math.random())*(world.getHeight()-other.getRadius())};
			other.setPosition(randomPosition[0], randomPosition[1]);
			if (world.significantOverlap(other))
				world.removeEntity(other);
			
		}
	}
	
	@Override 
	public void collide(ICollidable collidable){
		if (collidable instanceof Entity){this.objectCollide((Entity)collidable);}
		else if (collidable instanceof World){this.boundaryCollide((World)collidable);}
		
	}
	
	
	
}
