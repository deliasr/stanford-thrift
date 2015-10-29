#!/usr/bin/env python
# -*- coding: utf-8 -*-


from corenlp import StanfordCoreNLP
from corenlp.ttypes import *
from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol

#from bs4 import UnicodeDammit
#import re
import sys
from ftfy import fix_text

reload(sys)
sys.setdefaultencoding('utf-8')


# get command line arguments
args = sys.argv[1:]
if len(args) != 3:
    sys.stderr.write('Usage: tokenizer_client.py <server> <port> '
    '<input-file>\n')
    sys.exit(2)
else:
    server = args[0]
    port = int(args[1])


tokenized_sentences = ["Barack Hussein Obama II is the 44th and current President of the United States , in office since 2009 .",
                       u"He is the first African American to hold the office .",
                       u"Born in Honolulu , Hawaii , Obama is a graduate of Columbia University and Harvard Law School , where he was president of the Harvard Law Review .",
                       u"He was a community organizer in Chicago before earning his law degree .",
                       u"He worked as a civil rights attorney in Chicago and taught constitutional law at the University of Chicago Law School from 1992 to 2004 .",
                       u"He served three terms representing the 13th District in the Illinois Senate from 1997 to 2004 , running unsuccessfully for the United States House of Representatives in 2000 .",
                       "Procter &amp; Gamble, one of Warren Buffet's "
                       "mainstays, has been on a roll: http://t.co/AyWYR4vAMD $PG",
                       "Procter &amp; Gamble innovation focusing on personal "
                       "care and home products via http://t.co/62Dw970FTJ",
                       "$NUGT Reviews Updated Saturday, January 10, "
                       "2015 05:11:22 AM $FSLR $ECIG $PG $DRYS http://t.co/tfgDyq6nek"]

ts1 = ["Here's The Extreme Productivity Philosophy Used By Facebook" \
       " And PayPal. Specially useful in these distracting times. \n" \
       "http://t.co/2yisx9Wk1P.",
       "Here's The Extreme Productivity Philosophy Used By Facebook" \
       " And PayPal. Specially useful in these distracting times. " \
       "@stuff."]

transport = TSocket.TSocket(server, port)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)
client = StanfordCoreNLP.Client(protocol)

transport.open()

try:
    '''
    for sentence in ts1:
        result = client.untokenize_sentence(sentence.split(" "))
        print result
        result = client.tokenize_text(result)
        print result
        print
    '''
    
    with open(args[2], 'r') as f:
        
        for line in f:
            line = line.strip()
            print 'Input: ', line
            
            line_unicode = line.decode('utf-8')

            result = client.tokenize_text(line_unicode)
            tokens = []
            for elem in result:
                tokens += elem
            tokens_str = ', '.join(tokens)
            print 'Output: ', tokens_str.encode('utf-8')
            print

except Exception as e:
    print e

transport.close()


