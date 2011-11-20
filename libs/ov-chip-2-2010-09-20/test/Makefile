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
## testframe Makefile
## 
## $Id: Makefile,v 1.49 2010-09-20 13:55:35 tews Exp $


# load configuration
include ../ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


# default build target
# if you don't like it, put one in LocalMakefile, eg:
# myfirsttarget: xxx
.PHONY: all
all: applet test-host-driver test_host

.PHONY: core
core: all

.PHONY: windows-cpp
windows-cpp: applet-cpp test-host-driver-cpp


# The name of the build dir is a bit more complicated now.
# That makes collissions more unlikely and simplifies the 
# related emacs lisp code.
BUILD:=_java_build_dir

# The jar file containing the provider and TerminalFactory for
# connecting to the SUN emulators.
EMULATORIO:=../emulator/smartcardio/emulatorio.jar

# The jar file containing the provider and TerminalFactory for
# connecting to the jcop emulator.
JCOPIO:=../emulator/jcop/jcopio.jar

JCCP:=$(JCKIT221)/lib/api.jar
JCEP:=$(JCKIT221)/api_export_files


#############################################################################
## Protocol descriptions
##

TEST_IDL_SOURCES:=\
	Misc_protocols.id \
	Data_protocol.id \
	Bignat_protocols.id \
	Exponent_perf.id


TEST_IDL_DESCRIPTIONS:=$(TEST_IDL_SOURCES:.id=_description.java)
TEST_IDL_STUBS:=$(TEST_IDL_SOURCES:.id=_stubs.java)
TEST_IDL_TEST_STUBS:=$(TEST_IDL_SOURCES:.id=_test_stubs.java)

TOCLEAN+= $(TEST_IDL_DESCRIPTIONS) $(TEST_IDL_STUBS) $(TEST_IDL_TEST_STUBS)


.PRECIOUS: %_description.java 
.PRECIOUS: %_stubs.java
.PRECIOUS: %_test_stubs.java

%_description.java %_stubs.java %_test_stubs.java: %.id ../tools/idl.perl
	../tools/idl.perl $<

idl-files: $(TEST_IDL_DESCRIPTIONS) $(TEST_IDL_STUBS) $(TEST_IDL_TEST_STUBS)

# Additional dependencies for the include files. They are a bit 
# imprecise. Strictly, the *.prejava, generated from the *description.java
# depends on the *description_include.java. But this way is simpler.
Data_protocol_description.java: Data_protocol_description_include.java

#############################################################################
## Test Applet
##

APPLETBUILDDIR:=$(BUILD)/card/test/ov_test/javacard

applet: $(APPLETBUILDDIR)/ov_test.cap
applet-jcop: $(APPLETBUILDDIR)/applet-installed-jcop
applet-jcwde: $(APPLETBUILDDIR)/applet-installed-jcwde
applet-cref: $(APPLETBUILDDIR)/applet-installed-cref
applet-card: $(APPLETBUILDDIR)/applet-installed-card
applet-eeprom: $(APPLETBUILDDIR)/ov_test_eeprom.created

TEST_APPLET_ID_NAME:=ov_test.app

TEST_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(TEST_APPLET_ID_NAME))
TEST_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(TEST_APPLET_ID_NAME))
TEST_PACKAGE_VERSION:=1.0

#TEST_PACKAGE_ID = 0x6f:0x76:0x5f:0x74:0x65:0x73:0x74
#TEST_APPLET_ID  = 0x6f:0x76:0x5f:0x74:0x65:0x73:0x74:0x2e:0x61:0x70:0x70


