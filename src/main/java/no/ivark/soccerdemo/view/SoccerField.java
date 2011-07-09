package no.ivark.soccerdemo.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import no.ivark.soccerdemo.vectormath.Vector3D;

public class SoccerField implements Drawable {
	private int grassTexture;
    private int sandTexture;
    private int martinTexture;
    private int astridTexture;
    
	List<Rod> rods=new ArrayList<Rod>();

	public SoccerField() {
	}

	public void init(ViewFrustum frustum, GL2 gl,GLU glu) throws Exception {
        {
            InputStream imgInput=SoccerField.class.getResourceAsStream("/soccer.png");
            TextureLoader.Texture texture=TextureLoader.readTexture(imgInput);
            grassTexture=texture.toGL(gl,glu,false);
        }
        {
            InputStream imgInput=SoccerField.class.getResourceAsStream("/sand.jpg");
            TextureLoader.Texture texture=TextureLoader.readTexture(imgInput);
            sandTexture=texture.toGL(gl,glu,false);
        }
        {
            InputStream imgInput=SoccerField.class.getResourceAsStream("/martin.png");
            TextureLoader.Texture texture=TextureLoader.readTexture(imgInput);
            martinTexture=texture.toGL(gl,glu,false);
        }
        {
            InputStream imgInput=SoccerField.class.getResourceAsStream("/astrid.png");
            TextureLoader.Texture texture=TextureLoader.readTexture(imgInput);
            astridTexture=texture.toGL(gl,glu,false);
        }

		addGoalRods(1);
		addGoalRods(-1);

        rods.add(new Rod(new Vector3D(-10,50,0),new Vector3D(-10,50,10),3,0.2f));
        rods.add(new Rod(new Vector3D(10,50,0),new Vector3D(10,50,10),3,0.2f));
        rods.add(new Rod(new Vector3D(-10,-50,0),new Vector3D(-10,-50,10),3,0.2f));
        rods.add(new Rod(new Vector3D(10,-50,0),new Vector3D(10,-50,10),3,0.2f));
	}

	private void addGoalRods(int i) {
        float gw=5;
        rods.add(new Rod(new Vector3D(-59f*i,-gw,0f),new Vector3D(-59f*i,-gw,3f),10,0.15f));
        rods.add(new Rod(new Vector3D(-59f*i,gw,0f),new Vector3D(-59f*i,gw,3f),10,0.15f));
        rods.add(new Rod(new Vector3D(-59f*i,-gw,3f),new Vector3D(-59f*i,gw,3f),10,0.15f));

        rods.add(new Rod(new Vector3D(-59f*i,-gw,3f),new Vector3D(-61f*i,-gw,2.5f),6,0.1f));
        rods.add(new Rod(new Vector3D(-61f*i,gw,2.5f),new Vector3D(-59f*i,gw,3f),6,0.1f));

        rods.add(new Rod(new Vector3D(-61f*i,gw,2.5f),new Vector3D(-61f*i,-gw,2.5f),6,0.1f));

        rods.add(new Rod(new Vector3D(-61f*i,gw,2.5f),new Vector3D(-61.5f*i,gw,0f),6,0.1f));
        rods.add(new Rod(new Vector3D(-61f*i,-gw,2.5f),new Vector3D(-61.5f*i,-gw,0f),6,0.1f));

        rods.add(new Rod(new Vector3D(-61.5f*i,-gw,0f),new Vector3D(-61.5f*i,gw,0f),6,0.1f));

        rods.add(new Rod(new Vector3D(-61.5f*i,-gw,0f),new Vector3D(-59f*i,-gw,0f),6,0.1f));
        rods.add(new Rod(new Vector3D(-61.5f*i,gw,0f),new Vector3D(-59f*i,gw,0f),6,0.1f));
    }


