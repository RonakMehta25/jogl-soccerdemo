package no.ivark.soccerdemo.view;

import java.awt.Color;
import java.io.InputStream;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import no.ivark.soccerdemo.movement.MovingObject;
import no.ivark.soccerdemo.vectormath.Vector3D;


public abstract class Player implements DrawableMovingObject {
    static private final float RUNSPEED=0.3f;
    static private final Random R=new Random();
    static private int face=-1;
    
    private Vector3D pos;
    private Vector3D velocity=new Vector3D(R.nextFloat()*0.0001f,R.nextFloat()*0.0001f,R.nextFloat()*0.0001f);
    private Vector3D target;
    protected float walkPhase;
    
    private float femurAngle1=0f;
    private float femurAngle2=0f;
    private float lowLegAngle1=0f;
    private float lowLegAngle2=0f;
    private float shoeAngle1=0f;
    private float shoeAngle2=0f;
    
    
    private Color color;
    
    private Rod hipRod=new Rod(new Vector3D(-0.2f, 0f, 0f),new Vector3D(0.2f, 0f, 0f),8,.2f);
    private Rod femur=new Rod(new Vector3D(0f, 0f, 0f),new Vector3D(0f, 0f, -0.7f),8,0.1f);
    private Rod lowLeg=new Rod(new Vector3D(0f, 0f, 0f),new Vector3D(0f, 0f, -0.5f),8,0.05f);
    private Rod shoe=new Rod(new Vector3D(0f, 0.05f, 0f),new Vector3D(0f, -0.2f, 0f),8,0.07f);
    private Rod torso=new Rod(new Vector3D(0f, 0f, 0f),new Vector3D(0f, 0f, 0.35f),10,0.3f);
    private Rod axelRod=new Rod(new Vector3D(-0.3f, 0f, 0f),new Vector3D(0.3f, 0f, 0f),8,0.15f);
    private Rod upArm=new Rod(new Vector3D(0f, 0f, 0f),new Vector3D(0f, 0f, -0.4f),8,0.07f);
    private Rod lowArm=new Rod(new Vector3D(0f, 0f, 0f),new Vector3D(0f, 0f, -0.4f),8,0.05f);
    private Rod head=new Rod(new Vector3D(0f, 0f, 0f),new Vector3D(0f, 0f, 0.18f),10,0.15f);
    private Rod hair=new Rod(new Vector3D(0f, 0f, 0f),new Vector3D(0f, 0f, 0.01f),10,0.18f);
    private Rod nose=new Rod(new Vector3D(0f, -0.16f, 0f),new Vector3D(0f, -0.22f, 0.0f),8,0.02f);
    
    public Player(Vector3D pos,Color color) {
        this.pos=pos;
        this.target=pos;
        this.color=color;
        walkPhase=R.nextFloat()*2.0f*(float)Math.PI;
    }
    
