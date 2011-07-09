package no.ivark.soccerdemo.view;

import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import no.ivark.soccerdemo.vectormath.Vector3D;

public class MovingObjectDrawer implements Drawable {
	private Set<DrawableMovingObject> objects=new HashSet<DrawableMovingObject>();

	public void register(DrawableMovingObject object) {
		this.objects.add(object);
	}
	
	
	public void draw(ViewFrustum frustum, GL2 gl, GLU glu) {
        float[][] clipPlanes={frustum.nearClip,frustum.farClip,frustum.upClip,frustum.bottomClip,frustum.leftClip,frustum.rightClip};        
        int drawn=0;
        for (DrawableMovingObject o:objects) {
            Vector3D pos=o.getPos();
            boolean outside=false;
            for (float[] cp:clipPlanes) {
                float dist=cp[0]*pos.getX()+cp[1]*pos.getY()+cp[2]*pos.getZ()+cp[3];
                if (dist+o.getRadius()*3.0<0) {
                    outside=true;
                    break;
                }
            }
            if (!outside) {
                drawn++;
                o.draw(frustum, gl, glu);
            }
        }		
	}

	public void init(ViewFrustum frustum, GL2 gl, GLU glu) throws Exception {
		for (DrawableMovingObject o:objects) {
			o.init(frustum, gl, glu);
		}
		
	}

}
