package ch.idsia.agents;

import org.python.core.PyFrame;
import org.python.core.PyFunctionTable;
import org.python.util.PythonInterpreter;

public class TestJ
{
  public static void main(String[] args)
  {
    final PythonInterpreter interpreter = new PythonInterpreter();

    interpreter.exec("import sys");

    try
      {
        final Class<?> clazz = Class.forName("simplepythonagent$py");

        final java.lang.reflect.Constructor constructor
          = clazz.getConstructor(String.class);

        final PyFunctionTable module = (PyFunctionTable)constructor.newInstance("");

        final java.lang.reflect.Method method
          = clazz.getDeclaredMethod("getAction$1",
                                    PyFrame.class,
                                    org.python.core.ThreadState.class);

        method.invoke(module,
                      (PyFrame)interpreter.eval("sys._getframe()").__tojava__(PyFrame.class),
                      org.python.core.Py.getThreadState());
      }
    catch (final ClassNotFoundException e)
      { e.printStackTrace(); }
    catch (final NoSuchMethodException e)
      { e.printStackTrace(); }
    catch (final InstantiationException e)
      { e.printStackTrace(); }
    catch (final IllegalAccessException e)
      { e.printStackTrace(); }
    catch (final java.lang.reflect.InvocationTargetException e)
      { e.printStackTrace(); }
  }
}