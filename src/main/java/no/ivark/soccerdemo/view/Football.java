package no.ivark.soccerdemo.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import no.ivark.soccerdemo.movement.MovingObject;
import no.ivark.soccerdemo.vectormath.Matrix4x4;
import no.ivark.soccerdemo.vectormath.Vector3D;


public class Football implements DrawableMovingObject {
    static private final Vector3D UP=new Vector3D(0,0,1);
    static private final Random R=new Random();
    static private final Face[] blackFaces;
    static private final Face[] whiteFaces;
    static private int whiteTexture=-1;
    static private int blackTexture=-1;
    static private float[][] tcWhite;
    static private float[][] tcBlack;
    static private Integer dispList=null;
        
    private Vector3D pos=new Vector3D(0,0,10);
    private Vector3D velocity=new Vector3D(R.nextFloat()*0.0001f,R.nextFloat()*0.0001f,R.nextFloat()*0.0001f);
    private Matrix4x4 spinPos=new Matrix4x4(1,0,0,0,
                                            0,1,0,0,
                                            0,0,1,0,
                                            0,0,0,1);
    private Matrix4x4 spin=new Matrix4x4(1,0,0,0,
    		                             0,1,0,0,
    		                             0,0,1,0,
    		                             0,0,0,1);
    private boolean noSpin=true;
    
    static {
    	Set<Face> faces=getFaces();
    	List<Face> w=new ArrayList<Face>();
    	List<Face> b=new ArrayList<Face>();
    	
    	for (Face f:faces) {
    		if (f.vertices.size()==5) {
    			b.add(f);
    		} else {
    			w.add(f);
    		}
    	}
    	blackFaces=b.toArray(new Face[b.size()]);
    	whiteFaces=w.toArray(new Face[w.size()]);
    }
    
    
    
    
    public void move() {
        pos=Vector3D.add(pos, velocity.multiply(0.2f));
        if (pos.getZ() < 0.2 ) {
            pos=new Vector3D(pos.getX(),pos.getY(),0.2f+0.2f-pos.getZ());
            velocity=new Vector3D(velocity.getX(),velocity.getY(),-velocity.getZ());
            updateSpin();
        }

        velocity=velocity.multiply(0.99f);
        if (pos.getZ()>0.3f) {
            velocity=new Vector3D(velocity.getX(),velocity.getY(),velocity.getZ()-0.09f);
        } else {
            velocity=new Vector3D(velocity.getX()*0.95f,velocity.getY()*0.95f,velocity.getZ()*0.8f);
            updateSpin();
        }
        if (!noSpin) {
        	spinPos.leftMultiply(spin);
        }
    }

    public void updateSpin() {
        Vector3D xyVel=new Vector3D(velocity.getX(),velocity.getY(),0);
        float xySpeed=xyVel.getLength();
        if (xySpeed<0.001) {
            noSpin=true;
            return;
        }
        Vector3D spinVec=Vector3D.getNormal(UP,xyVel).getNormalized();

        float sin=(float)Math.sin(xySpeed);
	    float cos=(float)Math.cos(xySpeed);

        float a=spinVec.getX();
        float b=spinVec.getY();
        float c=spinVec.getZ();
        
        float ab=a*b;
        float ac=a*c;
        float bc=b*c;

        noSpin=false;
        spin.init(a*a*(1-cos)+cos,ab*(1-cos)-c*sin,ac*(1-cos)+b*sin,0.0f,
                  ab*(1-cos)+c*sin,b*b*(1-cos)+cos,bc*(1-cos)-a*sin,0.0f,
                  ac*(1-cos)-b*sin,bc*(1-cos)+a*sin,c*c*(1-cos)+cos,0.0f,
                  0.0f,0.0f,0.0f,1.0f);
    }

    public Football() {
        
    }

    public Vector3D getPos() {
        return pos;
    }

