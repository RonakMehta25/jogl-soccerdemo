package no.ivark.soccerdemo.view;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;


public interface Drawable {
	public void init(ViewFrustum frustum, GL2 gl, GLU glu) throws Exception;
	public void draw(ViewFrustum frustum, GL2 gl, GLU glu);
}
