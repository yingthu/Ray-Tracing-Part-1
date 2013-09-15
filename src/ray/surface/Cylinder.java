
package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a cylinder as a center, a radius and a height
 *
 * @author Yingchuan Hu
 */
public class Cylinder extends Surface {
  
  /** The center of the cylinder. */
  protected final Point3 center = new Point3();
  public void setCenter(Point3 center) { this.center.set(center); }
  
  /** The radius of the cylinder. */
  protected double radius = 1.0;
  public void setRadius(double radius) { this.radius = radius; }
  
  /** The height of the cylinder. */
  protected double height = 1.0;
  public void setHeight(double height) { this.height = height; }
  
  public Cylinder() { }
  
  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param ray the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
    // TODO: fill in this function.
	// Get point p and direction d
    Point3 p = new Point3();
    p.set(rayIn.origin);
    Vector3 d = new Vector3();
    d.set(rayIn.direction);
    
    // r(t) = p + td && |x|^2+|y|^2 = radius^2 && -height/2 < z < height/2
    // Assume that the length of the cylinder is infinite
    // so that we can treat it similar to sphere in the xy plane
    // limitation on z would be added later
    double dd = d.x * d.x + d.y * d.y;
    double pp = p.x * p.x + p.y * p.y;
    double dp = d.x * p.x + d.y * p.y;
    double delta = dp * dp - dd * (pp - radius * radius);
    
    // Point out of cylinder
    if (delta < 0 )
    	return false;
    
    // Intersect with side plane
    double t_side = Math.min(-dp + Math.sqrt(delta), -dp - Math.sqrt(delta));
    // Intersect with top cap, Infinity if p.z == 0
    double t_top = (height / 2.0 - p.z) / p.z;
    // Intersect with bottom cap, Infinity if p.z == 0
    double t_bot = (-height / 2.0 - p.z) / p.z;
    
    // We should find the minimum t satisfying our requirements
    double t_min = Math.min(Math.min(t_side, t_top), t_bot);
    double tmpMinT = Double.MAX_VALUE;
    IntersectionRecord tmpRecord = new IntersectionRecord();
    if (t_min == t_side)
    {
        // p + td
        rayIn.evaluate(tmpRecord.location, t_side);
        // Limitation added
        if (Math.abs(tmpRecord.location.z - center.z) < height / 2.0)
        {
        	outRecord.normal.set(tmpRecord.location.x - center.x, tmpRecord.location.y - center.y, 0);
        	outRecord.normal.normalize();
        	tmpMinT = t_min;
        }
    }
    // Top or bottom cap
    else
    {
    	if (Math.pow(tmpRecord.location.x - center.x, 2) + Math.pow(tmpRecord.location.y - center.y, 2) <= radius * radius)
    	{
    		if (t_min == t_top)
    			outRecord.normal.set(0,0,1);
    		else if (t_min == t_bot)
    			outRecord.normal.set(0,0,-1);
    		tmpMinT = t_min;
    	}
    }
    // Check whether between ray start and end point or error occurs
    if (tmpMinT == Double.MAX_VALUE || tmpMinT < rayIn.start || tmpMinT > rayIn.end)
    	return false;
    
    // Output
    outRecord.t = tmpMinT;
    Point3 loc = new Point3();
    rayIn.evaluate(loc, tmpMinT);
    outRecord.location.set(loc);
    outRecord.surface = this;
    
    return true;

  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "cylinder " + center + " " + radius + " " + height + " " + shader + " end";
  }

}