
package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

public class Box extends Surface {
  
  /* The corner of the box with the smallest x, y, and z components. */
  protected final Point3 minPt = new Point3();
  public void setMinPt(Point3 minPt) { this.minPt.set(minPt); }
  
  /* The corner of the box with the largest x, y, and z components. */
  protected final Point3 maxPt = new Point3();
  public void setMaxPt(Point3 maxPt) { this.maxPt.set(maxPt); }
  
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
    double tXenter, tXexit, tYenter, tYexit, tZenter, tZexit;
    
    double tXmin = (minPt.x - rayIn.origin.x) / rayIn.direction.x;
    double tXmax = (maxPt.x - rayIn.origin.x) / rayIn.direction.x;
    tXenter = Math.min(tXmin, tXmax);
    tXexit = Math.max(tXmin, tXmax);
    
    double tYmin = (minPt.y - rayIn.origin.y) / rayIn.direction.y;
    double tYmax = (maxPt.y - rayIn.origin.y) / rayIn.direction.y;
    tYenter = Math.min(tYmin, tYmax);
    tYexit = Math.max(tYmin, tYmax);
    
    double tZmin = (minPt.z - rayIn.origin.z) / rayIn.direction.z;
    double tZmax = (maxPt.z - rayIn.origin.z) / rayIn.direction.z;
    tZenter = Math.min(tZmin, tZmax);
    tZexit = Math.max(tZmin, tZmax);
    
    double tEnter = Math.max(Math.max(tXenter, tYenter), tZenter);
    double tExit = Math.min(Math.min(tXexit, tYexit), tZexit);
    
    if (tEnter > rayIn.end || tExit < rayIn.start || tEnter > tExit || tEnter == 0 || tExit == 0)
    	return false;
    
    rayIn.end = tEnter;
    outRecord.t = tEnter;
    outRecord.surface = this;
    
    // r(t) = p + td
    Vector3 tmpVec = new Vector3();
    tmpVec.scaleAdd(tEnter, rayIn.direction);
    outRecord.location.add(rayIn.origin, tmpVec);
    
    // Set normal for outRecord
    Point3 tmpPt = outRecord.location;
    // Consider float point rounding errors
    if (Math.abs(tmpPt.x - minPt.x) <= Ray.EPSILON)
    	outRecord.normal.set(-1, 0, 0);
    else if (Math.abs(tmpPt.x - maxPt.x) <= Ray.EPSILON)
    	outRecord.normal.set(1, 0, 0);
    else if (Math.abs(tmpPt.y - minPt.y) <= Ray.EPSILON)
    	outRecord.normal.set(0, -1, 0);
    else if (Math.abs(tmpPt.y - maxPt.y) <= Ray.EPSILON)
    	outRecord.normal.set(0, 1, 0);
    else if (Math.abs(tmpPt.z - minPt.z) <= Ray.EPSILON)
    	outRecord.normal.set(0, 0, -1);
    else if (Math.abs(tmpPt.z - maxPt.z) <= Ray.EPSILON)
    	outRecord.normal.set(0, 0, 1);
    
    return true;

  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Box ";
  }
}