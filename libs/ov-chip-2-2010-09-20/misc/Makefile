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
## Created 10.7.08 by Hendrik
## 
## Makefile for building stuff in subdir test_applet
## 
## $Id: Makefile,v 1.20 2010-09-20 13:55:35 tews Exp $


# The jar file containing the provider and TerminalFactory for
# connecting to SUN emulators.
EMULATORIO:=../emulator/smartcardio/emulatorio.jar

# The jar file containing the provider and TerminalFactory for
# connecting to JCOP emulator.
JCOPIO:=../emulator/jcop/jcopio.jar

# load configuration
include ../ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


JC220CP:=$(JCKIT220)/lib/api.jar
JC220EP:=$(JCKIT220)/api_export_files

JC221CP:=$(JCKIT221)/lib/api.jar
JC221EP:=$(JCKIT221)/api_export_files

JC222CP:=$(JCKIT222)/lib/api.jar
JC222EP:=$(JCKIT222)/api_export_files


ifeq ($(strip $(JCKIT222)),)
JCKIT:=$(JCKIT221)
else
JCKIT:=$(JCKIT222)
endif


# default build target
# if you don't like it, put one in LocalMakefile, eg:
# myfirsttarget: xxx
.PHONY: all
all: core test-jc222 

.PHONY: all-int
all-int: test-int Int_host.class int_host

.PHONY: core
core: test-rsa host test-sha sha_host test-mem \
	Host.class SHA_host.class Identify.class identify \
	Mem_host.class mem_host


.PHONY: windows-cpp
windows-cpp:
	echo nothing to do here


# The name of the build dir is a bit more complicated now.
# That makes collissions more unlikely and simplifies the 
# related emacs lisp code.
BUILD:=_java_build_dir

#############################################################################
##
## Generic Host driver
##
#############################################################################


Host.class: Host.java
	javac -classpath $(JC221CP):$(EMULATORIO):$(JCOPIO) -Xlint Host.java

TOCLEAN+=host
host: host.in
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT222=.*|JCKIT222=$(JCKIT222)|' \
		< $< > $@
	chmod +x $@

#############################################################################
##
## SHA Host driver
##
#############################################################################


SHA_host.class: SHA_host.java Host.java
	javac -classpath $(JC221CP):$(EMULATORIO):$(JCOPIO) -Xlint \
		Host.java SHA_host.java

TOCLEAN+=sha_host
sha_host: sha_host.in
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT222=.*|JCKIT222=$(JCKIT222)|' \
		< $< > $@
	chmod +x $@


#############################################################################
##
## MEM Host driver
##
#############################################################################


Mem_host.class: Mem_host.java Host.java
	javac -classpath $(JC221CP):$(EMULATORIO):$(JCOPIO) -Xlint \
		Host.java Mem_host.java

TOCLEAN+=mem_host
mem_host: mem_host.in
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT222=.*|JCKIT222=$(JCKIT222)|' \
		< $< > $@
	chmod +x $@


#############################################################################
##
## Identify card application
##
#############################################################################


Identify.class: Identify.java
	javac -classpath $(JC221CP):$(EMULATORIO):$(JCOPIO) -Xlint Identify.java

TOCLEAN+=identify
identify: identify.in
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT222=.*|JCKIT222=$(JCKIT222)|' \
		< $< > $@
	chmod +x $@


#############################################################################
##
## SHA Test Applet
##
#############################################################################

sha-applet: test-sha
test-sha: test_sha/javacard/test_sha.cap
sha-eeprom: test-sha-eeprom
test-sha-eeprom: test_sha/javacard/eeprom.created


SHA_TEST_APPLET_ID_NAME:=sha_test.app

# SHA_TEST_PACKAGE_ID = 0x73:0x68:0x61:0x5f:0x74:0x65:0x73:0x74
# SHA_TEST_APPLET_ID  = 0x73:0x68:0x61:0x5f:0x74:0x65:0x73:0x74:0x2e:0x61:0x70:0x70

SHA_TEST_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(SHA_TEST_APPLET_ID_NAME))
SHA_TEST_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(SHA_TEST_APPLET_ID_NAME))
SHA_TEST_PACKAGE_VERSION:=1.0


test_sha/Test.class: test_sha/Test.java
	javac -g -source 1.2 -target 1.2 -classpath $(JC221CP) -Xlint $^


