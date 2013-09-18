
package ray.surface;

import java.util.Arrays;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a cylinder as a center, a radius and a height
 *
 * @author Yingchuan Hu   yh537@cornell.edu
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
	// Get vector op and direction d
    Vector3 op = new Vector3();
    op.sub(rayIn.origin, center);
    Vector3 d = rayIn.direction;    
    
    // r(t) = p + td && |x|^2+|y|^2 = radius^2 && -height/2 < z < height/2
    // Assume that the length of the cylinder is infinite
    // so that we can treat it similar to sphere in the xy plane
    // limitation on z would be added later
    double dd = d.x * d.x + d.y * d.y;
    double dop = d.x * op.x + d.y * op.y;
    double opop = op.x * op.x + op.y * op.y;
    double delta = dop * dop - dd * (opop - radius * radius);
    // Point out of cylinder
    if (delta < 0)
    	return false;
    
    // Intersect with side plane
    double t_side = Math.min((-dop + Math.sqrt(delta)) / dd, (-dop - Math.sqrt(delta)) / dd);
    // Intersect with top cap, Infinity if d.z == 0
    double t_top = (height / 2.0 - op.z) / d.z;
    // Intersect with bottom cap, Infinity if d.z == 0
    double t_bot = (-height / 2.0 - op.z) / d.z;
    double[] tList = {t_side, t_top, t_bot};
    Arrays.sort(tList);
    Double t_min = Double.MAX_VALUE;
    // Loop from the minimum t to find the minimum t that satisfies all requirements
    for (double t : tList)
    {
    	// Calculate the possible intersection point by p + td
    	Vector3 tmpVec = new Vector3();
    	tmpVec.scaleAdd(t, d);
    	Point3 tmpLoc = new Point3();
    	tmpLoc.add(rayIn.origin, tmpVec);
    	// If intersect with side surface
    	if (t == t_side)
    	{
    		if (Math.abs(tmpLoc.z - center.z) < height / 2.0)
    		{
    			outRecord.normal.set(tmpLoc.x - center.x, tmpLoc.y - center.y, 0);
    			outRecord.normal.normalize();
    			t_min = t;
    			break;
    		}
    	}
    	else
    	{
    		// Must be inside circle if seen from the x-y plane
    		if (Math.pow(tmpLoc.x - center.x, 2) + Math.pow(tmpLoc.y - center.y, 2) - radius * radius <= 0)
    		{
    			// If intersect with top
    			if (t == t_top)
    				outRecord.normal.set(0, 0, 1);
    			// If intersect with bottom
    			else if (t == t_bot)
    				outRecord.normal.set(0, 0, -1);
    			t_min = t;
    			break;
    		}
    	}
    }
    // Check whether between ray start and end point or error occurs
    if (t_min == Double.MAX_VALUE || t_min < rayIn.start || t_min > rayIn.end)
    	return false;
    
    // Output
    outRecord.t = t_min;
    
    Vector3 scaleD = new Vector3();
	scaleD.scaleAdd(t_min, d);
	outRecord.location.add(rayIn.origin, scaleD);
    
	outRecord.surface = this;
    rayIn.end = t_min;
    return true;
  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "cylinder " + center + " " + radius + " " + height + " " + shader + " end";
  }

}