	public void draw(ViewFrustum frustum, GL2 gl,GLU glu) {
        
		// Sand
		gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_LIGHT1);
		gl.glEnable(GL2.GL_CULL_FACE);
        gl.glNormal3f(0,0,1);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.9f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D,sandTexture);
		gl.glColor3f(0.8f,0.8f,0.1f);
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        gl.glTexCoord2d(0d, 120d);
        gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(-700f,550f,-0.1f);
        gl.glTexCoord2d(0d, 0d);
        gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(-700f,-550f,-0.1f);
        gl.glTexCoord2d(120d, 120d);
        gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(700f,550f,-0.1f);
        gl.glTexCoord2d(120d, 0d);
        gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(700f,-550f,-0.1f);
		gl.glEnd();

		// Field
		gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.9f);
		gl.glBindTexture(GL2.GL_TEXTURE_2D,grassTexture);
		gl.glColor3f(0.8f,0.8f,0.8f);
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
		gl.glTexCoord2d(0d, 1d);
		gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(-60f,45f,0f);
		gl.glTexCoord2d(0d, 0d);
		gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(-60f,-45f,0f);
		gl.glTexCoord2d(1d, 1d);
		gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(60f,45f,0f);
		gl.glTexCoord2d(1d, 0d);
		gl.glNormal3f(0f,0f,1f);
		gl.glVertex3f(60f,-45f,0f);
		gl.glEnd();

		// Fence
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_CULL_FACE);
		gl.glColor3f(0.1f,0.1f,1f);
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
		gl.glVertex3f(-70f,55f,2f);
		gl.glVertex3f(-70f,55f,-0.1f);
		gl.glVertex3f(70f,55f,2f);
		gl.glVertex3f(70f,55f,-0.1f);
		gl.glVertex3f(70f,-55f,2f);
		gl.glVertex3f(70f,-55f,-0.1f);
		gl.glVertex3f(-70f,-55f,2f);
		gl.glVertex3f(-70f,-55f,-0.1f);
		gl.glVertex3f(-70f,55f,2f);
		gl.glVertex3f(-70f,55f,-0.1f);
		gl.glEnd(); 
        
        // Sign
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.9f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D,martinTexture);
        gl.glColor3f(0.8f,0.8f,0.8f);
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        gl.glTexCoord2d(0d, 0d);
        gl.glNormal3f(0f,-1f,0f);
        gl.glVertex3f(-12f,49f,10f);
        gl.glTexCoord2d(0d, 1d);
        gl.glNormal3f(0f,-1f,0f);
        gl.glVertex3f(-12f,49f,30f);
        gl.glTexCoord2d(1d, 0d);
        gl.glNormal3f(0f,-1f,0f);
        gl.glVertex3f(12f,49f,10f);
        gl.glTexCoord2d(1d, 1d);
        gl.glNormal3f(0f,-1f,0f);
        gl.glVertex3f(12f,49f,30f);
        gl.glEnd();

        // Sign2
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.9f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D,astridTexture);
        gl.glColor3f(0.8f,0.8f,0.8f);
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        gl.glTexCoord2d(1d, 0d);
        gl.glNormal3f(0f,1f,0f);
        gl.glVertex3f(12f,-49f,10f);
        gl.glTexCoord2d(1d, 1d);
        gl.glNormal3f(0f,1f,0f);
        gl.glVertex3f(12f,-49f,30f);
        gl.glTexCoord2d(0d, 0d);
        gl.glNormal3f(0f,1f,0f);
        gl.glVertex3f(-12f,-49f,10f);
        gl.glTexCoord2d(0d, 1d);
        gl.glNormal3f(0f,1f,0f);
        gl.glVertex3f(-12f,-49f,30f);
        gl.glEnd();
        
		gl.glColor3f(0.3f,0.3f,0.3f);
        gl.glDisable(GL2.GL_TEXTURE_2D);
		for (Rod rod:rods) {
			rod.draw(frustum,gl,glu);
		}
	}

}
