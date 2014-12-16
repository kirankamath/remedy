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

chosen_link = None
for link in links:
    if link.endswith("kalibic.php"):
        chosen_link = link


keywords = set(["General.", "Natural History.", "Stomach.",
                "Sleep",
        "Head.",
        "Mind.",
        "Face.",
        "Eyes.",
        "Ears.",
        "Nose.",
        "Mouth.",
        "Tongue.",
        "Throat.",
        "Respiratory.",
        "Chest.",
        "Heart.",
        "Back.",
        "Stomach.",
        "Abdomen.",
        "Urine.",
        "Genetalia.",
        "Rectum.",
        "Stool.",
        "Skin.",
        "Extremities.",
        "Fever.",
        "Female.",
        "Male.",
        "Modalities.",
        "Sexual.",
        "Dose.",
        "Relationship.",
        "Aggravation."])

def process_one_remedy(link):
    print "Processing link %s" % link
    import urllib2
    response = urllib2.urlopen(link)
    html = response.read()
    lines = html.split("\n")
    begin = False
    body = []
    for line in lines:
        if begin:
            if "</tr>" in line:
                # We have reached the end.
                break
            if "</body>" in line or "</td>" in line:
                continue
            body.append(line)
        elif "Materia Medica (Boericke)" in line:
            begin = True
            continue

    file_name = os.path.basename(link)
    with open(file_name, "w") as file_handle:
        file_handle.write("\n".join(body))

    check_call("xmllint --html %s > %s.out" % (file_name, file_name), shell=True)

    xmldoc = minidom.parse(file_name+".out")
    font_list = xmldoc.getElementsByTagName("font")
    data = []
    for font in font_list:
        if font.childNodes:
            child = font.childNodes[0]
            if isinstance(child, minidom.Text):
                data.append(str(child.nodeValue).strip())
            else:
                assert isinstance(child, minidom.Element), "Child is %s" % child
                data.append(str(child.childNodes[0].nodeValue).strip())

    category_map = {}
    current_list = None
    for element in data:
        if element in keywords:
            current_list = []
            category_map[element] = current_list
        else:
            if current_list != None:
                current_list.append(element)

    special_handling = set(["Relationship."])
    symptom_list = {}
    for k, v in category_map.iteritems():
        value = "".join(v)
        value = value.replace("\n", "")
        if k in special_handling:
            symptoms = [value]
        else:
            # Remove any content within paren.
            value = paren_remover.sub('', value)
            values = value.split(".")
            symptoms = []
            for x in values:
                x = x.strip()
                if not x:
                    continue
                symptoms.append(x)

        symptom_name = k[:-1]
        symptom_list[symptom_name] = symptoms
    print json.dumps(symptom_list, indent=2)


process_one_remedy(chosen_link)
