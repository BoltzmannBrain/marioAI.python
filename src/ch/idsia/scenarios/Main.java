package ch.idsia.scenarios;

import ch.idsia.benchmark.mario.simulation.SimulationOptions;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.HTMTask;
import ch.idsia.tools.MarioAIOptions;



public final class Main {
	public static void main(String[] args) throws Exception {
		
		/**
		 * Set the testbed and associated options.
		 */
	    final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
	//    marioAIOptions.setAgent(new ch.idsia.agents.controllers.HTMAgent());
	    marioAIOptions.setAgent(new ch.idsia.agents.controllers.RandomAgent());
	    marioAIOptions.setFlatLevel(false);
	    marioAIOptions.setLevelLength(72);
	    marioAIOptions.setLevelHeight(36);
	    marioAIOptions.setLevelDifficulty(0);
	    marioAIOptions.setLevelRandSeed(42);
	    marioAIOptions.setLevelType(2);  // [0..4] for overground, underground, castle, random
	    
	    marioAIOptions.setViewWidth(800);  // 320
	    marioAIOptions.setViewHeight(400);  // 240
	    
	    marioAIOptions.setFPS(100);
	    marioAIOptions.setTimeLimit(15);  // good for running random agent
	    
	    
//        for (int i = 0; i < 4; ++i) {  // runs 4 reps
//            int seed = 42;
//            marioAIOptions.setLevelDifficulty(i);
//            marioAIOptions.setLevelRandSeed(seed++);
//	    	  marioAIOptions.setLevelType(i); // [0,4]
//            final HTMTask sensorimotorTask = new HTMTask(marioAIOptions);
//    	    sensorimotorTask.setOptionsAndReset(marioAIOptions);
//    	    sensorimotorTask.runSingleEpisode(1);
//        }
//	    final Environment environment = new MarioEnvironment();  // not needed b/c task resets the environment
	    
	    
	    /**
	     * Set the task and associated options.
	     */
//	    final BasicTask basicTask = new BasicTask(marioAIOptions);
//	    basicTask.runSingleEpisode(1);
        
	    final HTMTask sensorimotorTask = new HTMTask(marioAIOptions);
	    sensorimotorTask.setOptionsAndReset(marioAIOptions);
	    sensorimotorTask.doEpisodes(4, true, 1);
	
	    System.exit(0);
	}
}
