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
## Created 11.8.08 by Hendrik
## 
## bignat Makefile
## 
## $Id: Makefile,v 1.27 2010-09-20 13:55:34 tews Exp $

# load configuration
include ../ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


# default build target
# if you don't like it, put one in LocalMakefile, eg:
# myfirsttarget: xxx
.PHONY: all
all: testall

.PHONY: core
core: all

.PHONY: testall
testall: testbignat

.PHONY: windows-cpp
windows-cpp: testbignat-cpp

# The name of the build dir is a bit more complicated now.
# That makes collissions more unlikely and simplifies the 
# related emacs lisp code.
BUILD:=_java_build_dir

# The Java Card class path, needed in the test frame because of 
# the UserExecption that is thrown in Bignat.subtract.
JCCP:=$(JCKIT221)/lib/api.jar

#############################################################################
## testframes
##

TESTFRAME_SOURCES:=\
	util/APDU_Serializable.java \
	util/Misc.java \
	util/Misc_host.java \
	util/APDU_short.java \
	util/APDU_long.java \
	util/Serializable_array.java \
	util/Convert_serializable.java \
	util/BigIntUtil.java \
	util/Reference.java \
	util/Option.java \
	util/Bool_option.java \
	util/Commandline.java \
	util/Option.java \
	util/Bool_option.java \
	util/String_option.java \
	util/BigInteger_inputs.java \
	util/BigInt_input_option.java \
	util/BigInt_hex_input_option.java \
	util/Int_option.java \
	util/Value_option.java \
	util/Parse_commandline.java \
	bignat/Bignat.java \
	bignat/APDU_BigInteger.java \
	bignat/Bignat_array.java \
	bignat/RSA_exponent_interface.java \
	bignat/Vector.java \
	bignat/Host_vector.java \
	bignat/Modulus.java \
	bignat/Host_modulus.java \
	bignat/Fake_rsa_exponent.java \
	bignat/Resize.java \
	bignat/Convenience.java \
	bignat/Testbignat.java

FULL_TESTFRAME_SOURCES:=$(addprefix $(BUILD)/testframe/ds/ov2/, \
				$(TESTFRAME_SOURCES))

#############################################################################
## testframe
##

testbignat: $(BUILD)/testframe/javac_compiled run_testbignat

testbignat-dir:
	mkdir -p $(BUILD)/testframe/ds/ov2/bignat

$(BUILD)/testframe/javac_compiled: $(FULL_TESTFRAME_SOURCES)
	javac -classpath $(JCCP):$(BUILD)/testframe -Xlint \
		$(BUILD)/testframe/ds/ov2/*/*.java
	touch $@

$(BUILD)/testframe/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/testframe/ds/ov2/util
	mkdir -p $(BUILD)/testframe/ds/ov2/bignat
	touch $(BUILD)/testframe/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/testframe/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/testframe/ds/ov2/bignat/%.prejava

$(BUILD)/testframe/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/testframe/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/testframe/ds/ov2/bignat/%.prejava: %.java config \
		$(BUILD)/testframe/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . $(CPPFLAGS) $$(pwd)/$< >> $@


.PHONY: testbignat-cpp
testbignat-cpp: $(FULL_TESTFRAME_SOURCES)


.PHONY: test-testbignat
test-testbignat: testbignat
	@echo =================== TEST ============================
	./run-testbignat


#############################################################################
## documentation
##

DOCFILES:= \
	package-info.java \
	APDU_BigInteger.java \
	Bignat_array.java \
	Bignat.java \
	Convenience.java \
	Fake_rsa_exponent.java \
	Host_modulus.java \
	Host_vector.java \
	Inverse_mod_256.java \
	Modulus.java \
	Resize.java \
	RSA_exponent.java \
	Testbignat.java \
	RSA_exponent_interface.java \
	Vector.java

../_doc_build_dir/ds/ov2/bignat/doc_sources_copied: \
		$(addprefix ../_doc_build_dir/ds/ov2/bignat/, $(DOCFILES))
	touch $@

../_doc_build_dir/ds/ov2/bignat/%.prejava: %.java config
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

TOCLEAN+= run_testbignat
run_testbignat: run_testbignat.in Makefile
	sed -e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		< $< > $@
	chmod +x $@


#############################################################################
## general goals/rules
##

.PRECIOUS: %.java

%.java: %.prejava
	rm -f $@
	sed -e 's|^# |//# |' <$< >$@
	chmod -w $@

clean:
	rm -rf $(BUILD)