TEST_APPLET_SOURCES:=\
	util/ov_test/APDU_Serializable.java \
	util/ov_test/Serializable_array.java \
	util/ov_test/Void_method.java \
	util/ov_test/Empty_void_method.java \
	util/ov_test/Response_status.java \
	util/ov_test/Misc.java \
	util/ov_test/Resizable_buffer.java \
	util/ov_test/APDU_boolean.java \
	util/ov_test/APDU_byte_array.java \
	util/ov_test/APDU_short.java \
	util/ov_test/APDU_short_array.java \
	util/ov_test/Installation_arguments.java \
	util/ov_test/Protocol_step.java \
	util/ov_test/Protocol.java \
	util/ov_test/Registered_protocols.java \
	util/ov_test/Card_protocol.java \
	util/ov_test/Protocol_applet.java \
	bignat/ov_test/Bignat.java \
	bignat/ov_test/Modulus.java \
	bignat/ov_test/Bignat_array.java \
	bignat/ov_test/RSA_exponent_interface.java \
	bignat/ov_test/RSA_exponent.java \
	bignat/ov_test/Vector.java \
	bignat/ov_test/Resize.java \
	test/ov_test/Data_protocol_card.java \
	test/ov_test/Performance_mult_card.java \
	test/ov_test/Exponent_perf_card.java \
	test/ov_test/Bignats.java \
	$(addprefix test/ov_test/, $(TEST_IDL_DESCRIPTIONS)) \
	test/ov_test/Test_protocols.java \
	test/ov_test/Test_applet.java


FULL_TEST_APPLET_SOURCES:=$(addprefix $(BUILD)/card/, $(TEST_APPLET_SOURCES))

