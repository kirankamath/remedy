#!/usr/bin/env python

import os
import sys
import json
import sqlite3

file_name = "%s" % __file__

db_name = "remedy"
src_root = os.path.abspath(os.path.join(os.path.dirname(file_name), "../" * 4))
database_dir = os.path.join(src_root, "assets", "databases")
catalog_dir = os.path.join(src_root, "Editor", "remedyList")

def create_tables(c):
    # Create remedy table.
    c.execute("create table remedy (name text PRIMARY KEY, details text, dosage text)")
    c.execute("create index remedy_name_index on remedy (name)")

    # Create category table.
    c.execute("create table category (category text PRIMARY KEY)")

    # Create symptom_list table.
    c.execute("create table symptom_list (cat_id integer, symptom text, " +
              " foreign key(cat_id) references category(rowid), " +
              " primary key(cat_id, symptom))")

    # Create remedy_symptom table.
    c.execute(" create table remedy_symptom (remedy_id integer, symptom_id integer, " +
              " primary key(remedy_id, symptom_id) " +
              " foreign key(remedy_id) references remedy(id), " +
              " foreign key(symptom_id) references symptom(id) )")

import sqlite3
conn = sqlite3.connect(os.path.join(database_dir, '%s.db' % db_name))
c = conn.cursor()
create_tables(c)

for f in os.listdir(catalog_dir):
    print "Processing %s" % f
    json_file = os.path.join(catalog_dir, f)
    with open(json_file) as fd:
        remedy = json.load(fd)
        c.execute("insert into remedy (name, details, dosage) values (?, ?, ?)",
                  (remedy["name"], remedy["details"], remedy["dosage"]))
        remedy_id = c.lastrowid
        symptoms = remedy["symptoms"]
        for category, desc_list in symptoms.iteritems():
            c.execute("select rowid from category where category = ?", (category,))
            row = c.fetchone()
            if row != None:
                category_id = row[0]
            else:
                c.execute("insert into category (category) values (?)", (category,))
                category_id = c.lastrowid
            for desc in desc_list:
                # See if the desc already exists.
                c.execute("select rowid from symptom_list where symptom = ? and cat_id = ?", (desc, category_id))
                row = c.fetchone()
                if row != None:
                    symptom_id = row[0]
                else:
                    c.execute("insert into symptom_list (cat_id, symptom) values (?, ?)",
                              (category_id, desc))
                    symptom_id = c.lastrowid

                # Add an entry for the symptom to this remedy.
                c.execute("insert into remedy_symptom (remedy_id, symptom_id) values (?, ?)",
                          (remedy_id, symptom_id))

conn.commit()

# Zip up the DB.
full_db_name = os.path.join(database_dir, db_name)
os.system("rm %s.zip" % full_db_name)
os.system("zip %s.zip %s.db" % (full_db_name, full_db_name))
os.system("rm %s.db" % full_db_name)
