package sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;

public class TestbedHook {
	
	public static Socket socket;
	public static PrintWriter out;
	public static BufferedReader in;
	
//	public static final ourClient = new TestbedHook();
	private static TestbedHook theClient;
	protected final static Environment environment = MarioEnvironment.getInstance();
	
	public TestbedHook() {
		try {
			System.out.println("J: IN THE TRY");
			System.out.println("J: Beginning of socket constructer");
			socket = new Socket("localhost", 1223);
			out = new PrintWriter(socket.getOutputStream(), true);
			in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("J: out and in are: " + out + "\n" + in);
			System.out.println("J: End of socket constructer");
//			System.out.print(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		/**
		 * Establish socket connection to python client
		 */
		if (theClient == null) {
			System.out.println("J: IN MAIN");
			TestbedHook theClient = new TestbedHook();
		}
	}
	
	
	public static TestbedHook getClient() {
		if (theClient == null) {
			theClient = new TestbedHook();
		}
		return theClient;
	}
	
	
	public static Socket getSocket() {
		return socket;
	}
	public static PrintWriter getOut() {
		return out;
	}
	public static BufferedReader getIn() {
		return in;
	}
	
	/**
	 * Close the socket when the testbed is closed.
	 */
	public void testbedClose() {
		if (environment.isLevelFinished() || GlobalOptions.isGameplayStopped)
		{
			out.close();
		}	
	}
	
	
	/**
	 * At each time step of the test, send a message to the socket client.
	 */
	public void timeStep() {
		
		Integer intReward = environment.getIntermediateReward();
		Long time = System.currentTimeMillis();
		String message = intReward + " " + time;
		
		try {
			System.out.println("Writing to socket: " + message);
			out.println(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}