TOCLEAN+= test_sha/javacard
test_sha/javacard/test_sha.cap: test_sha/Test.class \
		../tools/Applet_id.class 
	@echo SHA_TEST_PACKAGE_ID = $(SHA_TEST_PACKAGE_ID)
	@echo 'SHA_TEST_APPLET_ID  =' $(SHA_TEST_APPLET_ID)
	$(JCKIT221)/bin/converter \
		-out EXP JCA CAP \
		-exportpath $(JC221EP) \
		-applet $(SHA_TEST_APPLET_ID) test_sha.Test \
		test_sha $(SHA_TEST_PACKAGE_ID) $(SHA_TEST_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors.sh


# NOTE!!! cref stuff only works with JCKIT222 because the emulatorio lib
# uses JCKIT222 and can only talk to the 2.2.2 cref!

TOCLEAN+=test_sha/javacard/eeprom.created
test_sha/javacard/eeprom.created: \
		test_sha/javacard/eeprom.loaded \
		test_sha/create.apduscript
	$(JCKIT222)/bin/cref -p 8990 -i $< -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 test_sha/create.apduscript


#############################################################################
##
## RSA Test Applet
##
#############################################################################

rsa-applet: test-rsa
test-rsa: test_rsa/javacard/test_rsa.cap
rsa-eeprom: test-rsa-eeprom
test-rsa-eeprom: test_rsa/javacard/eeprom.created


RSA_TEST_APPLET_ID_NAME:=rsa_test.app

# RSA_TEST_PACKAGE_ID = 0x72:0x73:0x61:0x5f:0x74:0x65:0x73:0x74
# RSA_TEST_APPLET_ID  = 0x72:0x73:0x61:0x5f:0x74:0x65:0x73:0x74:0x2e:0x61:0x70:0x70

RSA_TEST_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(RSA_TEST_APPLET_ID_NAME))
RSA_TEST_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(RSA_TEST_APPLET_ID_NAME))
RSA_TEST_PACKAGE_VERSION:=1.0


test_rsa/Test.class: test_rsa/Test.java
	javac -g -source 1.2 -target 1.2 -classpath $(JC221CP) -Xlint $^


TOCLEAN+= test_rsa/javacard
test_rsa/javacard/test_rsa.cap: test_rsa/Test.class \
		../tools/Applet_id.class 
	@echo RSA_TEST_PACKAGE_ID = $(RSA_TEST_PACKAGE_ID)
	@echo 'RSA_TEST_APPLET_ID  =' $(RSA_TEST_APPLET_ID)
	$(JCKIT222)/bin/converter \
		-out EXP JCA CAP \
		-exportpath $(JC221EP) \
		-applet $(RSA_TEST_APPLET_ID) test_rsa.Test \
		test_rsa $(RSA_TEST_PACKAGE_ID) $(RSA_TEST_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors.sh


# NOTE!!! cref stuff only works with JCKIT222 because the emulatorio lib
# uses JCKIT222 and can only talk to the 2.2.2 cref!

TOCLEAN+=test_rsa/javacard/eeprom.created
test_rsa/javacard/eeprom.created: \
		test_rsa/javacard/eeprom.loaded \
		test_rsa/create.apduscript
	$(JCKIT222)/bin/cref -p 8990 -i $< -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 test_rsa/create.apduscript


#############################################################################
##
## JC222 Test Applet
##
#############################################################################

jc222-applet: test-jc222
test-jc222: test_jc222/javacard/test_jc222.cap
jc222-eeprom: test-jc222-eeprom
test-jc222-eeprom: test_jc222/javacard/eeprom.created


JC222_TEST_APPLET_ID_NAME:=jc222_test.app

#JC222_TEST_PACKAGE_ID = 0x6a:0x63:0x32:0x32:0x32:0x5f:0x74:0x65:0x73:0x74
#JC222_TEST_APPLET_ID  = 0x6a:0x63:0x32:0x32:0x32:0x5f:0x74:0x65:0x73:0x74:0x2e:0x61:0x70:0x70

JC222_TEST_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(JC222_TEST_APPLET_ID_NAME))
JC222_TEST_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(JC222_TEST_APPLET_ID_NAME))
JC222_TEST_PACKAGE_VERSION:=1.0


test_jc222/Test.class: test_jc222/Test.java
	javac -g -target 5 -classpath $(JC222CP) -Xlint $^


