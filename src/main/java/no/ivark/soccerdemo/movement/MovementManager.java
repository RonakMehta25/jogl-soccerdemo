package no.ivark.soccerdemo.movement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import no.ivark.soccerdemo.vectormath.Vector3D;
import no.ivark.soccerdemo.view.Updatable;


/**
 * Handles movement of objects.
 * 
 * 
 * 
 * @author Marius Krabset
 */
public class MovementManager implements Updatable {
    private final Zone[][] zones=new Zone[480][400]; 
    private final Set<MovingObject> movementManagables=new HashSet<MovingObject>(); 
    private final Set<Zone> zonesWithMovementManagables=new HashSet<Zone>(); 
    private Map<MovingObject,Zone> zoneForMovementManagable=new HashMap<MovingObject,Zone>(); 
    
    public MovementManager() { 
        for (int i=0;i<zones.length;i++) { 
                for(int j=0;j<zones[i].length;j++) { 
                        zones[i][j]=new Zone(); 
                } 
        } 
        for (int i=0;i<zones.length;i++) { 
        	for(int j=0;j<zones[i].length;j++) { 
        		Zone z=zones[i][j]; 
                for (int a=-1;a<=1;a++) { 
                	for (int b=-1;b<=1;b++) { 
                		int x=i+a; 
                        int y=j+b; 
                        if (x>=0 && x<zones.length && 
                        	y>=0 && y<zones[x].length) { 
                            	z.surroundingZones.add(zones[x][y]); 
                        } 
                    } 
                } 
            } 
        } 
    }

    public void register(MovingObject m) { 
    	updateZone(m,null); 
    	movementManagables.add(m); 
    }

    private Zone getZoneByPosition(MovingObject m) { 
        Vector3D pos=m.getPos(); 
        int x=(int)(pos.getX()*4+240.0); 
        if (x<0) x=0; 
        else if (x>239) x=239;

        int y=(int)(pos.getY()*4+200.0); 
        if (y<0) y=0; 
        else if (y>199) y=199; 
        return zones[x][y]; 
    }
//    private Zone getZoneByPosition(MovingObject m) { 
//        Vector3D pos=m.getPos(); 
//        int x=(int)(pos.getX()+60.0); 
//        if (x<0) x=0; 
//        else if (x>59) x=59;
//
//        int y=(int)(pos.getY()+50.0); 
//        if (y<0) y=0; 
//        else if (y>49) y=49; 
//        return zones[x][y]; 
//    }

    private void updateZone(MovingObject m,Zone lastZone) { 
        Zone newZone=getZoneByPosition(m); 
        if (lastZone!=newZone) { 
                newZone.addMovementManagable(m); 
                zoneForMovementManagable.put(m,newZone); 
                zonesWithMovementManagables.add(newZone); 
                if (lastZone!=null) { 
                        lastZone.removeMovementManagable(m); 
                        if (lastZone.isEmpty()) { 
                                zonesWithMovementManagables.remove(lastZone); 
                        } 
                } 
        } 
    }

    public Set<MovingObject> getMovementManagables() { 
        return movementManagables; 
    }

    public void update() {
		for (Zone z : zonesWithMovementManagables) {
			for (MovingObject m : z.mms) {
				for (Zone sz : z.surroundingZones) {
					for (MovingObject n : sz.mms) {
						if (n == m)
							continue;
						Vector3D pos = m.getPos();
						Vector3D velo = m.getVelocity();
						if (velo == null)
							continue;
						Vector3D qPos = n.getPos();
						float radiusSum = m.getRadius() + n.getRadius();
						if (Vector3D.getDistance(pos, qPos) < radiusSum) {
							m.onCollision(n);
						}
					}
				}
			}
		}
		for (MovingObject m : movementManagables) {
			m.move();
			updateZone(m, zoneForMovementManagable.get(m));
		}
	}

    private class Zone {
		Set<Zone> surroundingZones = new HashSet<Zone>();
		Set<MovingObject> mms = new HashSet<MovingObject>();

		public void addMovementManagable(MovingObject m) {
			mms.add(m);
		}

		public void removeMovementManagable(MovingObject m) {
			mms.remove(m);
		}

		public boolean isEmpty() {
			return mms.isEmpty();
		}
	}
}
