package isop.domain;

import java.util.HashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class TypeContainer implements Function<Class,Object> {
    public TypeContainer()
    {
        instances= new HashMap<>();
    }
    
    private HashMap<Class, Object> instances;
    private Function<Class,Object> factory;
    public void add(Class t,Object instance){
        instances.put(t,instance);
    }

    public Object apply(Class c) {
        if (!instances.containsKey(c)) {
            if (factory != null) {
                instances.put(c, factory.apply(c));
            } else {
                try {
                    instances.put(c, c.newInstance());
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(TypeContainer.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
            }
        }
        return instances.get(c);
    }

    public void setFactory(Function<Class,Object> objectFactory) {
        this.factory = objectFactory;
    }
}
