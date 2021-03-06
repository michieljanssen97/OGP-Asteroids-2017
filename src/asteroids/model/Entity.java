
package asteroids.model;

import be.kuleuven.cs.som.annotate.*;

/**
 * 
 * A class that defines a entity for the Asteroids game.
 *
 * @author Michiel Janssen & Jelle Pelgrims
 *
 */

public abstract class Entity implements ICollidable, IOwnable {

	protected double x;
	protected double y;
	
	protected double velocityX;
	protected double velocityY;
	
	protected double radius;
	protected double orientation;
	protected double mass;
	
	protected World world;
	
	protected boolean isTerminated = false;
	protected boolean isDestroyed = false;
	
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
		if (hasAsOwner(world)) {
			world.removeEntity(this);
		}
		this.isTerminated = true;
	}
	
	/**
	 * Marks this entity as destroyed
	 */
	public void destroy() {
		this.isDestroyed = true;
	}
	
	/**
	 * Returns a boolean indicating whether this entity is destroyed
	 */
	public boolean isDestroyed() {
		return this.isDestroyed;
	}
	
	/**
	 * Returns the maximum speed of a bullet
	 */
	public double getMaxSpeed() {return 300000;}

	/**
	 * Returns the minimum speed of a bullet
	 */
	public double getMinRadius() {return 0;}

	/**
	 * Returns the minimum density of a bullet
	 */
	public double getMinDensity() {return 0;}
	
	/**
	 * Returns the owner of this entity
	 */
	public Object getOwner() {
		return this.world;
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
		return (radius >= getMinRadius()) && (!Double.isNaN(radius));
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
		assert 0<= angle && angle<=2*Math.PI;
		this.orientation = angle;
	}
	
	/**
	 * Returns the entities mass
	 */
	@Basic @Immutable
	public double getMass() {
		return this.mass;
	}
	
	//TODO ?
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
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Returns a boolean indicating whether this entity is associated with a world
	 */
	public boolean isPartOfWorld() {
		return !(this.world==null);
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
	public void changeOwner(World world) {
		if (canHaveAsOwner(world)) {
			this.world = world;
		}
	}
	
	public void changeOwner(Object object) {};
	
	/**
	 * Removes any association with a world
	 * @see implementation
	 */
	public void disown() {
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
			return 0.0;
		} else if (other == null) {
			throw new NullPointerException();
		} else {
			double distance = Math.sqrt(Math.pow(other.getPositionX()-this.getPositionX(), 2)+Math.pow(other.getPositionY()-this.getPositionY(), 2));
			return distance;
		}
	}
	
	/**
	 * This method measures the distance between an entity and a world.
	 * 
	 * Implement defensively. 
	 * 
	 * @param  world
	 * 		   The world. We use this world to measure the distance between the world and the given entity.
	 * @invar world must be a valid world
	 * 		  | !(world = null) == true
	 * @return The distance between a world and an entity
	 * 		   |  result = Math.min(distanceToVerticalWall, distanceToHorizontalWall)
	 * @throws NullPointerException
	 *         The world does not exist.
	 *         | world == null
	 */
	public double getDistanceBetween(World world) throws NullPointerException {
		if (world == null) {
			throw new NullPointerException();
		} else {
			
			double distanceToHorizontalWall = 0;
			double distanceToVerticalWall = 0;
			
			if (getVelocityX() > 0) {
				distanceToVerticalWall = world.getWidth()-(getPositionX()+getRadius());
			} else if (getVelocityX() < 0) {
				distanceToVerticalWall = getPositionX()-getRadius();
			} else if(getVelocityX() == 0){
				distanceToVerticalWall = Double.POSITIVE_INFINITY;
			}
			
			if (getVelocityY() > 0) {
				distanceToHorizontalWall = world.getHeight()-(getPositionY()+getRadius());
			} else if (getVelocityY() < 0) {
				distanceToHorizontalWall = getPositionY()-getRadius();
			} else if (getVelocityY() == 0) {
				distanceToHorizontalWall = Double.POSITIVE_INFINITY;
			}
			
			return Math.min(distanceToVerticalWall, distanceToHorizontalWall);
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
		return collidable.getTimeToCollision(this);
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
			
			double deltaPosX = this.getPositionX()-other.getPositionX();
			double deltaPosY = this.getPositionY()-other.getPositionY();

			double deltaVelX = this.getVelocityX()-other.getVelocityX();
			double deltaVelY = this.getVelocityY()-other.getVelocityY();
			
			double deltaRR = Math.pow(deltaPosX, 2) + Math.pow(deltaPosY, 2);
			double deltaVV = Math.pow(deltaVelX, 2) + Math.pow(deltaVelY, 2);
			double deltaVR = (deltaVelX*deltaPosX)  + (deltaVelY*deltaPosY);
			double d = Math.pow(deltaVR, 2) - (deltaVV)*(deltaRR - Math.pow(this.getRadius()+other.getRadius(), 2));
			
			
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
		
		double verticalCollisionTime = 0;
		double horizontalCollisionTime = 0;
		
		if (world == null || !withinBoundaries(world)) {
			return Double.POSITIVE_INFINITY;
		} 
		
		double distanceToWall = getDistanceBetween(world);
		
		verticalCollisionTime = Math.abs(distanceToWall/getVelocityX());
		horizontalCollisionTime = Math.abs(distanceToWall/getVelocityY());

		return Math.min(verticalCollisionTime, horizontalCollisionTime);
	}
	
	/**
	 * Returns the position of a possible collision between this entity and the given ICollidableµ
	 */
	public double[] getCollisionPosition(ICollidable collidable) {
		return collidable.getCollisionPosition(this);
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
			return null;
		} else {
			if (getTimeToCollision(world) != Double.POSITIVE_INFINITY ){
				double posX = this.getPositionX() + this.getTimeToCollision(world)*this.getVelocityX();
				double posY = this.getPositionY() + this.getTimeToCollision(world)*this.getVelocityY();
	
				double distanceToWall = getDistanceBetween(world);
				boolean verticalCollision = Math.abs(distanceToWall/this.getVelocityX()) < Math.abs(distanceToWall/this.getVelocityY());
				
				if (verticalCollision) {
					if (getVelocityX() < 0) {
						posX -= this.getRadius();
					} else if (getVelocityX() > 0){
						posX += this.getRadius();
					}
				} else if (!verticalCollision){
					if (getVelocityY() < 0) {
						posY -= this.getRadius();
					} else if (getVelocityY() > 0) {
						posY += this.getRadius();
					}
				}

				
				double[] pos =  {posX, posY};
				return pos;	
			} else {
				return null;
			}
		}
	}
	
	/**
	 * This method calculates the position of a collision between two entities. 
	 * 
	 * @param other
	 *        The second (other) entity.
	 * @return This function returns an array containing two doubles indicating the position of a collision between two entities.
	 */
	public double[] calculateCollisionPosition(Entity other) {
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
		
		return new double[]{posX, posY};
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
		} else {
			if (getTimeToCollision(other) != Double.POSITIVE_INFINITY ){
				return calculateCollisionPosition(other);
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
		if ((this.getPositionX() > (0.99*getRadius())) && (this.getPositionY() > (0.99*getRadius())) 
				&& ((this.getPositionX() + (0.99*getRadius()) < world.getWidth())
						&& ((this.getPositionX() + (0.99*getRadius()) < world.getWidth())))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method moves the entity with a given valid duration.
	 * 
	 * Implemented defensively.
	 * 
	 * @param  duration
	 * @invar The given duration must be valid
	 * 		  | isValidDuration(duration) == true
	 * @post The new xPosition of this entity is the current xPosition plus the current xVelocity*duration.
	 *       | new.getPositionX() == this.getPositionX() + this.getVelocityX()*duration
	 * @post The new yPosition of this entity is the current yPosition plus the current yVelocity*duration.
	 *       | new.getPositionY() == this.getPositionY() + this.getVelocityY()*duration
	 * @throws IllegalArgumentException
	 * 		   The given duration was invalid
	 *         | ! isValidDuration(duration)
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
	 * Dispatch based on runtime information. 
	 * This will call the function collide((subtype of entity) world) on the given ICollidable.
	 */
	public void collide(ICollidable collidable) {
		collidable.collide(this);
	}
	
	/**
	 * Collide with the given World
	 */
	public void collide(World world) {
		world.defaultCollide(this);
	}
	
	/**
	 * A helper function that resolves a default collision event between two entities
	 * 
	 * @param entity
	 * @param this
	 * @post This function executes in such a manner that ensures that, at the end of the function:
	 * 			* The velocities of both entities are changed according to the
	 * 			  formula as found in the task specification and is used in subclasses as a default collision.
	 */
	public void defaultCollide(Entity entity) {
		double deltaPosX = this.getPositionX()-entity.getPositionX();
		double deltaPosY = this.getPositionY()-entity.getPositionY();

		double deltaVelX = this.getVelocityX()-entity.getVelocityX();
		double deltaVelY = this.getVelocityY()-entity.getVelocityY();
		
		double deltaVR = (deltaVelX*deltaPosX)  + (deltaVelY*deltaPosY);
		
		double radiusSum = entity.getRadius() + this.getRadius();
		double J = (2*entity.getMass()*this.getMass()*deltaVR)/((entity.getMass()+this.getMass())*radiusSum);
		
		double Jx = (J*deltaPosX)/(radiusSum);	
		double Jy = (J*deltaPosY)/(radiusSum);
		
		double newVelocityX1 = entity.getVelocityX() + (Jx/entity.getMass());
		double newVelocityY1 = entity.getVelocityY() + (Jy/entity.getMass());
		
		double newVelocityX2 = this.getVelocityX() - (Jx/this.getMass());
		double newVelocityY2 = this.getVelocityY() - (Jy/this.getMass());
		
		entity.setVelocity(newVelocityX1, newVelocityY1);
		this.setVelocity(newVelocityX2, newVelocityY2);
	}
}
