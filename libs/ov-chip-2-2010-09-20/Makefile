## 
## OV-chip 2.0 project
## 
## Digital Security (DS) group at Radboud Universiteit Nijmegen
## 
## Copyright (C) 2008, 2009
## 
## This program is free software; you can redistribute it and/or
## modify it under the terms of the GNU General Public License as
## published by the Free Software Foundation; either version 2 of
## the License, or (at your option) any later version.
## 
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
## General Public License in file COPYING in this or one of the
## parent directories for more details.
## 
## Created 2.12.08 by Hendrik
## 
## top level makefile
## 
## $Id: Makefile,v 1.29 2010-09-20 13:55:34 tews Exp $


# load configuration
-include ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


#############################################################################
## build everything in all subdirectories
##

ALL_SUBDIRS:=tools emulator misc bignat test front gui doc

.PHONY: all
all:
	for d in $(ALL_SUBDIRS) ; do $(MAKE) -C $$d all || exit; done
	$(MAKE) doc



#############################################################################
## build everything in core subdirectories
##

.PHONY: core
core:
	for d in $(ALL_SUBDIRS) ; do $(MAKE) -C $$d core || exit; done
	$(MAKE) doc


#############################################################################
## run cpp for a windows-zip release
##

.PHONY: windows-cpp
windows-cpp:
	for d in $(ALL_SUBDIRS) ; do $(MAKE) -C $$d windows-cpp || exit; done


#############################################################################
## build everything in selected subdirectories
##

TEST_SUBDIRS:=tools emulator bignat test front
# XXXX take gui in again

.PHONY: test
test:
	for d in $(TEST_SUBDIRS) ; do $(MAKE) -C $$d all || exit; done


#############################################################################
## doc
##

BUILD:=_doc_build_dir

OVDOCDIRS:=util bignat test front gui


.PHONY: doc
doc: $(BUILD)/directory-exists
	for d in $(OVDOCDIRS) ; do \
		$(MAKE) -C $$d ../$(BUILD)/ds/ov2/$$d/doc_sources_copied ; \
	done
	javadoc -sourcepath $(BUILD) -d doc/generated \
		-classpath $(JCKIT221)/lib/api.jar:$(GPM) \
		-overview doc/overview.html \
		-linkoffline 'http://java.sun.com/javase/6/docs/api/' \
								doc/java-6 \
		-linkoffline 'http://java.sun.com/javase/6/docs/jre/api/security/smartcardio/spec/' \
							   doc/smartcardio \
		-linkoffline 'http://www.cs.ru.nl/~woj/javacardapi221/' \
							doc/javacard-2-2-1 \
		-private -version -author -use \
		-tag CPP:a:"CPP Preprocessing" \
		-tag environment:t:"Execution Environment:" \
		-tag todo:a:"To Do:" \
		-tag author \
		-tag version \
		-tag commitdate:t:"Last Commit:" \
		ds.ov2.util ds.ov2.bignat ds.ov2.test \
		ds.ov2.front ds.ov2.gui
	(cd doc; ./doccount.perl > count)
	head -10 doc/count

#-linkoffline 'http://java.sun.com/javase/6/docs/api/' \ doc/java-6 \
#-link 'http://java.sun.com/javase/6/docs/api/' \
#-link /home/tews/java/java_card_kit-2_2_1/doc/en/api/ \
#-linkoffline 'http://www.cs.ru.nl/~woj/javacardapi221/' doc/javacard-2-2-1 \


TOCLEAN+=$(BUILD)
$(BUILD)/directory-exists:
	for d in $(OVDOCDIRS) ; do \
		mkdir -p $(BUILD)/ds/ov2/$$d ; \
	done
	touch $@


#############################################################################
## misc
##

.PHONY: guide
guide:
	$(MAKE) -C doc guide

LOCSUBDIRS:=$(OVDOCDIRS) emulator

loc:
	find $(LOCSUBDIRS) -name \*.java -o -name \*.id \
	| xargs cat | grep -v '^ *//' | grep -v '^ *$$' \
	| grep -v '^ *\*' | grep -v '^ */\*\* *$$' \
	| wc

docloc:
	find $(LOCSUBDIRS) -name \*.java -o -name \*.id \
	| xargs cat | egrep '(^ */\*\*)|(^ *\*)' \
	| wc doc/overview.html -

idlloc:
	find . -name _java_build_dir | xargs rm -rf
	find . -name _doc_build_dir | xargs rm -rf
	find $(LOCSUBDIRS) -name \*description.java -o -name \*stubs.java \
	| xargs cat | wc


#############################################################################
## clean
##

CLEAN_SUBDIRS:=bignat emulator front misc test \
	tools util gui

cleandoc:
	rm -rf $(BUILD) doc/generated

clean: cleandoc
	rm -rf $(TOCLEAN)
	for d in $(CLEAN_SUBDIRS) ; do $(MAKE) -C $$d clean; done