TOCLEAN+= test_jc222/javacard
test_jc222/javacard/test_jc222.cap: test_jc222/Test.class \
		../tools/Applet_id.class 
	@echo JC222_TEST_PACKAGE_ID = $(JC222_TEST_PACKAGE_ID)
	@echo 'JC222_TEST_APPLET_ID  =' $(JC222_TEST_APPLET_ID)
	$(JCKIT222)/bin/converter \
		-out EXP JCA CAP \
		-exportpath $(JC222EP) \
		-applet $(JC222_TEST_APPLET_ID) test_jc222.Test \
		test_jc222 $(JC222_TEST_PACKAGE_ID) $(JC222_TEST_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors.sh


TOCLEAN+=test_jc222/javacard/eeprom.created
test_jc222/javacard/eeprom.created: \
		test_jc222/javacard/eeprom.loaded \
		test_jc222/create.apduscript
	$(JCKIT222)/bin/cref -p 8990 -i $< -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 test_jc222/create.apduscript




#############################################################################
##
## MEM Test Applet
##
#############################################################################

mem-applet: test-mem
test-mem: test_mem/javacard/test_mem.cap
mem-eeprom: test-mem-eeprom
test-mem-eeprom: test_mem/javacard/eeprom.created


MEM_TEST_APPLET_ID_NAME:=mem_test.app

# MEM_TEST_PACKAGE_ID = 0x6d:0x65:0x6d:0x5f:0x74:0x65:0x73:0x74
# MEM_TEST_APPLET_ID  = 0x6d:0x65:0x6d:0x5f:0x74:0x65:0x73:0x74:0x2e:0x61:0x70:0x70

MEM_TEST_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(MEM_TEST_APPLET_ID_NAME))
MEM_TEST_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(MEM_TEST_APPLET_ID_NAME))
MEM_TEST_PACKAGE_VERSION:=1.0


test_mem/Test.class: test_mem/Test.java
	javac -g -source 1.2 -target 1.2 -classpath $(JC221CP) -Xlint $^


TOCLEAN+= test_mem/javacard
test_mem/javacard/test_mem.cap: test_mem/Test.class \
		../tools/Applet_id.class 
	@echo MEM_TEST_PACKAGE_ID = $(MEM_TEST_PACKAGE_ID)
	@echo 'MEM_TEST_APPLET_ID  =' $(MEM_TEST_APPLET_ID)
	$(JCKIT221)/bin/converter \
		-out EXP JCA CAP \
		-exportpath $(JC221EP) \
		-applet $(MEM_TEST_APPLET_ID) test_mem.Test \
		test_mem $(MEM_TEST_PACKAGE_ID) $(MEM_TEST_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors.sh


# NOTE!!! cref stuff only works with JCKIT222 because the emulatorio lib
# uses JCKIT222 and can only talk to the 2.2.2 cref!

TOCLEAN+=test_mem/javacard/eeprom.created
test_mem/javacard/eeprom.created: \
		test_mem/javacard/eeprom.loaded \
		test_mem/create.apduscript
	$(JCKIT222)/bin/cref -p 8990 -i $< -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 test_mem/create.apduscript


#############################################################################
##
## Int Host driver
##
#############################################################################


Int_host.class: Int_host.java Host.java
	javac -classpath $(JC221CP):$(EMULATORIO):$(JCOPIO) -Xlint \
		Host.java Int_host.java

TOCLEAN+=int_host
int_host: int_host.in
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT222=.*|JCKIT222=$(JCKIT222)|' \
		< $< > $@
	chmod +x $@


#############################################################################
##
## Int Test Applet
##
#############################################################################

WITH_INT_BUILD=test_int/$(BUILD)/with_int
ONLY_SHORT_BUILD=test_int/$(BUILD)/only_short

int-applet: test-int
test-int: $(WITH_INT_BUILD)/test_int/javacard/test_int.cap \
		$(ONLY_SHORT_BUILD)/test_int/javacard/test_int.cap
int-eeprom: test-int-eeprom
test-int-eeprom: $(WITH_INT_BUILD)/test_int/javacard/eeprom.created \
			$(ONLY_SHORT_BUILD)/test_int/javacard/eeprom.created


INT_TEST_APPLET_ID_NAME:=int_test.app

#INT_TEST_PACKAGE_ID = 0x69:0x6e:0x74:0x5f:0x74:0x65:0x73:0x74
#INT_TEST_APPLET_ID  = 0x69:0x6e:0x74:0x5f:0x74:0x65:0x73:0x74:0x2e:0x61:0x70:0x70

INT_TEST_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(INT_TEST_APPLET_ID_NAME))
INT_TEST_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(INT_TEST_APPLET_ID_NAME))
INT_TEST_PACKAGE_VERSION:=1.0


TOCLEAN+= test_int/$(BUILD)
test_int/$(BUILD)/directory-exists:
	mkdir -p $(WITH_INT_BUILD)/test_int
	mkdir -p $(ONLY_SHORT_BUILD)/test_int
	touch $@


