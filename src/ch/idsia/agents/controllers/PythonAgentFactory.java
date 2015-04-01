package ch.idsia.agents.controllers;

import java.io.File;
import java.util.Properties;

import ch.idsia.agents.controllers.PythonAgent;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.Py;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;



public class PythonAgentFactory {

	private PyObject pythonAgentClass;
	
//	public void init(){
//		PythonInterpreter interp = new PythonInterpreter(null, new PySystemState());
//
//        PySystemState sys = Py.getSystemState();
//        sys.path.append(new PyString("/Users/alavin/Library/Python/2.7/lib/python/site-packages"));
//        sys.path.append(new PyString(System.getProperty("user.dir") + "/python_scripts/"));
//	}
	/**
	 * Create a new PytonInterpeter object in order to import the python module 
	 * we wish to coerce.
	 */
	public PythonAgentFactory() {
		PythonInterpreter interp = new PythonInterpreter(null, new PySystemState());

        PySystemState sys = Py.getSystemState();
        sys.path.append(new PyString("/Users/alavin/Library/Python/2.7/lib/python/site-packages"));
        sys.path.append(new PyString(System.getProperty("user.dir") + "/python_scripts/"));
        System.out.print(sys.path);
//		Properties props = new Properties();
//		String path1 = "/Users/alavin/Library/Python/2.7/lib/python/site-packages";
//		String path2 = System.getProperty("user.dir") + "/python_scripts/";
//		props.setProperty("python.path", path1+":"+path2);
//		System.out.println(props.getProperty("python.path"));
//		PythonInterpreter.initialize(System.getProperties(), props, new String[] {""});
		
//		PySystemState sys = Py.getSystemState();
//		sys.path.append(new PyString(System.getProperty("user.dir") + "/python_scripts/forwardagent.py"));
		
		PythonInterpreter interpreter = new PythonInterpreter();
//        sys.path.append(new PyString(System.getProperty("user.dir")));
//        sys.path.append(new PyString("/usr/local/bin/Jython2.5.3"));
        
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		boolean exist = new File(System.getProperty("user.dir") + "/python_scripts/forwardagent.py").isFile();
		System.out.println(exist);
		
		interpreter.exec("from forwardagent import ForwardAgent");
		pythonAgentClass = interpreter.get("ForwardAgent");
	}
	
	/**
	 * Coerce the referenced python module into Java bytecode.
	 */
	public PythonAgent create (String name) {
		PyObject pythonAgentObject = pythonAgentClass.__call__(new PyString(name));
		return (PythonAgent)pythonAgentObject.__tojava__(PythonAgent.class);
	}
	
}
