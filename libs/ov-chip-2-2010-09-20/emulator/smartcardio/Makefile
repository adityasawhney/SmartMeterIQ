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
## Created 4.10.08 by Hendrik
## 
## emulator smartcardio interface test Makefile
## 
## $Id: Makefile,v 1.9 2010-09-20 13:55:35 tews Exp $


# load configuration
include ../../ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


# default build target
# if you don't like it, put one in LocalMakefile, eg:
# myfirsttarget: xxx
all: test-emulator


.PHONY: windows-cpp
windows-cpp: _jar_loopback_copy


APDUIOJAR:=$(JCKIT222)/lib/apduio.jar


#############################################################################
## Emulator connection jar
##

TOCLEAN+= ds
EM_PACK_DIR:=ds/javacard/emulator/smartcardio

build-jar: emulatorio.jar

EMULATOR_CONNECTION_SOURCES:= \
	DS_provider.java \
	Emulator_connection_spi.java \
	Emulator_connection_terminals.java \
	Emulator_connection_terminal.java \
	Emulator_connection_card.java \
	Emulator_connection_channel.java

EMULATOR_CONNECTION_PATHS:=\
	$(addprefix $(EM_PACK_DIR)/, $(EMULATOR_CONNECTION_SOURCES))

TOCLEAN+= emulatorio.jar
emulatorio.jar: _jar_compiled
	jar cf emulatorio.jar \
		$(patsubst %.java,%.class,$(EMULATOR_CONNECTION_PATHS))


TOCLEAN+=_jar_compiled
_jar_compiled: _jar_loopback_exists $(EMULATOR_CONNECTION_PATHS)
	javac -classpath $(APDUIOJAR) -Xlint $(EMULATOR_CONNECTION_PATHS)
	touch $@

TOCLEAN+= _jar_loopback_exists
_jar_loopback_exists:
	mkdir -p $(dir $(EM_PACK_DIR))
	ln -s `pwd` $(EM_PACK_DIR)
	touch $@


.PHONY: _jar_loopback_copy
_jar_loopback_copy:
	mkdir -p $(EM_PACK_DIR)
	cp $(EMULATOR_CONNECTION_SOURCES) $(EM_PACK_DIR)


#############################################################################
## Test Emulator connection
##

test-emulator: _test_compiled

TEST_PACK_DIR:=ds/javacard/emulator/test

TEST_HOST_SOURCES:=\
	Test_emulator.java

TEST_HOST_PATHS:=$(addprefix $(TEST_PACK_DIR)/, $(TEST_HOST_SOURCES))

TOCLEAN+= _test_compiled
_test_compiled: emulatorio.jar _test_loopback_exists $(TEST_HOST_PATHS)
	javac -classpath $(APDUIOJAR):emulatorio.jar -Xlint $(TEST_HOST_PATHS) 
	touch $@

TOCLEAN+= _test_loopback_exists
_test_loopback_exists:
	mkdir -p $(dir $(TEST_PACK_DIR))
	ln -s `pwd` $(TEST_PACK_DIR)
	touch $@


#############################################################################
## Misc
##

tags: TAGS

.PHONY: TAGS
TAGS:
	etags --members *java


.PHONY: clean
clean:
	rm -f *.class
	rm -rf $(TOCLEAN)
