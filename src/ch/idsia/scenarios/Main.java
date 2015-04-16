package ch.idsia.scenarios;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.HTMTask;
import ch.idsia.tools.MarioAIOptions;


public final class Main {
	public static void main(String[] args) throws Exception {
		
		/**
		 * Set the testbed and associated options.
		 */
	    final MarioAIOptions options = new MarioAIOptions(args);
	//    options.setAgent(new ch.idsia.agents.controllers.HTMAgent());
	    options.setAgent(new ch.idsia.agents.controllers.ForwardJumpingAgent());
	    options.setMarioMode(2);
	    
	    options.setFlatLevel(false);
	    options.setLevelLength(72);
	    options.setLevelHeight(72);
	    System.out.println("LENGTH = " + options.getLevelHeight());
	    options.setLevelDifficulty(0);
	    options.setLevelRandSeed(42);
	    options.setLevelType(2);  // [0..4] for overground, underground, castle, random
	    
	    options.setViewWidth(800);  // 320
	    options.setViewHeight(400);  // 240
	    options.setVisualization(true);
	    
	    options.setFPS(24);
	    options.setTimeLimit(15);  // good for running random agent
//	    options.setTimeLimit(Integer.MAX_VALUE);
	    
	    // Set parameter values; see strings in ch.idsia.utils.ParameterContainer.java
	    options.setParameterValue("-mgr", "1.0"); // set Mario gravity
	    options.setParameterValue("-mix", "32"); // set Mario initial physical x
	    options.setParameterValue("-miy", "32"); // set Mario initial physical y
	    options.setParameterValue("-rfw", "10"); // set grid width
	    options.setParameterValue("-rfh", "10"); // set grid height
	    options.setReceptiveFieldWidth(10);
	    
	    String param = "-rfw";
//	    options.setParameterValue(param, "0");
	    System.out.println("--->" + options.getParameterValue(param));
	    
	    // ZLevel params at standard values; https://sites.google.com/site/imarioproject/Home/zoom-levels-zlevels-
	    options.setZLevelScene(1);
	    options.setZLevelEnemies(0);
//	    ??????? SimulationOptions options = new MarioAIOptions(new String[0]);
	    
	    
//        for (int i = 0; i < 4; ++i) {  // runs 4 reps
//            int seed = 42;
//            options.setLevelDifficulty(i);
//            options.setLevelRandSeed(seed++);
//	    	  options.setLevelType(i); // [0,4]
//            final HTMTask sensorimotorTask = new HTMTask(options);
//    	    sensorimotorTask.setOptionsAndReset(options);
//    	    sensorimotorTask.runSingleEpisode(1);
//        }
//	    final Environment environment = new MarioEnvironment();  // not needed b/c task resets the environment
	    
	    
	    /**
	     * Set the task and associated options.
	     */
//	    final BasicTask basicTask = new BasicTask(options);
//	    basicTask.runSingleEpisode(1);
        
	    final HTMTask sensorimotorTask = new HTMTask(options);
	    sensorimotorTask.setOptionsAndReset(options);
	    sensorimotorTask.doEpisodes(4, true, 1, true);  // (amount, verbose, reps of single episode, print Mario stats)
	
	    System.exit(0);
	}
}
