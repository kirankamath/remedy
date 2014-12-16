#!/usr/bin/env python

import os
import re
from xml.dom import minidom
from subprocess import check_call
import json

paren_remover = re.compile('\(.+?\)')

links = set()

with open("links.html", "r") as file_handle:
    for line in file_handle:
        chunks = line.split()
        for chunk in chunks:
            if chunk.startswith("href"):
                subchunks = chunk.split('"')
                links.add(subchunks[1])
links = list(links)
links.sort()
print "Number of links %s" % len(links)

def download_one_file(link):
    import urllib2
    response = urllib2.urlopen(link)
    html = response.read()
    file_name = os.path.basename(link)
    assert file_name.endswith(".php")
    file_name = file_name[:-4] + ".orig"
    with open(file_name, "w") as file_handle:
        file_handle.write(html)

chosen_link = None
for link in links:
    print "Downloading %s" % link
    download_one_file(link)
    if link.endswith("kalibic.php"):
        chosen_link = link
