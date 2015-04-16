#!/usr/bin/env python
# ----------------------------------------------------------------------
# Numenta Platform for Intelligent Computing (NuPIC)
# Copyright (C) 2015, Numenta, Inc.  Unless you have an agreement
# with Numenta, Inc., for a separate license for this software code, the
# following terms and conditions apply:
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 3 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see http://www.gnu.org/licenses.
#
# http://numenta.org/licenses/
# ----------------------------------------------------------------------

#from nupic.encoders.sdrcategory import SDRCategoryEncoder



class MarioEncoders(object):
  
  def __init__(self, n, w, verbose):
    self.n = n
    self.w = w
    self.verbose = verbose
  
  
  def _transformBinaryVector(self, vector):
    transform = [i for i in range(len(vector)) if vector[i]==1]
    if self.verbose:
      print "vector: ", vector
      print "transform: ", transform
    assert sum(vector)==len(transform)
    return transform


#  def encodeSensorySequence(self, sequence, n, w):
#    SDRs = []
#    sdr_encoder = SDRCategoryEncoder(n, w, forced=True)
#    for s in sequence:
#      sdr = self._transformBinaryVector(sdr_encoder.encode(s))
#      if self.verbose:
#        print "s: ", s
#        print "sdr: ", sdr
#      SDRs.append(sdr)
#    return SDRs


  def encodeXYPositionSequences(self, sequences, ranges):
    # sequences[0] -> x, and sequences[1] -> y
    assert len(sequences[0])==len(sequences[1])
    x_section = float(self.n/2) / float(ranges[0][1]-ranges[0][0])
    y_section = float(self.n/2) / float(ranges[1][1]-ranges[1][0])
    SDRs = []
    for i in range(len(sequences[0])): # for each sequence
      assert len(sequences[0][i])==len(sequences[1][i])
      SDR = []
      for j in range(len(sequences[0][i])):
        # encode x
        x_sdr = range(int(sequences[0][i][j]*x_section),
                      int(sequences[0][i][j]*x_section + self.w/2))
        # encode y
        y_sdr = range(int(self.n/2 + sequences[1][i][j]*y_section),
                      int(self.n/2 + sequences[1][i][j]*y_section + self.w/2))
        SDR.append(x_sdr + y_sdr)
      SDRs.append(SDR)
    return SDRs


  def encodeMotorSequences(self, sequences):
    # Note the encoding may end up with <w ON bits due to rounding down
    SDRs = []
    for sequence in sequences:
      SDR = []
      for s in sequence:
        section = self.n/len(s)
        try:
          numON = self.w/sum(s)  # for every ON bit in s, this is the # of ON bits in the subsequent sdr
        except ZeroDivisionError:
          numON = self.w
        idx = [i*section for i in xrange(len(s)) if s[i]==1]
        if self.verbose:
          print "s: ", s
          print "individual motor sdr: ", idx
        SDR.append(idx)
      if self.verbose:
        print "sequence SDR: ", SDR
      SDRs.append(SDR)
    return SDRs
