"""
Run an HTM agent in the Mario testbed.
1. Generate (i) sensory and (ii) motor sequences
  i. sequences of x values and sequences of y values
  ii. sequences of binary lists of length 6 -- e.g. [0,1,1,0,0,0]
2. Encode sequences
3. Mario runner object... train and test by calling tm.compute()
  note: need novel sequences for test phase, which returns stats
"""
import argparse
import csv
import numpy
import os
import subprocess
import time


from python_scripts.htm_encoders import MarioEncoders
from python_scripts.sensorimotor_experiment_runner import MarioSensorimotorRunner
from python_scripts import htm_client


#java_cmd = ['java', '-cp', 'bin/MarioAI/:lib/asm-all-3.3.jar:lib/jdom.jar',
#    'ch.idsia.scenarios.Main']
##python_cmd = ['python', 'python_scripts/htm_client.py']
#subprocess.call(java_cmd, shell=False)



def generateSequences(no_runs):
  print "Generating sensory and motor sequences from Mario API."
  motor_sequences = []
  sensory_sequences_x = []
  sensory_sequences_y = []

  java_cmd = ['java', '-cp', 'bin/MarioAI/:lib/asm-all-3.3.jar:lib/jdom.jar',
      'ch.idsia.scenarios.Main']
  subprocess.Popen(java_cmd, shell=False)
  time.sleep(1)
  print "HELLO WORLD"
  for _ in xrange(no_runs):
    m, s = htm_client.runner()
    motor_sequences.append(m)
    sensory_sequences_x.append(s[0])
    sensory_sequences_y.append(s[1])
    time.sleep(0.1)
  print "=============================================="
  print "motor sequences: ", motor_sequences
  print "______________________________________________"
  print "sensory sequences (x and y): ", sensory_sequences_x, sensory_sequences_y
  print "______________________________________________"
  
#  import pdb; pdb.set_trace()

  return [sensory_sequences_x, sensory_sequences_y], motor_sequences


def encodeSequences(sensory_sequences, motor_sequences, environment_size, write=False):
  print "Encoding raw sequences as SDRs"
#  nSensor, wSensor = 1024, 20
#  nMotor, wMotor = 1024, 20
  encoder = MarioEncoders(1024, 20, False)
  sensorySDRs = encoder.encodeXYPositionSequences(sensory_sequences,  # [x,y]
                                                  environment_size)   # [(xmin,xmax), (ymin,ymax)]
  motorSDRs = encoder.encodeMotorSequences(motor_sequences)
#  import pdb; pdb.set_trace()

  if write==True:
    with open("sensorySDRs.csv", "wb") as f:
      writer = csv.writer(f, delimiter=",", quoting=csv.QUOTE_ALL)
      for s in sensorySDRs:
        writer.writerow(s)
    print "sensory SDR sequences written to \'sensorySDRs.csv\'"
    with open("motorSDRs.csv", "wb") as f:
      writer = csv.writer(f, delimiter=",", quoting=csv.QUOTE_ALL)
      for m in motorSDRs:
        writer.writerow(m)
    print "motor SDR sequences written to \'motorSDRs.csv\'"
  return sensorySDRs, motorSDRs


def main(args):
  start = time.time()
  print "Setting up experiment..."
  s, m = generateSequences(args.no_runs)
  sSDR, mSDR = encodeSequences(s, m, environment_size=[(0,71),(0,35)])
#  sSDR, mSDR = encodeSequences(generateSequences(1000), ___)
  print "Running sensorimotor experiment..."
  mario = MarioSensorimotorRunner()
  # Train and test
  print "Training TM on sequences"
  mario.feed([sSDR, mSDR], verbosity=2, showProgressInterval=50)
  print "Testing TM on sequences"
  s, m = generateSequences(args.no_runs)
  sSDR, mSDR = encodeSequences(s, m, environment_size=[(0,71),(0,35)])
#  sSDR, mSDR = encodeSequences(generateSequences(100), ___)
  mario.feed([sSDR, mSDR], tmLearn=False, verbosity=2, showProgressInterval=50)


  elapsed = int(time.time() - start)
  print "Total time: {0:2} seconds".format(elapsed)


if __name__ == "__main__":
  # TO DO: setup args
  parser = argparse.ArgumentParser()
  parser.add_argument("--no_runs",
                    help="Number of runs through level.",
                    type=int,
                    default=100)
                    # first param in Main.sensorimotorTask.doEpisodes(no_runs,true,1);
  parser.add_argument("--save",
                    help="Save sequences to an output csv.",
                    default=False,
                    action="store_true")
  args = parser.parse_args()
  
#  if args.runs != 2: print "Warning: number of runs x must match parameter "\
#    "passed to java server in Main.java at sensorimotorTask.doEpisodes(x,_,_)."

  main(args)