#############################################################################
#
# Applet with int
#

$(WITH_INT_BUILD)/test_int/%.prejava: test_int/%.java \
					 test_int/$(BUILD)/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DWITH_INT_SUPPORT $$(pwd)/$< >> $@

$(WITH_INT_BUILD)/test_int/Test.class: $(WITH_INT_BUILD)/test_int/Test.java
	javac -g  -source 1.2 -target 1.2 \
		-classpath $(WITH_INT_BUILD):$(JC220CP) -Xlint $^


$(WITH_INT_BUILD)/test_int/javacard/test_int.cap: \
		$(WITH_INT_BUILD)/test_int/Test.class \
		../tools/Applet_id.class 
	@echo INT_TEST_PACKAGE_ID = $(INT_TEST_PACKAGE_ID)
	@echo 'INT_TEST_APPLET_ID  =' $(INT_TEST_APPLET_ID)
	$(JCKIT221)/bin/converter \
		-i \
		-out EXP JCA CAP \
		-classdir $(WITH_INT_BUILD) \
		-exportpath $(JC220EP) \
		-applet $(INT_TEST_APPLET_ID) test_int.Test \
		test_int $(INT_TEST_PACKAGE_ID) $(INT_TEST_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors.sh -classdir $(WITH_INT_BUILD)


#############################################################################
#
# Applet with only short
#

$(ONLY_SHORT_BUILD)/test_int/%.prejava: test_int/%.java \
					 test_int/$(BUILD)/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . $$(pwd)/$< >> $@

$(ONLY_SHORT_BUILD)/test_int/Test.class: $(ONLY_SHORT_BUILD)/test_int/Test.java
	javac -g  -source 1.2 -target 1.2 \
		-classpath $(ONLY_SHORT_BUILD):$(JC221CP) -Xlint $^


$(ONLY_SHORT_BUILD)/test_int/javacard/test_int.cap: \
		$(ONLY_SHORT_BUILD)/test_int/Test.class \
		../tools/Applet_id.class 
	@echo INT_TEST_PACKAGE_ID = $(INT_TEST_PACKAGE_ID)
	@echo 'INT_TEST_APPLET_ID  =' $(INT_TEST_APPLET_ID)
	$(JCKIT221)/bin/converter \
		-i \
		-out EXP JCA CAP \
		-classdir $(ONLY_SHORT_BUILD) \
		-exportpath $(JC221EP) \
		-applet $(INT_TEST_APPLET_ID) test_int.Test \
		test_int $(INT_TEST_PACKAGE_ID) $(INT_TEST_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors.sh -classdir $(ONLY_SHORT_BUILD)


#############################################################################
#
# Final cref eeprom rules
#

# NOTE!!! cref stuff only works with JCKIT222 because the emulatorio lib
# uses JCKIT222 and can only talk to the 2.2.2 cref!

$(WITH_INT_BUILD)/test_int/javacard/eeprom.created: \
		$(WITH_INT_BUILD)/test_int/javacard/eeprom.loaded \
		test_int/create.apduscript
	$(JCKIT222)/bin/cref -p 8990 -i $< -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 test_int/create.apduscript


$(ONLY_SHORT_BUILD)/test_int/javacard/eeprom.created: \
		$(ONLY_SHORT_BUILD)/test_int/javacard/eeprom.loaded \
		test_int/create.apduscript
	$(JCKIT222)/bin/cref -p 8990 -i $< -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 test_int/create.apduscript




#############################################################################
##
## misc
##
#############################################################################

# NOTE!!! cref stuff only works with JCKIT222 because the emulatorio lib
# uses JCKIT222 and can only talk to the 2.2.2 cref!

%/apdu_prescript: %/test_int.cap
	$(JCKIT222)/bin/scriptgen $< -o $@


%/apdu_script: %/apdu_prescript
	echo 'powerup;' > $@
	echo '// Select the installer applet' >> $@
	echo '0x00 0xA4 0x04 0x00 0x09 0xa0 0x00 0x00 0x00 0x62 0x03 0x01 0x08 0x01 0x7F;' >> $@
	echo >> $@
	cat $< >> $@
	echo >> $@
	echo 'powerdown;' >> $@


%/eeprom.loaded: %/apdu_script
	$(JCKIT222)/bin/cref -p 8990 -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 $<


.PRECIOUS: %.java

%.java: %.prejava
	rm -f $@
	sed -e 's|^# |//# |' <$< >$@
	chmod -w $@


clean:
	rm -f *class */*class
	rm -rf $(TOCLEAN)

