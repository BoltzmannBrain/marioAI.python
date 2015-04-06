package ch.idsia.scenarios;

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
	    marioAIOptions.setAgent(new ch.idsia.agents.controllers.ForwardJumpingAgent());
	    marioAIOptions.setFlatLevel(true);
	    marioAIOptions.setLevelLength(72);
	    marioAIOptions.setLevelHeight(36);
	    marioAIOptions.setLevelDifficulty(0);
	    marioAIOptions.setLevelRandSeed(42);
	    marioAIOptions.setLevelType(0);  // 1 is underground
	    
	    marioAIOptions.setViewWidth(320);
	    marioAIOptions.setViewHeight(240);
	    
//        for (int i = 0; i < 10; ++i)
//        {
//            int seed = 0;
//            do
//            {
//                marioAIOptions.setLevelDifficulty(i);
//                marioAIOptions.setLevelRandSeed(seed++);	
//	    final Environment environment = new MarioEnvironment();  // not needed b/c task resets the environment
	    
	    
	    /**
	     * Set the task and associated options.
	     */
//	    final BasicTask basicTask = new BasicTask(marioAIOptions);
//	    basicTask.runSingleEpisode(1);
	    final HTMTask sensorimotorTask = new HTMTask(marioAIOptions);
	    sensorimotorTask.setOptionsAndReset(marioAIOptions);
	    sensorimotorTask.doEpisodes(3, true, 1);
	
	    System.exit(0);
	}
}
