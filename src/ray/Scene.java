package ray;

import java.util.ArrayList;
import java.util.List;

import ray.math.Color;
import ray.camera.Camera;
import ray.light.Light;
import ray.shader.Shader;
import ray.surface.Surface;

/**
 * The scene is just a collection of objects that compose a scene. The camera,
 * the background color, the list of lights, the list of surfaces, the list of
 * shaders, and the output image are all stored here.
 *
 * @author ags, pramook
 */
public class Scene {
	
	/** The camera for this scene. */
	protected Camera camera;
	public void setCamera(Camera camera) { this.camera = camera; }
	public Camera getCamera() { return this.camera; }
	
	/** The background color for this scene. Any rays that don't hit a surface
	 *  return this color.
	 */
	protected Color backColor = new Color(0, 0, 0);
	public void setBackColor(Color color) { this.backColor = color; }
	public Color getBackColor() { return this.backColor; }
	
	/** The list of lights for the scene. */
	protected ArrayList<Light> lights = new ArrayList<Light>();
	public void addLight(Light toAdd) { lights.add(toAdd); }
	public List<Light> getLights() { return this.lights; }
	
	/** The list of surfaces for the scene. */
	protected ArrayList<Surface> surfaces = new ArrayList<Surface>();
	public void addSurface(Surface toAdd) { surfaces.add(toAdd); }
	public List<Surface> getSurfaces() { return this.surfaces; }
	
	/** The list of shaders in the scene. */
	protected ArrayList<Shader> shaders = new ArrayList<Shader>();
	public void addShader(Shader toAdd) { shaders.add(toAdd); }
	
	/** Image to be produced by the renderer **/
	protected Image outputImage;
	public Image getImage() { return this.outputImage; }
	public void setImage(Image outputImage) { this.outputImage = outputImage; }
	
	/**
	 * Set outRecord to the first intersection of ray with the scene. Return true
	 * if there was an intersection and false otherwise. If no intersection was
	 * found outRecord is unchanged.
	 *
	 * @param outRecord the output IntersectionRecord
	 * @param ray the ray to intersect
	 * @return true if an intersection is found.
	 */
	public boolean getFirstIntersection(IntersectionRecord outRecord, Ray rayIn) {
		// TODO: 1) Loop through all surfaces in the scene.
		//		 2) Intersect each with a copy of the given ray.
		//		 3) If there was an intersection, check the modified IntersectionRecord to see
		//			if the object was hit by the ray sooner than any previous object.
		//			Hint: modifying the end field of your local copy of ray might be useful here.
		//		 4) Set outRecord to the IntersectionRecord of the first object hit.
		//		 5) If there was an intersection, return true; otherwise return false.
		
		return false;
	}
	
	/**
	 * Shadow ray calculations can be considerably accelerated by not bothering to find the
	 * first intersection.  This record returns any intersection of the ray and the surfaces
	 * and returns true if one is found.
	 * @param ray the ray to intersect
	 * @return true if any intersection is found
	 */
	public boolean getAnyIntersection(Ray ray) {
		// TODO: Fill in this function. It should be much simpler than getFirstIntersection().

		return false;
	}
}