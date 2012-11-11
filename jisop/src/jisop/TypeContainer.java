package jisop;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
class TypeContainer implements ObjectFactory {
    public TypeContainer()
    {
        instances= new HashMap<Class, Object>();
    }
    
    HashMap<Class, Object> instances;
    private ObjectFactory factory;

    @Override
    public Object build(Class c) {
        if (!instances.containsKey(c)) {
            if (factory != null) {
                instances.put(c, factory.build(c));
            } else {
                try {
                    instances.put(c, c.newInstance());
                } catch (InstantiationException ex) {
                    Logger.getLogger(TypeContainer.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(TypeContainer.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
            }
        }
        return instances.get(c);
    }

    void setFactory(ObjectFactory objectFactory) {
        this.factory = objectFactory;
    }
}
