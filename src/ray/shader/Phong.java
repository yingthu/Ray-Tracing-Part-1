
package ray.shader;

import ray.IntersectionRecord;
import ray.Ray;
import ray.Scene;
import ray.Workspace;
import ray.light.Light;
import ray.math.Color;
import ray.math.Vector3;

/**
 * A Phong material has a diffuse component plus a specular highlight.
 *
 * @author ags, pramook
 */
public class Phong extends Shader {

	/** The color of the diffuse reflection. */
	protected final Color diffuseColor = new Color(1, 1, 1);
	public void setDiffuseColor(Color diffuseColor) { this.diffuseColor.set(diffuseColor); }

	/** The color of the specular reflection. */
	protected final Color specularColor = new Color(1, 1, 1);
	public void setSpecularColor(Color specularColor) { this.specularColor.set(specularColor); }

	/** The exponent controlling the sharpness of the specular reflection. */
	protected double exponent = 1.0;
	public void setExponent(double exponent) { this.exponent = exponent; }

	public Phong() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "phong " + diffuseColor + " " + specularColor + " " + exponent + " end";
	}

	/**
	 * Evaluate the intensity for a given intersection using the Phong shading model.
	 */
	@Override
	public void shade(Color outIntensity, Scene scene, Workspace workspace) {
		Vector3 incoming = workspace.incomingDirection;
		Vector3 outgoing = workspace.outgoingDirection;
		IntersectionRecord record = workspace.intersectionRecord;
		Ray shadowRay = workspace.shadowRay;
		Color color = workspace.color;
		// TODO: Fill in this function.
		// 1) Loop through each light in the scene.
		// 2) If the intersection point is shadowed, skip the calculation for the light.
		//	  See Shader.java for a useful shadowing function.
		// 3) Compute the incoming direction by subtracting
		//    the intersection point from the light's position.
		// 4) Compute the color of the point using the Phong shading model. Add this value
		//    to the output.
		// Reset
		outIntensity.set(0, 0, 0);
		
		// Save unit vector in half vector direction
		Vector3 h = new Vector3();
		
		// Loop through lights
		for (Light light : scene.getLights())
		{
			// If intersection point not shadowed
			//if (!isShadowed(scene, light, record, shadowRay))
			//{
			//	System.out.println("Got inside!");
				incoming.sub(light.position, record.location);
				incoming.normalize();
				//outgoing.normalize();
				
				h.add(incoming, outgoing);
				h.normalize();
				
				// Compute values for diffuse part and specular part
				double valD = Math.max(record.normal.dot(incoming), 0);
				double valS = Math.pow(Math.max(record.normal.dot(h), 0), exponent);
				
				// Compute using Phong model and add contribution
				outIntensity.r += (diffuseColor.r * valD + specularColor.r * valS) * light.intensity.r;
				outIntensity.g += (diffuseColor.g * valD + specularColor.g * valS) * light.intensity.g;
				outIntensity.b += (diffuseColor.b * valD + specularColor.b * valS) * light.intensity.b;
			//}
		}
		//outIntensity.set(color);
		// Keep in range
		outIntensity.clamp(0, 1);
	}

}