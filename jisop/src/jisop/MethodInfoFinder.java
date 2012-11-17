package jisop;

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
        //     retv = retv.Where (m => m.GetParameters ().Select (p => p.ParameterType).SequenceEqual (parameters));
        // if (null != name)
        //     retv = retv.Where (m => m.Name.Equals (name, StringComparison.OrdinalIgnoreCase));
        // return retv;
        throw new RuntimeException("not implemented");
        //return null;
    }

    public List<Method> FindSet(List<Method> methods, Class returnType, String name, List<Class> parameters) {
        // var retv = Find (methods,returnType,null,parameters)
        //     .Where(m=>m.Name.StartsWith("set", StringComparison.OrdinalIgnoreCase));
        //  if (null != name)
        //      retv = retv.Where (m => 
        //         m.Name.Equals ("set" + name, StringComparison.OrdinalIgnoreCase)
        //         || m.Name.Equals ("set_" + name, StringComparison.OrdinalIgnoreCase));
        // return retv.Select(m=> {
        //     if (m.Name.StartsWith("set_"))
        //     {
        //         return new MethodInfoOrProperty(m, m.DeclaringType.GetProperty(m.Name.Replace("set_",""))); // can prob be optimized
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

        // retv = retv.Where (m => m.Name.Equals (name, StringComparison.OrdinalIgnoreCase) 
        //            || m.Name.Equals ("get_" + name, StringComparison.OrdinalIgnoreCase)
        //           || m.Name.Equals ("get" + name, StringComparison.OrdinalIgnoreCase));
        //  var methodInfo = retv.FirstOrDefault ();
        //  if (null != methodInfo)
        //  {
        //       PropertyInfo propertyInfo=null;
        //       if (methodInfo.Name.StartsWith("get_"))
        //           propertyInfo = methodInfo.DeclaringType.GetProperty(methodInfo.Name.Replace("get_", ""));
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

    List<Method> WithoutName(List<Method> marray, String name) {
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