    public void draw(ViewFrustum frustum, GL2 gl, GLU glu) {
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPushMatrix();                     
        gl.glColor3d(color.getRed()/256.0,color.getGreen()/256.0,color.getBlue()/256.0);
        
        // Calculate angle to target
        float dx=target.getX()-pos.getX();
        float dy=target.getY()-pos.getY();
        float angle=(float)(Math.atan2(dy,dx)/Math.PI*180.0+90);  
        
        // Hip
        gl.glColor3d(color.getRed()/256.0,color.getGreen()/256.0,color.getBlue()/256.0);
        gl.glTranslated(pos.getX(),pos.getY(),1.3+0.03*Math.sin(walkPhase*2));
        gl.glRotated(angle,0,0,1);
        hipRod.draw(frustum,gl,glu);
        
        // Leg
        gl.glColor3d(0.9f,0.8f,0.4f);
        gl.glPushMatrix();
        gl.glTranslated(-0.15,0,0);
        gl.glRotated(femurAngle1, 1,0,0);
        femur.draw(frustum, gl, glu);
        gl.glTranslated(0,0,-0.7);
        gl.glRotated(lowLegAngle1, 1,0,0);
        lowLeg.draw(frustum, gl, glu);
        gl.glTranslated(0,0,-0.5);
        gl.glRotated(shoeAngle1, 1, 0, 0);
        gl.glColor3d(0.1f,0.1f,0.5f);
        shoe.draw(frustum, gl, glu);
        gl.glPopMatrix();
        
        // Leg
        gl.glColor3d(0.9f,0.8f,0.4f);
        gl.glPushMatrix();
        gl.glTranslated(0.15,0,0);
        gl.glRotated(femurAngle2, 1,0,0);
        femur.draw(frustum, gl, glu);
        gl.glTranslated(0,0,-0.7);
        gl.glRotated(lowLegAngle2, 1,0,0);
        lowLeg.draw(frustum, gl, glu);
        gl.glTranslated(0,0,-0.5);
        gl.glRotated(shoeAngle2, 1, 0, 0);
        gl.glColor3d(0.1f,0.1f,0.5f);
        shoe.draw(frustum, gl, glu);
        gl.glPopMatrix();
               
        
        gl.glPushMatrix();
        
        // Torso
        gl.glColor3d(color.getRed()/256.0,color.getGreen()/256.0,color.getBlue()/256.0);
        //torso1.draw(frustum, gl, glu);
        gl.glTranslated(0,0,0.25);
        gl.glColor3d(0f,0f,0f);
        torso.draw(frustum, gl, glu);
        gl.glTranslated(0,0,0.25);
          
        // Axel
        axelRod.draw(frustum, gl, glu);

        // Arm
        gl.glColor3d(0.9f,0.8f,0.4f);
        gl.glPushMatrix();
        gl.glTranslated(-0.35,0,0);
        gl.glRotated(45*Math.sin(-walkPhase), 1,0,0);
        gl.glRotated(15, 0, 1, 0);
        upArm.draw(frustum, gl, glu);
        gl.glTranslated(0,0,-0.4);
        gl.glRotated(-80, 1,0,0);
        lowArm.draw(frustum, gl, glu);
        gl.glPopMatrix();
        
        // Arm
        gl.glColor3d(0.9f,0.8f,0.4f);
        gl.glPushMatrix();
        gl.glTranslated(0.35,0,0);
        gl.glRotated(45*Math.sin(walkPhase), 1,0,0);
        gl.glRotated(-15, 0, 1, 0);
        upArm.draw(frustum, gl, glu);
        gl.glTranslated(0,0,-0.4);
        gl.glRotated(-80, 1,0,0);
        lowArm.draw(frustum, gl, glu);
        gl.glPopMatrix();
        
        gl.glColor3d(0.9f,0.8f,0.4f);
        gl.glPushMatrix();
        gl.glTranslated(0,0,0.25f);
        head.draw(frustum, gl, glu);
        
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 0.9f);
        gl.glBindTexture(GL.GL_TEXTURE_2D,face);
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        for (int i=-180;i<200;i+=20) {
            float x=0.16f*(float)Math.sin(i/180f*Math.PI);
            float y=-0.16f*(float)Math.cos(i/180f*Math.PI);
            gl.glTexCoord2d((i+180f)/360f,0.99f);
            gl.glVertex3d(x, y, 0.2);
            gl.glTexCoord2d((i+180f)/360f,0f);
            gl.glVertex3d(x, y, 0);
        }
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
               
        gl.glTranslated(0,0,0.21f);
        gl.glColor3d(0.0f,0.0f,0.0f);
        hair.draw(frustum, gl, glu);
        gl.glTranslated(0,0,-0.1f);
        gl.glColor3d(0.9f,0.2f,0.2f);
        nose.draw(frustum, gl, glu);
        gl.glPopMatrix();        
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    public Vector3D getPos() {
        return pos;
    }

    public Vector3D getTarget() {
        return target;
    }
    
    public void setPos(Vector3D pos) {
        this.pos=pos;
    }
    
    public void setTarget(Vector3D target) {
        this.target=target;
    }
    
    public void init(ViewFrustum frustum, GL2 gl, GLU glu) throws Exception {
        if (face==-1) {
            InputStream imgInput=SoccerField.class.getResourceAsStream("/face.png");
            TextureLoader.Texture texture=TextureLoader.readTexture(imgInput);
            face=texture.toGL(gl,glu,false);
        }
    }

    public float getRadius() {
        return 0.5f;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void move() {
    	Vector3D remainingRun=Vector3D.subtract(target, pos);
    	if (remainingRun.getLength()>getRadius()) {
    		pos=Vector3D.add(pos, velocity);
	    	velocity=remainingRun.getNormalized().multiply(RUNSPEED);
            walkPhase+=RUNSPEED*2;
            
            // Update angles
            femurAngle1=45.0f*(float)Math.sin(walkPhase);
            femurAngle2=45.0f*(float)Math.sin(walkPhase+Math.PI);
            lowLegAngle1=45.0f-50.0f*(float)Math.sin(1+walkPhase);
            lowLegAngle2=45.0f-50.0f*(float)Math.sin(1+walkPhase+Math.PI);
            shoeAngle1=30.0f+30.0f*(float)Math.sin(walkPhase-0.5);
            shoeAngle2=30.0f+30.0f*(float)Math.sin(-walkPhase-0.5);
    	} else {
            femurAngle1=0f;
            femurAngle2=0f;
            lowLegAngle1=0f;
            lowLegAngle2=0f;
            shoeAngle1=0f;
            shoeAngle2=0f;
            onTarget();
    	}
    }

    public void onCollision(MovingObject otherObject) {
        if (otherObject instanceof Player) {
            Vector3D reflect=Vector3D.subtract(pos, otherObject.getPos()).getNormalized();
            velocity=Vector3D.add(velocity, reflect).getNormalized().multiply(RUNSPEED);
        } else if (otherObject instanceof Football) {
        	Vector3D pushVector=Vector3D.subtract(otherObject.getPos(),getPos()).getNormalized();
        	((Football)otherObject).push(pushVector);
        }
    }    
    
    public abstract void onTarget();
}
