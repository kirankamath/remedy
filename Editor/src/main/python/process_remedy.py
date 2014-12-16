#!/usr/bin/env python
import shutil
import os
import re
from xml.dom import minidom
from subprocess import check_call
import json
import sys

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

def process_one_remedy(file_name):
    with open(file_name, "r") as input_handle:
        lines = input_handle.readlines()
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

    file_name = os.path.basename(file_name)
    with open(file_name+".filtered", "w") as file_handle:
        file_handle.write("\n".join(body))

    check_call("xmllint --html %s.filtered > %s.xhtml" % (file_name, file_name), shell=True)

    xmldoc = minidom.parse(file_name+".xhtml")
    os.remove(file_name+".filtered")
    os.remove(file_name+".xhtml")

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

    special_handling = set(["Relationship.", "General."])
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
        if symptom_name == "Aggravation":
            symptom_name = "Modalities"
        symptom_list[symptom_name] = symptoms
    print json.dumps(symptom_list, indent=2)

def main():
    file_name = sys.argv[1]
if __name__ == "__main__":
    main()