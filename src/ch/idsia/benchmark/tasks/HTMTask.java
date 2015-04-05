package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.statistics.StatisticalSummary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;


public class HTMTask implements Task {
	protected final static Environment environment = MarioEnvironment.getInstance();
	private Agent agent;
	protected MarioAIOptions options;
	private long COMPUTATION_TIME_BOUND = 42; // stands for prescribed  FPS 24.
	private String name = getClass().getSimpleName();
	private EvaluationInfo evaluationInfo;
	private Vector<StatisticalSummary> statistics = new Vector<StatisticalSummary>();

public HTMTask(MarioAIOptions marioAIOptions) {
    this.setOptionsAndReset(marioAIOptions);
}


/**
 * @param repetitionsOfSingleEpisode
 * @return boolean flag whether controller is disqualified or not
 */
public boolean runSingleEpisode(final int repetitionsOfSingleEpisode) throws Exception {
    long c = System.currentTimeMillis();
    for (int r = 0; r < repetitionsOfSingleEpisode; ++r) {
        this.reset();
        String fromClient;
        String toClient;
        
		ServerSocket server = new ServerSocket(1123);
		System.out.println("Java server waiting on connection via port 1123");
        
		Socket client = server.accept();
	    System.out.println("Java server connected to client on port 1123");
    	PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    	BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    	fromClient = in.readLine();
        System.out.println("message received: " + fromClient);
        
        Integer i = 0;
        boolean run = true;
        while (run) {
        	environment.tick();
//        	System.out.println("position = " + environment.getMarioFloatPos());
            i += 1;
            
            if (!GlobalOptions.isGameplayStopped) {
                c = System.currentTimeMillis();
                agent.integrateObservation(environment);
                agent.giveIntermediateReward(environment.getIntermediateReward());

                boolean[] action = agent.getAction();  // keep this here, even when using HTM agent
                if (System.currentTimeMillis() - c > COMPUTATION_TIME_BOUND)
                    return false;
//                System.out.println("action = " + Arrays.toString(action));
//            environment.setRecording(GlobalOptions.isRecording);

                System.out.println("step #" + i);
                
//                out.println(environment.getIntermediateReward());
                toClient = Integer.toString(environment.getMarioX()) + "+" + Integer.toString(environment.getMarioY()) + "+" + Arrays.toString(action);
                // for y to be useful, need to know if agent is immediately over ground or air
                out.println(toClient);
                // HTM gets input here, and then returns data via in.readLine()
                fromClient = in.readLine();
                System.out.println("received from python: " + fromClient);
                
                if (fromClient.equals("Shut it down")) { // end the run b/c python said so
                	toClient = "Kill\n";
                	System.out.println("python client says shutdown");
                    out.println(toClient);
                    client.close();
                    System.out.println("socket closed");
                    break;
                } else { // keep going
	                System.out.println("performing action: " + Arrays.toString(action));
	                environment.performAction(action);
                }
            }
            run = !environment.isLevelFinished();
            if (!run) {
            	toClient = "Kill";
            	System.out.println("telling python client to shutdown");
                out.println(toClient);
                client.close();
                System.out.println("socket closed");
            }
        }
        server.close();
        environment.closeRecorder(); //recorder initialized in environment.reset
        environment.getEvaluationInfo().setTaskName(name);
        this.evaluationInfo = environment.getEvaluationInfo().clone();
        
        
    }
    return true;
}

public Environment getEnvironment() {
    return environment;
}

public int evaluate(Agent controller) {
    return 0;
}

public void setOptionsAndReset(MarioAIOptions options) {
    this.options = options;
    reset();
}

public void setOptionsAndReset(final String options) {
    this.options.setArgs(options);
    reset();
}

public void doEpisodes(int amount, boolean verbose, final int repetitionsOfSingleEpisode) throws Exception
{
    for (int j = 0; j < EvaluationInfo.numberOfElements; j++)
    {
        statistics.addElement(new StatisticalSummary());
    }
    for (int i = 0; i < amount; ++i)
    {
        this.reset();
        this.runSingleEpisode(repetitionsOfSingleEpisode);
        if (verbose)
            System.out.println(environment.getEvaluationInfoAsString());

        for (int j = 0; j < EvaluationInfo.numberOfElements; j++)
        {
            statistics.get(j).add(environment.getEvaluationInfoAsInts()[j]);
        }
    }

    System.out.println(statistics.get(3).toString());
}

public boolean isFinished()
{
    return false;
}

public void reset()
{
    agent = options.getAgent();
    environment.reset(options);
    agent.reset();
    agent.setObservationDetails(environment.getReceptiveFieldWidth(),
            environment.getReceptiveFieldHeight(),
            environment.getMarioEgoPos()[0],
            environment.getMarioEgoPos()[1]);
}

public String getName()
{
    return name;
}

public void printStatistics()
{
    System.out.println(evaluationInfo.toString());
}

public EvaluationInfo getEvaluationInfo()
{
//    System.out.println("evaluationInfo = " + evaluationInfo);
    return evaluationInfo;
}


}

//            start timer
//            long tm = System.currentTimeMillis();

//            System.out.println("System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND = " + (System.currentTimeMillis() - tm ));
//            if (System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND)
//            {
////                # controller disqualified on this level
//                System.out.println("Agent is disqualified on this level");
//                return false;
//            }