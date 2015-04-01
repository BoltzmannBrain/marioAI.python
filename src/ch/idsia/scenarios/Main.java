package ch.idsia.scenarios;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.HTMTask;
import ch.idsia.tools.MarioAIOptions;



public final class Main {
	public static void main(String[] args) throws Exception {
	    final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
	//    marioAIOptions.setAgent(new ch.idsia.agents.controllers.HTMAgent());
	    marioAIOptions.setAgent(new ch.idsia.agents.controllers.ForwardJumpingAgent());
	    marioAIOptions.setFlatLevel(true);
	    marioAIOptions.setLevelLength(42);
	
	//    final Environment environment = new MarioEnvironment();
	    final BasicTask basicTask = new BasicTask(marioAIOptions);
	    final HTMTask sensorimotorTask = new HTMTask(marioAIOptions);
	//    final PrintWriter out = sensorimotorTask.getSocketOut();
	//        for (int i = 0; i < 10; ++i)
	//        {
	//            int seed = 0;
	//            do
	//            {
	//                marioAIOptions.setLevelDifficulty(i);
	//                marioAIOptions.setLevelRandSeed(seed++);
	    sensorimotorTask.setOptionsAndReset(marioAIOptions);
	//    basicTask.runSingleEpisode(1);
	    sensorimotorTask.doEpisodes(2,true,1);
	
	    System.exit(0);
	}
}
