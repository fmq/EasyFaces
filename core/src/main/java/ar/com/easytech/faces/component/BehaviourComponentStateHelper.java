package ar.com.easytech.faces.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHelper;
import javax.faces.context.FacesContext;
import javax.faces.component.behavior.ClientBehaviorBase;

public class BehaviourComponentStateHelper<T extends ClientBehaviorBase> implements StateHelper {

	private T component;
	
    private Map<Serializable, Object> deltaMap;
    private Map<Serializable, Object> defaultMap;
    
    public BehaviourComponentStateHelper(T component) {
    	
    	this.component = component;
    	deltaMap = null;
    	defaultMap = new HashMap<Serializable, Object>();
    	
    }

    
    
	public Object put(Serializable key, Object value) {
		
    	if (component.initialStateMarked() ) {
    		Object currVal = deltaMap.put(key, value);
    		
    		if (currVal == null)
    			return defaultMap.put(key, value);
    		
    		defaultMap.put(key, value);
    		return currVal;
    	}
    	
		return defaultMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public Object put(Serializable key, String mapKey, Object value) {
		
		Object currVal = null;
		
		if (component.initialStateMarked()) {
			//get the map from the delta
			Map<String, Object> tmpMap = (Map<String, Object>)deltaMap.get(key);
			// If null initialize and add the delta
			if (tmpMap == null) {
				tmpMap = new HashMap<String, Object>();
				deltaMap.put(key, tmpMap);
			}
			// set currentVal to the new map
			currVal = tmpMap.put(mapKey, value);
		}
		
		// get the value from the defaultMap
		Map<String, Object> defTmpMap = (Map<String, Object>)defaultMap.get(key);
		// If null initilize and add to map
		if (defTmpMap == null) {
			defTmpMap = new HashMap<String, Object>();
			defaultMap.put(mapKey, defTmpMap);
		}
		
		// If current value is not null
		if (currVal != null ) 
			// Add this to the defult map and return
			return defTmpMap.put(mapKey, value);
		
		// Since the value was in the deltaMap add to
		// the default and return the delta
		defTmpMap.put(mapKey,value);
		
		return currVal;
	}

	public void add(Serializable key, Object value) {
		// Now Lists
		if (component.initialStateMarked()) {
			// Get value from deltaMap
			List<Object> deltas = (List<Object>)deltaMap.get(key);
			// If null initilize and add to map
			if (deltas == null) {
				deltas = new ArrayList<Object>();
				deltaMap.put(key, deltas);
			}
			// Add value to deltas 
			deltas.add(value);
		}
		// Now get from defaultMap
		List<Object> defaults = (List<Object>) defaultMap.get(key);
		// If null initialize and add..
        if (defaults == null) {
        	defaults = new ArrayList<Object>(4);
            defaultMap.put(key, defaults);
        }
        //add value..
        defaults.add(value);
		
	}

	public Object get(Serializable key) {
		return defaultMap.get(key);
	}
    
	
	public Object remove(Serializable key) {
		
		if(component.initialStateMarked()) {
            Object retVal = deltaMap.remove(key);

            if(retVal==null) 
            	return defaultMap.remove(key);

            defaultMap.remove(key);
            return retVal;
        } else
        	return defaultMap.remove(key);
	}
	
	public Object remove(Serializable key, Object valueOrKey) {
		
		Object source = get(key);
        if (source instanceof Collection) {
            return removeFromList(key, valueOrKey);
        } else if (source instanceof Map) {
            return removeFromMap(key, valueOrKey.toString());
        }
        return null;
	}
	
	
    
	public Object saveState(FacesContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	public void restoreState(FacesContext context, Object state) {
		// TODO Auto-generated method stub

	}

	public boolean isTransient() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTransient(boolean newTransientValue) {
		// TODO Auto-generated method stub

	}

	
	

	

	public Object eval(Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object eval(Serializable key, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	 private Object removeFromList(Serializable key, Object value) {
	        Object ret = null;
	        if (component.initialStateMarked() || value instanceof PartialStateHolder) {
	            Collection<Object> deltaList = (Collection<Object>) deltaMap.get(key);
	            if (deltaList != null) {
	                ret = deltaList.remove(value);
	                if (deltaList.isEmpty()) {
	                    deltaMap.remove(key);
	                }
	            }
	        }
	        Collection<Object> list = (Collection<Object>) get(key);
	        if (list != null) {
	            if (ret == null) {
	                ret = list.remove(value);
	            } else {
	                list.remove(value);
	            }
	            if (list.isEmpty()) {
	                defaultMap.remove(key);
	            }
	        }
	        return ret;
	    }


	    private Object removeFromMap(Serializable key, String mapKey) {
	        Object ret = null;
	        if (component.initialStateMarked()) {
	            Map<String,Object> dMap = (Map<String,Object>) deltaMap.get(key);
	            if (dMap != null) {
	                ret = dMap.remove(mapKey);
	                if (dMap.isEmpty()) {
	                    deltaMap.remove(key);
	                }
	            }
	        }
	        Map<String,Object> map = (Map<String,Object>) get(key);
	        if (map != null) {
	            if (ret == null) {
	                ret = map.remove(mapKey);
	            } else {
	                map.remove(mapKey);

	            }
	            if (map.isEmpty()) {
	                defaultMap.remove(key);
	            }
	        }
	        if (ret != null && !component.initialStateMarked()) {
	            deltaMap.remove(key);
	        }
	        return ret;
	    }

	

}
