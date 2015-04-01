import os
import socket
import subprocess

from nupic.research import *


def getAction(i):
  return "this would be python agent action #%i\n" % i

HOST = "localhost"
PORT = 1123

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.connect((HOST, PORT))

sock.send("entering the python while loop...\n")
i = 1
while 1<2:
  data_in = sock.recv(1024)
  print "step ", i
  print "data received: ", data_in
    # this data represents the environment preceding the action for this step
  data_out = getAction(i)
  sock.send(data_out)  # random for sensorimotor
  print "data sent: ", data_out
  i += 1
  
  # Stoppage criteria
  if (data_in == "Kill\n"):
    print "Stoppage criteria reached, shutting down socket..."
#    sock.send("Shut it down")
    sock.close()
    print "Socket closed"
    break
  elif i>1000:
    # Note: this doesn't break cleanly; sock.send() isn't received by server before sock.close() is called
    print "Stoppage criteria reached, shutting down socket..."
    sock.send("Shut it down")
    sock.close()
    print "Socket closed"
    break