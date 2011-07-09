package no.ivark.soccerdemo.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import no.ivark.soccerdemo.vectormath.Matrix4x4;
import no.ivark.soccerdemo.vectormath.Vector3D;


public class Pilot extends ViewFrustum implements Runnable {
	private float velocity;
    private boolean pause=false;
	private boolean pup=false;
	private boolean pdown=false;
	private boolean pleft=false;
	private boolean pright=false;
	private boolean pacc=false;
	private boolean pdeAcc=false;

	private float upAngle=0.0f;
	private float rightAngle=0.0f;

    public Pilot() {
        super();
        velocity=0.0f;
    }

    public Pilot(Vector3D pos, Vector3D dir, Vector3D up) {
        super(pos,dir,up);
        velocity=0.0f;
    }

	public void start() {
		new Thread(this).start();
		keySetup();
	}

	/**
	 * Main thread
	 */
	public void run() {
		while(true) {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
			}

            if (pause) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                }
                continue;
            }
            
			if (pup) {
				if (upAngle>-0.1) upAngle-=0.005;
			} else if (pdown) {
				if (upAngle<0.1) upAngle+=0.005;
			}
			if (pleft) {
				if (rightAngle>-0.1) rightAngle-=0.005;
			} else if (pright) {
				if (rightAngle<0.1) rightAngle+=0.005;
			}

			if (pacc) {
				velocity+=0.05f;
			} else if (pdeAcc) {
				velocity-=0.05f;
			}

			if (!pup && ! pdown)upAngle/=1.3f;
			if (!pleft && !pright)rightAngle/=1.3f;

			normalize();
			up(upAngle);
			right(rightAngle);
			float dx=dir.getX()*velocity;
			float dy=dir.getY()*velocity;
			float dz=dir.getZ()*velocity;
            pos=new Vector3D(pos.getX()+dx, pos.getY()+dy,pos.getZ()+dz);
			repaint();
		}
	}

	public void keySetup() {
		this.addKeyListener(new KeyAdapter() {
            
			public void keyPressed(KeyEvent e) {
				int code=e.getKeyCode();
				switch (code) {
                case 32 :  // up
                    pause=true;
                    break;
                case 38 :  // up
                    pup=true;
                    break;
				case 40 : // down
					pdown=true;
					break;
				case 37 : // left
					pleft=true;
					break;
				case 39 : // right
					pright=true;
					break;
				case 65:
					pacc=true;
					break;
				case 90:
					pdeAcc=true;
					break;
				}

				char c=e.getKeyChar();
				if ('m'==c) {
					filled=!filled;
				} else if ('f'==c) {
					if (fogging) fogging=false;
					else fogging=true;
				} else if ('s'==c) {
					velocity=0.0f;
				} else if ('b'==c) {
					birdeye=!birdeye;
				} else if ('t'==c) {
					texture=!texture;
				} else if ('i'==c) {
					System.out.println("Pos: "+pos+", dir="+dir);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        
                    }
				}
			}

			public void keyReleased(KeyEvent e) {
				int code=e.getKeyCode();
                //System.out.println("code="+code);
				switch (code) {
                case 32 :  // up
                    pause=false;
                    break;
                case 38 :  // up
                    pup=false;
                    break;
				case 40 : // down
					pdown=false;
					break;
				case 37 : // left
					pleft=false;
					break;
				case 39 : // right
					pright=false;
					break;
				case 65:
					pacc=false;
					break;
				case 90:
					pdeAcc=false;
					break;
				}
			}
		});
	}

	public void up(float angle) {
		float sin=(float)Math.sin(angle);
		float cos=(float)Math.cos(angle);

		Vector3D v=Vector3D.getNormal(dir,up);
		v=v.getNormalized();
		float a=v.getX();
		float b=v.getY();
		float c=v.getZ();

		Matrix4x4 upTransform=new Matrix4x4(
				a*a*(1-cos)+cos,a*b*(1-cos)-c*sin,a*c*(1-cos)+b*sin,0.0f,
				a*b*(1-cos)+c*sin,b*b*(1-cos)+cos,b*c*(1-cos)-a*sin,0.0f,
				a*c*(1-cos)-b*sin,b*c*(1-cos)+a*sin,c*c*(1-cos)+cos,0.0f,
				0.0f,0.0f,0.0f,1.0f);

		Vector3D oldDir=dir;
		dir=upTransform.multiply(dir);

		Vector3D oldUp=up;
		up=upTransform.multiply(up);
	}

	public void right(float angle)
	{
		float sin=(float)Math.sin(angle);
		float cos=(float)Math.cos(angle);

		dir=dir.getNormalized();
		float a=dir.getX();
		float b=dir.getY();
		float c=dir.getZ();

		Matrix4x4 upTransform=new Matrix4x4(
				a*a*(1-cos)+cos,a*b*(1-cos)-c*sin,a*c*(1-cos)+b*sin,0.0f,
				a*b*(1-cos)+c*sin,b*b*(1-cos)+cos,b*c*(1-cos)-a*sin,0.0f,
				a*c*(1-cos)-b*sin,b*c*(1-cos)+a*sin,c*c*(1-cos)+cos,0.0f,
				0.0f,0.0f,0.0f,1.0f);
		up=upTransform.multiply(up);
	}

}