    public void setPos(Vector3D pos) {
        this.pos=pos;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void push(Vector3D push) {
        velocity=Vector3D.add(velocity, push);
    }
    
    public void kick(Vector3D direction, float yield) {
    	final float noise=0.5f;
    	direction=direction.getNormalized();
    	direction=Vector3D.add(direction, new Vector3D((R.nextFloat()-0.5f)*noise,(R.nextFloat()-0.5f)*noise,R.nextFloat()*noise));
    	velocity=direction.multiply(yield);
    }

    public void onCollision(MovingObject otherObject) {
        float touchDist=otherObject.getRadius()+getRadius();
        float realDist=Vector3D.subtract(otherObject.getPos(), pos).getLength();
        if (otherObject instanceof Player) {
            //velocity=Vector3D.subtract(pos,otherObject.getPos()).getNormalized().multiply((float)(touchDist/realDist));
        } else if (otherObject instanceof Football){
        	Football otherFootball=(Football)otherObject;
            Vector3D v=Vector3D.subtract(velocity, otherFootball.getVelocity()); // Relative velocity
            Vector3D d=Vector3D.subtract(otherFootball.getPos(), pos); // Distance-vector to other
            float t=timeSinceCrash(v, d, getRadius()+otherFootball.getRadius()); // Negative

            Vector3D crashPos=Vector3D.add(pos, velocity.multiply(t));
            Vector3D ocrashPos=Vector3D.add(otherFootball.getPos(), otherFootball.getVelocity().multiply(t));

            Vector3D reflect=Vector3D.subtract(crashPos, ocrashPos).getNormalized();
            float push=-Vector3D.dotProduct(v, reflect);
            reflect=reflect.multiply(push/2);
            push(reflect);
            ((Football)otherFootball).push(reflect.getNegative());
            setPos(Vector3D.add(crashPos, velocity.multiply(-t)));
            otherFootball.setPos(Vector3D.add(ocrashPos, otherFootball.getVelocity().multiply(-t)));
            ((Football)otherFootball).updateSpin();
        }
        updateSpin();
    }

    static public float timeSinceCrash(Vector3D v, Vector3D d, float r) {
        float vx=v.getX();
        float vy=v.getY();
        float vz=v.getZ();
        float dx=d.getX();
        float dy=d.getY();
        float dz=d.getZ();
        float a=vx*vx+vy*vy+vz*vz;
        float b=-2*(dx*vx+dy*vy+dz*vz);
        float c=dx*dx+dy*dy+dz*dz-r*r;

        float l1=-b/(2*a);
        float l2=(float)Math.sqrt(b*b-4*a*c)/(2*a);

        if (b*b-4*a*c <= 0) {
            System.out.println(""+v+":"+d+":"+r);
        }


        float t=l1+l2;
        if (t>0) t=l1-l2;
        return t;
    }

    static public void main(String[] args) {
        Vector3D pos=new Vector3D(3,4,1);
        Vector3D opos=new Vector3D(3,5,2);

        Vector3D velo=new Vector3D(7,-1,8);
        Vector3D ovelo=new Vector3D(6,3,-2);

        Vector3D v=Vector3D.subtract(velo,ovelo);
        Vector3D d=Vector3D.subtract(opos,pos);

        float t=timeSinceCrash(v,d,256f);
        System.out.println("time="+t);

        Vector3D cpos=Vector3D.add(pos, velo.multiply(t));
        Vector3D copos=Vector3D.add(opos, ovelo.multiply(t));

        System.out.println(Vector3D.subtract(cpos,copos).getLength());
    }

    public float getRadius() {
        return 0.25f;
    }

    public void draw(ViewFrustum frustum, GL2 gl, GLU glu) {
//        float a00=spinPos.val[0][0];
//        float a01=spinPos.val[0][1];
//        float a02=spinPos.val[0][2];
//        float a10=spinPos.val[1][0];
//        float a11=spinPos.val[1][1];
//        float a12=spinPos.val[1][2];
//        float a20=spinPos.val[2][0];
//        float a21=spinPos.val[2][1];
//        float a22=spinPos.val[2][2];
        
        // Determinant of rot-matrix
//        float det=a00*(a11*a22-a12*a21)+a10*(a02*a21-a01*a22)+a20*(a01*a12-a02*a11);
        
        // Adjoint of spinPos
//        float adj00=a11*a22-a12*a21;
//        float adj01=a02*a21-a01*a22;
//        float adj02=a01*a12-a02*a11;
//        float adj10=a12*a20-a10*a22;
//        float adj11=a00*a22-a02*a20;
//        float adj12=a02*a10-a00*a12;
//        float adj20=a10*a21-a11*a20;
//        float adj21=a01*a20-a00*a21;
//        float adj22=a00*a11-a01*a10;
        
//        // Multiply frustum-direction with adj
//        float dx=pos.getX()-frustum.pos.getX();
//        float dy=pos.getY()-frustum.pos.getY();
//        float dz=pos.getZ()-frustum.pos.getZ();
//        
//        // Rotated frustum-direction (not multiplied by determinant of rot-matrix)
//        float rdx=adj00*dx+adj10*dy+adj20*dz;
//        float rdy=adj01*dx+adj11*dy+adj21*dz;
//        float rdz=adj02*dx+adj12*dy+adj22*dz;
//                
        gl.glPushMatrix();
        gl.glTranslatef(pos.getX(), pos.getY(),pos.getZ());
        gl.glScalef(0.05f, 0.05f, 0.05f);
        gl.glMultMatrixf(spinPos.getAsFloatArray(),0);

        gl.glCallList(getDisplayList(gl));
        gl.glPopMatrix();
    }

	private synchronized int getDisplayList(GL2 gl) {
		if (dispList==null) {
			System.out.println("Generating displaylist");
			dispList=gl.glGenLists(1);
			gl.glNewList(dispList,GL2.GL_COMPILE);
			gl.glEnable(GL.GL_TEXTURE_2D);       
	        gl.glColor3f(0.8f,0.8f,0.8f);
	        gl.glBindTexture(GL.GL_TEXTURE_2D, whiteTexture);
	        for (Face f:whiteFaces) {
	//            float sp=f.center.getX()*rdx+f.center.getY()*rdy+f.center.getZ()*rdz;
	//            if (sp*det>0) f=f.opposite;
	            
	            gl.glBegin(GL.GL_TRIANGLE_FAN);
	            gl.glNormal3f(f.center.getX(), f.center.getY(),f.center.getZ());
	            gl.glTexCoord2d(0.5f,0.5f);
	            gl.glVertex3f(f.center.getX(), f.center.getY(),f.center.getZ());
	            int i=0;
	            for (Vertex v:f.vertices) {
	            	float x=v.pos.getX();
	            	float y=v.pos.getY();
	            	float z=v.pos.getZ();
	                gl.glNormal3f(x,y,z);
	                gl.glTexCoord2d(tcWhite[i][0],tcWhite[i][1]);
	                gl.glVertex3f(x,y,z);
	                i++;
	            }
	            Vertex v=f.vertices.get(0);
	            gl.glNormal3f(v.pos.getX(), v.pos.getY(),v.pos.getZ());
	            gl.glTexCoord2d(tcWhite[0][0],tcWhite[0][1]);
	            gl.glVertex3f(v.pos.getX(), v.pos.getY(),v.pos.getZ());
	            gl.glEnd();
	        }
	        
	        gl.glColor3f(0.2f,0.2f,0.2f);
	        gl.glBindTexture(GL.GL_TEXTURE_2D, blackTexture);
	        for (Face f:blackFaces) {
	//            float sp=f.center.getX()*rdx+f.center.getY()*rdy+f.center.getZ()*rdz;
	//            if (sp*det>0) f=f.opposite;
	            
	            gl.glBegin(GL.GL_TRIANGLE_FAN);
	            gl.glNormal3f(f.center.getX(), f.center.getY(),f.center.getZ());
	            gl.glTexCoord2d(0.5f,0.5f);
	            gl.glVertex3f(f.center.getX(), f.center.getY(),f.center.getZ());
	            int i=0;
	            for (Vertex v:f.vertices) {
	            	float x=v.pos.getX();
	            	float y=v.pos.getY();
	            	float z=v.pos.getZ();
	                gl.glNormal3f(x,y,z);
	                gl.glTexCoord2d(tcBlack[i][0],tcBlack[i][1]);
	                gl.glVertex3f(x,y,z);
	                i++;
	            }
	            Vertex v=f.vertices.get(0);
	            gl.glNormal3f(v.pos.getX(), v.pos.getY(),v.pos.getZ());
	            gl.glTexCoord2d(tcBlack[0][0],tcBlack[0][1]);
	            gl.glVertex3f(v.pos.getX(), v.pos.getY(),v.pos.getZ());
	            gl.glEnd();
	        }
	        
	        gl.glDisable(GL.GL_TEXTURE_2D);
	        gl.glEndList();
		}
        return dispList;
	}

    public synchronized void init(ViewFrustum frustum, GL2 gl, GLU glu) throws Exception {
    	if (whiteTexture==-1) {
    		{
	            InputStream imgInput=SoccerField.class.getResourceAsStream("/leather1.jpg");
	            TextureLoader.Texture texture=TextureLoader.readTexture(imgInput);
	            blackTexture=texture.toGL(gl,glu,false);
    		}
    		{
	            InputStream imgInput=SoccerField.class.getResourceAsStream("/burn.png");
	            TextureLoader.Texture texture=TextureLoader.readTexture(imgInput);
	            whiteTexture=texture.toGL(gl,glu,false);
    		}
    		
    		tcWhite=new float[6][2];
	    	for (int i=0;i<6;i++) {
	    		tcWhite[i][0]=0.5f-0.35f*(float)Math.sin(i/3f*(float)Math.PI);
	    		tcWhite[i][1]=0.5f+0.35f*(float)Math.cos(i/3f*(float)Math.PI);
	    	}
	    	tcBlack=new float[5][2];
	    	for (int i=0;i<5;i++) {
	    		tcBlack[i][0]=0.5f-0.4f*(float)Math.sin(i/2.5f*(float)Math.PI);
	    		tcBlack[i][1]=0.5f+0.4f*(float)Math.cos(i/2.5f*(float)Math.PI);
	    	}
    	}
    }
    /**
     * Returns all vertices of a football
     * @return
     */
    static private Set<Vertex> getVertices() {
        float t=(1.0f+(float)Math.sqrt(5.0))/2.0f;

        // Create vertices
        Set<Vertex> vertices=new HashSet<Vertex>();
        float[] posneg={-1f,1f};
        for (float i:posneg) {
            for (float j:posneg) {
                vertices.add(new Vertex(new Vector3D(0,i*1,j*3*t)));
                vertices.add(new Vertex(new Vector3D(i*1,j*3*t,0)));
                vertices.add(new Vertex(new Vector3D(i*3*t,0,j*1)));
                for (float k:posneg) {
                        vertices.add(new Vertex(new Vector3D(i*2,j*(1+2*t),k*t)));
                        vertices.add(new Vertex(new Vector3D(i*(1+2*t),j*t,k*2)));
                        vertices.add(new Vertex(new Vector3D(i*t,j*2,k*(1+2*t))));
                        vertices.add(new Vertex(new Vector3D(i*1,j*(2+t),k*2*t)));
                        vertices.add(new Vertex(new Vector3D(i*(2+t),j*2*t,k*1)));
                        vertices.add(new Vertex(new Vector3D(i*2*t,j*1,k*(2+t))));
                }
            }
        }

        // For each vertex, check distance to every other vertex.
        // When distance==2, we have an edge, and we add the other vertex as neighboor to this one
        for (Vertex v:vertices) {
            for (Vertex v2:vertices) {
                if (v==v2) continue;
                if (Vector3D.getDistance(v.pos, v2.pos) < 2.001f) {
                    v.neighboors.add(v2);
                }
            }
        }
        return vertices;
    }

    /**
     * Returns all faces of a football
     * @return
     */
    static private Set<Face> getFaces() {
        System.out.println("getFaces");
        Set<Vertex> vertices=getVertices();
        Set<Face> result=new HashSet<Face>();

        for (Vertex v:vertices) {

                // The neighboors of v
            Vertex a=v.neighboors.get(0);
            Vertex b=v.neighboors.get(1);
            Vertex c=v.neighboors.get(2);

            // Edges from v
            Vector3D va=Vector3D.subtract(a.pos,v.pos);
            Vector3D vb=Vector3D.subtract(b.pos,v.pos);
            Vector3D vc=Vector3D.subtract(c.pos,v.pos);

            // The normal of the face containing vertices {a,v,b}
            Vector3D abNorm=Vector3D.getNormal(va,vb).getNormalized();
            if (Vector3D.dotProduct(v.pos,abNorm)<0) {
                abNorm=abNorm.getNegative();
            }

            // The normal of the face containing vertices {a,v,c}
            Vector3D acNorm=Vector3D.getNormal(va,vc).getNormalized();
            if (Vector3D.dotProduct(v.pos,acNorm)<0) {
                acNorm=acNorm.getNegative();
            }

            // The normal of the face containing vertices {b,v,c}
            Vector3D bcNorm=Vector3D.getNormal(vb,vc).getNormalized();
            if (Vector3D.dotProduct(v.pos,bcNorm)<0) {
                bcNorm=bcNorm.getNegative();
            }

            final float errorTolerance=0.001f;

            // Find the face for each of the normals abNorm, acNorm or bcNorm
            for (Face f:result) {
                if (abNorm!=null && Vector3D.getDistance(f.normal, abNorm)<errorTolerance) {
                    f.vertices.add(v);
                    abNorm=null;
                } else if (acNorm!=null && Vector3D.getDistance(f.normal, acNorm)<errorTolerance) {
                    f.vertices.add(v);
                    acNorm=null;
                } else if (bcNorm!=null && Vector3D.getDistance(f.normal, bcNorm)<errorTolerance) {
                    f.vertices.add(v);
                    bcNorm=null;
                }
            }

            // Create a new Face for normals we couldn't match
            if (abNorm!=null) {
                Face f=new Face(abNorm);
                f.vertices.add(v);
                result.add(f);
            }
            if (acNorm!=null) {
                Face f=new Face(acNorm);
                f.vertices.add(v);
                result.add(f);
            }
            if (bcNorm!=null) {
                Face f=new Face(bcNorm);
                f.vertices.add(v);
                result.add(f);
            }
        }

        for (Face f:result) {
            f.orderVertices();
            f.calcCenter();
        }
        
        for (Face f:result) {
            if (f.opposite!=null) continue;
            for (Face g:result) {
                if (f==g || g.opposite!=null) continue;
                if (Vector3D.add(f.normal,g.normal).getLength()<0.001) {
                    f.opposite=g;
                    g.opposite=f;
                }
            }
        }
        
        for (Iterator<Face> i=result.iterator();i.hasNext();) {
            Face f=i.next();
            if (result.contains(f.opposite)) {
                //i.remove();
            }
        }
        
        return result;
    }




    static class Face {
        Vector3D normal;
        List<Vertex> vertices=new ArrayList<Vertex>();
        Vector3D center;
        Face opposite;

        Face(Vector3D normal) {
            this.normal=normal;
        }

        void orderVertices() {

            // Connect neighboors
            List<Vertex> orderedVertices=new ArrayList<Vertex>();
            orderedVertices.add(vertices.get(0));
            vertices.remove(0);
            while (!vertices.isEmpty()) {
                for (Vertex v:vertices) {
                    if (orderedVertices.get(orderedVertices.size()-1).neighboors.contains(v)) {
                        orderedVertices.add(v);
                        vertices.remove(v);
                        break;
                    }
                }
            }
            vertices=orderedVertices;

            // Check orientation
            Vertex a=vertices.get(0);
            Vertex b=vertices.get(1);
            Vertex c=vertices.get(2);

            Vector3D ba=Vector3D.subtract(a.pos, b.pos);
            Vector3D bc=Vector3D.subtract(c.pos, b.pos);
            Vector3D abcNorm=Vector3D.getNormal(ba, bc);
            if (Vector3D.dotProduct(b.pos,abcNorm)>0) {
                List<Vertex> reversed=new ArrayList<Vertex>();
                for (int i=0;i<vertices.size();i++) {
                    reversed.add(vertices.get(vertices.size()-1-i));
                }
                vertices=reversed;
            }
        }

        void calcCenter() {
            float x=0;
            float y=0;
            float z=0;
            float s=vertices.size();
            for (Vertex v:vertices) {
                x+=v.pos.getX();
                y+=v.pos.getY();
                z+=v.pos.getZ();
            }
            center=new Vector3D(x/s,y/s,z/s);
        }
    }

    static class Vertex {
        Vector3D pos;
        List<Vertex> neighboors=new ArrayList<Vertex>();

        Vertex(Vector3D pos) {
            this.pos=pos;
        }
    }
}