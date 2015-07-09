package jisop.infrastructure;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class MethodInfoFinder {

    static List<Method> Find(List<Method> methods, Class returnType, String name, List<Class> parameters) {
        // IEnumerable<MethodInfo> retv = methods;
        // if (null != returnType)
        //     retv = retv.Where (m => m.ReturnType == returnType);
        // if (null != parameters)
        //     retv = retv.Where (m => m.getParameters ().Select (p => p.ParameterType).SequenceEqual (parameters));
        // if (null != name)
        //     retv = retv.Where (m => m.name.Equals (name, StringComparison.OrdinalIgnoreCase));
        // return retv;
        throw new RuntimeException("not implemented");
        //return null;
    }

    public List<Method> FindSet(List<Method> methods, Class returnType, String name, List<Class> parameters) {
        // var retv = Find (methods,returnType,null,parameters)
        //     .Where(m=>m.name.StartsWith("set", StringComparison.OrdinalIgnoreCase));
        //  if (null != name)
        //      retv = retv.Where (m => 
        //         m.name.Equals ("set" + name, StringComparison.OrdinalIgnoreCase)
        //         || m.name.Equals ("set_" + name, StringComparison.OrdinalIgnoreCase));
        // return retv.Select(m=> {
        //     if (m.name.StartsWith("set_"))
        //     {
        //         return new MethodInfoOrProperty(m, m.DeclaringType.GetProperty(m.name.Replace("set_",""))); // can prob be optimized
        //     }
        //     return new MethodInfoOrProperty(m); 
        // });
        throw new RuntimeException("not implemented");
    }

    public Method Match(List<Method> methods, Class returnType, String name, List<Class> parameters) {
        // var retv = Find (methods, returnType, name, parameters);
        // return retv.FirstOrDefault ();
        throw new RuntimeException("not implemented");
    }

    public Method MatchGet(List<Method> methods, String name, Class returnType, List<Class> parameters) {
        //var retv = Find (methods, returnType, null, parameters);

        // retv = retv.Where (m => m.name.Equals (name, StringComparison.OrdinalIgnoreCase)
        //            || m.name.Equals ("get_" + name, StringComparison.OrdinalIgnoreCase)
        //           || m.name.Equals ("get" + name, StringComparison.OrdinalIgnoreCase));
        //  var methodInfo = retv.FirstOrDefault ();
        //  if (null != methodInfo)
        //  {
        //       FieldInfo propertyInfo=null;
        //       if (methodInfo.name.StartsWith("get_"))
        //           propertyInfo = methodInfo.DeclaringType.GetProperty(methodInfo.name.Replace("get_", ""));
        //       return new MethodInfoOrProperty(methodInfo, propertyInfo);
        //    }
        //     else return null;
        throw new RuntimeException("not implemented");

    }

    public List<Method> GetOwnPublicMethods(Class type) {
        List<Method> f = new LinkedList<Method>();
        Method[] marray = type.getMethods();
        for (int i = 0; i < marray.length; i++) {
            Method m = marray[i];

            String name = m.getName().toLowerCase();
            if (!name.startsWith("get") && !name.startsWith("set")) {
                f.add(m);
            }
        }
        return f;
    }

    public List<Method> WithoutName(List<Method> marray, String name) {
        List<Method> f = new LinkedList<Method>();
        for (int i = 0; i < marray.size(); i++) {
            Method m = marray.get(i);
            if (!m.getName().equalsIgnoreCase(name)) {
                f.add(m);
            }
        }
        return f;
    }
}
