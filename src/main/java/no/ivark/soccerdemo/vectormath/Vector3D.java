/*
 * Created on 31.jan.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package no.ivark.soccerdemo.vectormath;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * @author ivark
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Vector3D {
    private float x;
    private float y;
    private float z;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    static public float getDistance(Vector3D a, Vector3D b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        float dz = b.z - a.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    static public Vector3D subtract(Vector3D b, Vector3D a) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        float dz = b.z - a.z;
        return new Vector3D(dx,dy,dz);
    }

    static public Vector3D add(Vector3D a, Vector3D b) {
        return new Vector3D(a.getX()+b.getX(),a.getY()+b.getY(),a.getZ()+b.getZ());
    }

    static public float dotProduct(Vector3D a, Vector3D b) {
        return a.getX()*b.getX()+a.getY()*b.getY()+a.getZ()*b.getZ();
    }
    
    public Vector3D multiply(float scalar) {
        return new Vector3D(x*scalar,y*scalar,z*scalar);
    }

    static public Vector3D average(Collection<Vector3D> vectors) {
        float x=0;
        float y=0;
        float z=0;
        for (Vector3D v:vectors) {
            x+=v.x;
            y+=v.y;
            z+=v.z;
        }
        float s=vectors.size();
        return new Vector3D(x/s,y/s,z/s);
    }
    
    
    public Vector3D getNormalized() {
        float l = (float) Math.sqrt(x * x + y * y + z * z);
        return new Vector3D(x / l, y / l, z / l);
    }

    public Vector3D getNegative() {
        return new Vector3D(-x, -y, -z);
    }

    static public Vector3D getNormal(Vector3D a, Vector3D b) {
        return new Vector3D(a.y * b.z - a.z * b.y, b.x * a.z - b.z
                * a.x, a.x * b.y - a.y * b.x);
    }

    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }
}