# use -target 5 for 2.2.2
$(BUILD)/card/test/javac_compiled: $(FULL_TEST_APPLET_SOURCES)
	javac -g -source 1.2 -target 1.2 -classpath \
		$(JCCP):$(BUILD)/card/test -Xlint \
		$(BUILD)/card/test/ov_test/*.java
	touch $@


#.PRECIOUS: $(BUILD)/card/util/ov_test/%.prejava
#.PRECIOUS: $(BUILD)/card/bignat/ov_test/%.prejava
#.PRECIOUS: $(BUILD)/card/test/ov_test/%.prejava

$(BUILD)/card/util/ov_test/%.prejava: ../util/%.java config \
		$(BUILD)/card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_TEST_APPLET $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/card/bignat/ov_test/%.prejava: ../bignat/%.java config \
		$(BUILD)/card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_TEST_APPLET $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/card/test/ov_test/%.prejava: %.java config \
		$(BUILD)/card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_TEST_APPLET $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/card/directory-exists:
	mkdir -p $(BUILD)/card/test/ov_test
	(cd $(BUILD)/card; ln -s test util)
	(cd $(BUILD)/card; ln -s test bignat)
	touch $(BUILD)/card/directory-exists

.PHONY: applet-cpp
applet-cpp: $(FULL_TEST_APPLET_SOURCES)

$(APPLETBUILDDIR)/ov_test.cap: \
		$(BUILD)/card/test/javac_compiled \
		../tools/Applet_id.class
	@echo TEST_PACKAGE_ID = $(TEST_PACKAGE_ID)
	@echo 'TEST_APPLET_ID  =' $(TEST_APPLET_ID)
	$(JCKIT221)/bin/converter -classdir $(BUILD)/card/test \
		-out EXP JCA CAP \
		-exportpath $(JCEP):$(BUILD)/card/test \
		-applet $(TEST_APPLET_ID) ov_test.Test_applet \
		ov_test $(TEST_PACKAGE_ID) $(TEST_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors.sh -classdir $(BUILD)/card/test


.PHONY: verify-applet
verify-applet: $(APPLETBUILDDIR)/ov_test.cap
	$(JCKIT221)/bin/verifycap \
	   $(JCKIT221)/api_export_files/java/lang/javacard/lang.exp \
	   $(JCKIT221)/api_export_files/javacard/framework/javacard/framework.exp \
	   $(JCKIT221)/api_export_files/javacard/security/javacard/security.exp \
	   $(JCKIT221)/api_export_files/javacardx/crypto/javacard/crypto.exp \
	   $<


$(APPLETBUILDDIR)/ov_test_apdu_prescript: $(APPLETBUILDDIR)/ov_test.cap
	$(JCKIT221)/bin/scriptgen $< -o $@


$(APPLETBUILDDIR)/ov_test_apdu_script: $(APPLETBUILDDIR)/ov_test_apdu_prescript
	echo 'powerup;' > $@
	echo '// Select the installer applet' >> $@
	echo '0x00 0xA4 0x04 0x00 0x09 0xa0 0x00 0x00 0x00 0x62 0x03 0x01 0x08 0x01 0x7F;' >> $@
	echo >> $@
	cat $< >> $@
	echo >> $@


$(APPLETBUILDDIR)/ov_test_eeprom.loaded: $(APPLETBUILDDIR)/ov_test_apdu_script
	$(JCKIT222)/bin/cref -p 8990 -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 $<


$(APPLETBUILDDIR)/ov_test_eeprom.created: \
		$(APPLETBUILDDIR)/ov_test_eeprom.loaded create-cref.apduscript
	$(JCKIT222)/bin/cref -p 8990 -i $< -o $@ &
	$(JCKIT222)/bin/apdutool -p 8990 create-cref.apduscript


$(APPLETBUILDDIR)/applet-installed-cref: reload-cref emulator-cref \
			$(APPLETBUILDDIR)/ov_test_eeprom.created
	./reload-cref
	touch $@


$(APPLETBUILDDIR)/applet-installed-jcop: \
			$(APPLETBUILDDIR)/ov_test.cap \
			delete.patt install-test.patt
	patt -simulator delete.patt
	patt -simulator install-test.patt
	touch $@


$(APPLETBUILDDIR)/applet-installed-jcwde: \
			$(APPLETBUILDDIR)/ov_test.cap \
			reload-jcwde create-jcwde.apduscript
	./reload-jcwde
	touch $@


$(APPLETBUILDDIR)/applet-installed-card: \
			$(APPLETBUILDDIR)/ov_test.cap \
			delete.patt install-test.patt
	patt delete.patt
	patt install-test.patt
	touch $@


applet-cref-ping: $(APPLETBUILDDIR)/ov_test_eeprom.created pingcard.apduscript
	$(JCKIT221)/bin/cref -p 8990 -i $< &
	$(JCKIT221)/bin/apdutool -p 8990 pingcard.apduscript


#CLASSPATH=_java_build_dir/card/test
#export CLASSPATH

applet-jcwde-ping: $(BUILD)/card/test/javac_compiled jcwde.app
	CLASSPATH=_java_build_dir/card/test $(JCKIT221)/bin/jcwde \
		-p 8990 -verbose jcwde.app &
	sleep 0.1
	apdutool -p 8990 wde2.apduscript


#############################################################################
## Test Host
##

TEST_HOST_SOURCES:=\
	util/APDU_Serializable.java \
	util/Serializable_array.java \
	util/Misc.java \
	util/Misc_host.java \
	util/BigIntUtil.java \
	util/Resizable_buffer.java \
	util/APDU_boolean.java \
	util/APDU_byte_array.java \
	util/APDU_short.java \
	util/APDU_short_array.java \
	util/Card_terminal.java \
	util/Response_status.java \
	util/Response_apdu.java \
	util/Protocol_step.java \
	util/Protocol.java \
	util/Registered_protocols.java \
	util/Convert_serializable.java \
	util/Host_protocol.java \
	util/Security_parameter.java \
	util/PrintWriter_APDUListener.java \
	util/Reference.java \
	util/Commandline.java \
	util/Option.java \
	util/Bool_option.java \
	util/Int_option.java \
	util/Bool_int_option.java \
	util/String_option.java \
	util/BigInteger_inputs.java \
	util/BigInt_input_option.java \
	util/BigInt_hex_input_option.java \
	util/Value_option.java \
	util/Parse_commandline.java \
	bignat/Bignat.java \
	bignat/Modulus.java \
	bignat/Host_modulus.java \
	bignat/Bignat_array.java \
	bignat/RSA_exponent_interface.java \
	bignat/Vector.java \
	bignat/Resize.java \
	bignat/Host_vector.java \
	bignat/APDU_BigInteger.java \
	test/State.java \
	test/Data_protocol_host.java \
	test/Bignats.java \
	$(addprefix test/, $(TEST_IDL_DESCRIPTIONS)) \
	$(addprefix test/, $(TEST_IDL_STUBS)) \
	test/Misc_protocols_host.java \
	test/Test_protocols.java \
	test/Check_data_transmission.java \
	test/Card_performance.java \
	test/Performance_mult_host.java \
	test/Exponent_perf_host.java \
	test/Test_host.java

FULL_TEST_HOST_SOURCES:=$(addprefix $(BUILD)/test_host/ds/ov2/, \
				$(TEST_HOST_SOURCES))

test-host-driver: $(BUILD)/test_host/javac_compiled

$(BUILD)/test_host/javac_compiled: $(FULL_TEST_HOST_SOURCES)
	javac -classpath \
		$(BUILD)/test_host:$(JCCP):$(EMULATORIO):$(JCOPIO):$(GPM) \
		-Xlint \
		$(BUILD)/test_host/ds/ov2/*/*.java
	touch $@


