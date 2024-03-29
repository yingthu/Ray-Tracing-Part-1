
package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;
import sun.net.www.content.text.plain;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
  
  /** The center of the sphere. */
  protected final Point3 center = new Point3();
  public void setCenter(Point3 center) { this.center.set(center); }
  
  /** The radius of the sphere. */
  protected double radius = 1.0;
  public void setRadius(double radius) { this.radius = radius; }
  
  public Sphere() { }
  
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
	Vector3 op = new Vector3();
	op.sub(rayIn.origin, center);
	Vector3 d = rayIn.direction;
	
	double a = d.dot(d);
	double b = 2 * d.dot(op);
	double c = op.dot(op) - radius * radius;
	double discriminant = b * b - 4 * a * c;
	// No intersection
	if (discriminant < 0) {
		return false;
	}
	// Value t for intersection point
	double t = (discriminant == 0 ? -b : -b - Math.sqrt(discriminant)) / (2 * a);
	if (t > rayIn.end || t < rayIn.start) 
	{
	  return false;
	}
	// Update outRecord and rayIn.end
	outRecord.t = t;
	outRecord.surface = this;
	Vector3 tmp = new Vector3();
	tmp.scaleAdd(outRecord.t, rayIn.direction);
	outRecord.location.add(rayIn.origin, tmp);
	outRecord.normal.sub(outRecord.location, center);
	outRecord.normal.normalize();
	rayIn.end = t;
	
	return true;

  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "sphere " + center + " " + radius + " " + shader + " end";
  }

}