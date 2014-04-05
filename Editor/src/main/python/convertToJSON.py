#!/usr/bin/env python

import sys
import json

in_file_name = sys.argv[1]
out_file_name = sys.argv[2]

print "Reading from file %s and writing to %s" % (in_file_name, out_file_name)

dosage = ""
details = ""
category_map = {}
with open(in_file_name) as in_file:
    lines = in_file.readlines()
    for line in lines:
        line = line.strip()
        if len(line) == 0:
            continue
        if line.startswith("#"):
            continue
        if line.startswith("dosage"):
            dosage = line.split("#")[-1]
            continue
        if line.startswith("details"):
            details = line.split("#")[-1]
            continue
        chunks = line.split("#")
        assert len(chunks) == 2, "Found [%s], chunks = %s" % (line, chunks)
        category = chunks[0]
        symptom = chunks[1]
        
        if category not in category_map:
            category_map[category] = []
        category_map[category].append(symptom)

remedy = {}
remedy["name"] = in_file_name
remedy["details"] = details
remedy["dosage"] = dosage
remedy["symptoms"] = {}
for k,v in category_map.iteritems():
    remedy["symptoms"][k] = v
    
with open(out_file_name, "w") as out_file:
    out_file.write(json.dumps(remedy, indent=2))
