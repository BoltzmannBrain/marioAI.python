"""
Run an HTM agent in the Mario testbed.
"""
import subprocess

from python_scripts import htm_client2


cmd = ['java', '-cp', 'bin/MarioAI/:lib/asm-all-3.3.jar:lib/jdom.jar',
    'ch.idsia.scenarios.Main']
import pdb; pdb.set_trace()
no_runs = 2  # first param in Main.sensorimotorTask.doEpisodes(2,true,1);
subprocess.call(cmd, shell=False)

for i in range(no_runs):
  htm_client2







#def executeJavaFile():
#  cmd = ["java", "-cp", "bin/MarioAI/:lib/asm-all-3.3.jar:lib/jdom.jar",
#    "ch.idsia.scenarios.Main"]
#  subprocess.call(cmd, shell=False)
#
#
#def run(args):
#  subprocess.call(["python", "python_scripts/htm_client.py"])
##  executeJavaFile()
#
#
#if __name__ == "__main__":
#  run(None)