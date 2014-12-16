#!/usr/bin/env python
import shutil
import os
import re
from xml.dom import minidom
from subprocess import check_call
import json
import sys

paren_remover = re.compile('\(.+?\)')

keywords = set(["General.",
                "Natural History.",
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
                if "</tr>" in line or "</td>" in line or "</span>" in line:
                    # We have reached the end.
                    break
                if "</body>" in line:
                    continue
                body.append(line.replace("<br>", ""))
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

    # Name is always in the first font node.
    name =  font_list[0].childNodes[0].nodeValue
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

    special_handling = set(["Relationship.", "General.", "Dose."])
    symptom_list = {}
    rename_pairs = {"Aggravation" : "Modalities", "Dose" : "dosage", "General" : "details"}
    for k, v in category_map.iteritems():
        value = "".join(v)
        value = value.replace("\n", "")
        if k in special_handling:
            symptoms = value
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
        if symptom_name in rename_pairs.keys():
            symptom_name =  rename_pairs[symptom_name]

        symptom_list[symptom_name] = symptoms
    remedy = {"name": name}

    # Some items move from symptoms to remedy main section.
    main_items = ["dosage", "details", "Relationship", "Natural History"]
    for item in main_items:
        item_value = symptom_list.get(item)
        if item_value:
            remedy[item] = item_value
            del symptom_list[item]

    # Modalities needs special casing.
    modalities = symptom_list.get("Modalities")
    if modalities:
        readable_modalities = []
        for mod in modalities:
            if mod.startswith("BETTER,"):
                mod = mod[7:]
                chunks = mod.split(",")
                for chunk in chunks:
                    readable_modalities.append("Better: %s" % chunk.strip())
            elif mod.startswith("WORSE,"):
                mod = mod[6:]
                chunks = mod.split(",")
                for chunk in chunks:
                    readable_modalities.append("Worse: %s" % chunk.strip())
        symptom_list["Modalities"] = readable_modalities


    # Rest of the items go to symptoms of the remedy.
    remedy["symptoms"] = symptom_list

    # Replace any spaces in the filename with underscores.
    with open(name.replace(" ", "_") + ".json", "w") as file_handle:
        file_handle.write(json.dumps(remedy, indent=2))

def main():
    file_name = sys.argv[1]
    process_one_remedy(file_name)
if __name__ == "__main__":
    main()