$(BUILD)/test_host/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/test_host/ds/ov2/util
	mkdir -p $(BUILD)/test_host/ds/ov2/bignat
	mkdir -p $(BUILD)/test_host/ds/ov2/test
	touch $(BUILD)/test_host/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/test_host/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/test_host/ds/ov2/bignat/%.prejava
#.PRECIOUS: $(BUILD)/test_host/ds/ov2/test/%.prejava

$(BUILD)/test_host/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/test_host/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/test_host/ds/ov2/bignat/%.prejava: ../bignat/%.java config \
		$(BUILD)/test_host/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/test_host/ds/ov2/test/%.prejava: %.java config \
		$(BUILD)/test_host/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . $(CPPFLAGS) $$(pwd)/$< >> $@


.PHONY: test-host-driver-cpp
test-host-driver-cpp: $(FULL_TEST_HOST_SOURCES)

#############################################################################
## documentation
##

DOCFILES:= \
	package-info.java \
	Misc_protocols_description.java \
	Misc_protocols_host.java \
	Misc_protocols_stubs.java \
	Misc_protocols_test_stubs.java \
	Bignat_protocols_description.java \
	Bignat_protocols_stubs.java \
	Bignat_protocols_test_stubs.java \
	Card_performance.java \
	Check_data_transmission.java \
	Bignats.java \
	Data_protocol_card.java \
	Data_protocol_description.java \
	Data_protocol_host.java \
	Data_protocol_stubs.java \
	Data_protocol_test_stubs.java \
	Exponent_perf_card.java \
	Exponent_perf_description.java \
	Exponent_perf_host.java \
	Exponent_perf_stubs.java \
	Exponent_perf_test_stubs.java \
	Performance_mult_card.java \
	Performance_mult_host.java \
	State.java \
	Test_protocols.java \
	Test_applet.java \
	Test_host.java

../_doc_build_dir/ds/ov2/test/doc_sources_copied: \
		$(addprefix ../_doc_build_dir/ds/ov2/test/, $(DOCFILES))
	touch $@

../_doc_build_dir/ds/ov2/test/%.prejava: %.java config
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DJAVADOC $(CPPFLAGS) $$(pwd)/$< >> $@


#############################################################################
## Start script
##

ifeq ($(strip $(JCKIT222)),)
JCKIT:=$(JCKIT221)
else
JCKIT:=$(JCKIT222)
endif

TOCLEAN+= test_host
test_host: test_host.in Makefile
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		-e 's|GPM=.*|GPM=$(GPM)|' \
		< $< > $@
	chmod +x $@


TOCLEAN+= reload-cref
reload-cref: reload-cref.in Makefile
	sed -e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		< $< > $@
	chmod +x $@

TOCLEAN+= emulator-cref
emulator-cref: emulator-cref.in Makefile
	sed -e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		< $< > $@
	chmod +x $@


#############################################################################
## Misc
##

emulatorio-jar: $(EMULATORIO)

$(EMULATORIO):
	$(MAKE) -C ../emulator/smartcardio JCKIT222=$(JCKIT222) build-jar

jcopio-jar: $(JCOPIO)
$(JCOPIO):
	$(MAKE) -C ../emulator/jcop OFFCARD=$(OFFCARD) build-jar


tags: TAGS

.PHONY: TAGS
TAGS:
	etags --members *java ../util/*java ../bignat/*java


../tools/Applet_id.class: ../tools/Applet_id.java
	$(MAKE) -C ../tools Applet_id.class

../tools/converter_errors: ../tools/converter_errors.ml
	$(MAKE) -C ../tools converter_errors



.PRECIOUS: %.java

%.java: %.prejava
	rm -f $@
	sed -e 's|^# |//# |' <$< >$@
	chmod -w $@

.PHONY: clean
clean:
	rm -rf $(TOCLEAN)
	rm -rf $(BUILD)
