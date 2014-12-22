#!/usr/bin/env python

from corenlp import StanfordCoreNLP
from corenlp.ttypes import *
from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol

#from bs4 import UnicodeDammit
#import re
import sys


# get command line arguments
args = sys.argv[1:]
if len(args) != 2:
    sys.stderr.write('Usage: tagger_client.py <server> <port>\n')
    sys.exit(2)
else:
    server = args[0]
    port = int(args[1])


tokenized_sentences = ["Barack Hussein Obama II is the 44th and current President of the United States , in office since 2009 .",
                       u"He is the first African American to hold the office .",
                       u"Born in Honolulu , Hawaii , Obama is a graduate of Columbia University and Harvard Law School , where he was president of the Harvard Law Review .",
                       u"He was a community organizer in Chicago before earning his law degree .",
                       u"He worked as a civil rights attorney in Chicago and taught constitutional law at the University of Chicago Law School from 1992 to 2004 .",
                       u"He served three terms representing the 13th District in the Illinois Senate from 1997 to 2004 , running unsuccessfully for the United States House of Representatives in 2000 ."]

arbitrary_text = u"Barack Hussein Obama II is the 44th and current President of the United States, in office since 2009.  He is the first African American to hold the office.  Born in Honolulu, Hawaii, Obama is a graduate of Columbia University and Harvard Law School, where he was president of the Harvard Law Review.  He was a community organizer in Chicago before earning his law degree.  He worked as a civil rights attorney in Chicago and taught constitutional law at the University of Chicago Law School from 1992 to 2004.  He served three terms representing the 13th District in the Illinois Senate from 1997 to 2004, running unsuccessfully for the United States House of Representatives in 2000."

p_tokenized_sentences = [u"What_A a_B day_C"]

transport = TSocket.TSocket(server, port)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)
client = StanfordCoreNLP.Client(protocol)

transport.open()

try:
    result = client.tag_text(arbitrary_text)
    for r in result:
        print r
        print
        in_place = " ".join(tagged.token + "/" + tagged.tag for tagged in r)
        print in_place
        print
    print
    for sentence in tokenized_sentences:
        result = client.tag_tokenized_sentence(sentence.split(" "))
        print result
        print

    for sentence in p_tokenized_sentences:
        print sentence
        result = client.tag_partially_tagged_tokenized_sentence(sentence, "_")
        print result

except Exception as e:
    print e

transport.close()
