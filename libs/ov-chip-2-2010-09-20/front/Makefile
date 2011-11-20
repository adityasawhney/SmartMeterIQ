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
## Created 17.11.08 by Hendrik
## 
## frontoffice Makefile
## 
## $Id: Makefile,v 1.45 2010-09-20 14:33:22 tews Exp $


# load configuration
include ../ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


# default build target
# if you don't like it, put one in LocalMakefile, eg:
# myfirsttarget: xxx
.PHONY: all
all: applets host-driver host-testframes

applets: plain-applet mont-applet square-applet square4-applet

host-driver: rsa-card-testframe cardtestframe

host-testframes: plain-rsa-host-testframe mont-rsa-host-testframe \
	square-rsa-host-testframe square4-rsa-host-testframe \
	plainhosttestframe monthosttestframe \
	squarehosttestframe square4hosttestframe

.PHONY: core
core: all


.PHONY: windows-cpp
windows-cpp: plain-applet-cpp mont-applet-cpp square-applet-cpp \
	square4-applet-cpp \
	rsa-card-testframe-cpp \
	plain-rsa-host-testframe-cpp mont-rsa-host-testframe-cpp \
	square-rsa-host-testframe-cpp square4-rsa-host-testframe-cpp


# The name of the build dir is a bit more complicated now.
# That makes collissions more unlikely and simplifies the 
# related emacs lisp code.
BUILD:=_java_build_dir


JCCP:=$(JCKIT221)/lib/api.jar
JCEP:=$(JCKIT221)/api_export_files


#############################################################################
## Protocol descriptions
##

RSA_IDL_SOURCES:=\
	RSA_card_protocol.id \
	RSA_card_debug.id


RSA_IDL_DESCRIPTIONS:=$(RSA_IDL_SOURCES:.id=_description.java)
RSA_IDL_STUBS:=$(RSA_IDL_SOURCES:.id=_stubs.java)
RSA_IDL_TEST_STUBS:=$(RSA_IDL_SOURCES:.id=_test_stubs.java)

TOCLEAN+= $(RSA_IDL_DESCRIPTIONS) $(RSA_IDL_STUBS) $(RSA_IDL_TEST_STUBS)


.PRECIOUS: %_description.java 
.PRECIOUS: %_stubs.java
.PRECIOUS: %_test_stubs.java

%_description.java %_stubs.java %_test_stubs.java: %.id ../tools/idl.perl
	../tools/idl.perl $<

idl-files: $(RSA_IDL_DESCRIPTIONS) $(RSA_IDL_STUBS) $(RSA_IDL_TEST_STUBS)


# Additional dependencies for the include files. They are a bit 
# imprecise. Strictly, the *.prejava, generated from the *description.java
# depends on the *description_include.java. But this way is simpler.


#############################################################################
## Plain RSA Applet
##

PLAIN_RSA_PACKAGE:=ds_ov2_prsa

PLAIN_APPLET_BUILDDIR:=$(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE)/javacard

plain-applet: $(PLAIN_APPLET_BUILDDIR)/$(PLAIN_RSA_PACKAGE).cap
plain-applet-jcop: $(PLAIN_APPLET_BUILDDIR)/applet-installed-jcop
plain-applet-jcwde: $(PLAIN_APPLET_BUILDDIR)/applet-installed-jcwde
plain-applet-cref: $(PLAIN_APPLET_BUILDDIR)/applet-installed-cref
plain-applet-card: $(PLAIN_APPLET_BUILDDIR)/applet-installed-card
plain-applet-eeprom: $(PLAIN_APPLET_BUILDDIR)/rsa_eeprom.created

# XXX the applet id is also hardwired in config!
PLAIN_RSA_APPLET_ID_NAME:=$(PLAIN_RSA_PACKAGE).app

PLAIN_RSA_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(PLAIN_RSA_APPLET_ID_NAME))
PLAIN_RSA_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(PLAIN_RSA_APPLET_ID_NAME))
PLAIN_RSA_PACKAGE_VERSION:=1.0

# PLAIN_RSA_PACKAGE_ID = 0x64:0x73:0x5f:0x6f:0x76:0x32:0x5f:0x70:0x72:0x73:0x61
# PLAIN_RSA_APPLET_ID  = 0x64:0x73:0x5f:0x6f:0x76:0x32:0x5f:0x70:0x72:0x73:0x61:0x2e:0x61:0x70:0x70


SHARED_RSA_APPLET_SOURCES:=\
	util/PACKAGE/Misc.java \
	util/PACKAGE/Void_method.java \
	util/PACKAGE/Empty_void_method.java \
	util/PACKAGE/APDU_Serializable.java \
	util/PACKAGE/Serializable_array.java \
	util/PACKAGE/APDU_byte.java \
	util/PACKAGE/APDU_short.java \
	util/PACKAGE/APDU_boolean.java \
	util/PACKAGE/APDU_byte_array.java \
	util/PACKAGE/Response_status.java \
	util/PACKAGE/Protocol_step.java \
	util/PACKAGE/Protocol.java \
	util/PACKAGE/Registered_protocols.java \
	util/PACKAGE/Card_protocol.java \
	util/PACKAGE/Protocol_applet.java \
	bignat/PACKAGE/Bignat.java \
	bignat/PACKAGE/Modulus.java \
	bignat/PACKAGE/Bignat_array.java \
	bignat/PACKAGE/RSA_exponent_interface.java \
	bignat/PACKAGE/RSA_exponent.java \
	bignat/PACKAGE/Vector.java \
	front/PACKAGE/Hash_size.java \
	front/PACKAGE/Signature.java \
	front/PACKAGE/RSA_data.java \
	front/PACKAGE/RSA_card_protocol_description.java \
	front/PACKAGE/RSA_card_debug_description.java \
	front/PACKAGE/Front_protocols.java \
	front/PACKAGE/RSA_applet.java

PLAIN_RSA_APPLET_SOURCES:=\
	front/PACKAGE/RSA_plain_card.java

PLAIN_RSA_APPLET_PACK_SOURCES:=\
	$(subst PACKAGE,$(PLAIN_RSA_PACKAGE), \
		$(SHARED_RSA_APPLET_SOURCES) $(PLAIN_RSA_APPLET_SOURCES))


FULL_PLAIN_RSA_APPLET_SOURCES:=$(addprefix $(BUILD)/plain_rsa_card/, \
		$(PLAIN_RSA_APPLET_PACK_SOURCES))

# use -target 5 for 2.2.2
$(BUILD)/plain_rsa_card/front/javac_compiled: $(FULL_PLAIN_RSA_APPLET_SOURCES)
	javac -g -source 1.2 -target 1.2 -classpath \
		$(JCCP):$(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE) \
		-Xlint \
		$(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE)/*.java
	touch $@

# $(BUILD)/plain_rsa_card/front/javac_compiled: $(RSA_APPLET_SOURCES)
# 	javac -g -target 5 -classpath \
# 		$(JCCP):$(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE) \
# 		-Xlint \
# 		$(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE)/*.java
# 	touch $@


# .PRECIOUS: $(BUILD)/plain_rsa_card/util/$(PLAIN_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/plain_rsa_card/bignat/$(PLAIN_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE)/%.prejava

$(BUILD)/plain_rsa_card/util/$(PLAIN_RSA_PACKAGE)/%.prejava: ../util/%.java config \
		$(BUILD)/plain_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_PLAIN_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/plain_rsa_card/bignat/$(PLAIN_RSA_PACKAGE)/%.prejava: ../bignat/%.java config \
		$(BUILD)/plain_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_PLAIN_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE)/%.prejava: %.java config \
		$(BUILD)/plain_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat \
		-DOV_PLAIN_RSA_TEST_APPLET $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/plain_rsa_card/directory-exists:
	mkdir -p $(BUILD)/plain_rsa_card/front/$(PLAIN_RSA_PACKAGE)
	(cd $(BUILD)/plain_rsa_card; ln -s front util)
	(cd $(BUILD)/plain_rsa_card; ln -s front bignat)
	touch $(BUILD)/plain_rsa_card/directory-exists


.PHONY: plain-applet-cpp
plain-applet-cpp: $(FULL_PLAIN_RSA_APPLET_SOURCES)


$(PLAIN_APPLET_BUILDDIR)/$(PLAIN_RSA_PACKAGE).cap: \
		$(BUILD)/plain_rsa_card/front/javac_compiled \
		../tools/Applet_id.class ../tools/converter_errors
	@echo PLAIN_RSA_PACKAGE_ID = $(PLAIN_RSA_PACKAGE_ID)
	@echo 'PLAIN_RSA_APPLET_ID  =' $(PLAIN_RSA_APPLET_ID)
	$(JCKIT221)/bin/converter -classdir $(BUILD)/plain_rsa_card/front \
		-out EXP JCA CAP \
		-exportpath $(JCEP):$(BUILD)/plain_rsa_card/front \
		-applet $(PLAIN_RSA_APPLET_ID) \
		$(PLAIN_RSA_PACKAGE).RSA_applet $(PLAIN_RSA_PACKAGE) \
		$(PLAIN_RSA_PACKAGE_ID) $(PLAIN_RSA_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors -classdir \
					$(BUILD)/plain_rsa_card/front


.PHONY: verify-plain-applet
verify-plain-applet: $(PLAIN_APPLET_BUILDDIR)/$(PLAIN_RSA_PACKAGE).cap
	$(JCKIT221)/bin/verifycap \
	   $(JCKIT221)/api_export_files/java/lang/javacard/lang.exp \
        $(JCKIT221)/api_export_files/javacard/security/javacard/security.exp \
       $(JCKIT221)/api_export_files/javacard/framework/javacard/framework.exp \
	   $(JCKIT221)/api_export_files/javacardx/crypto/javacard/crypto.exp \
	   $<


#############################################################################
## Montgomerizing RSA Applet
##

MONT_RSA_PACKAGE:=ds_ov2_mrsa

MONT_APPLET_BUILDDIR:=$(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE)/javacard

mont-applet: $(MONT_APPLET_BUILDDIR)/$(MONT_RSA_PACKAGE).cap
mont-applet-jcop: $(MONT_APPLET_BUILDDIR)/applet-installed-jcop
mont-applet-jcwde: $(MONT_APPLET_BUILDDIR)/applet-installed-jcwde
mont-applet-cref: $(MONT_APPLET_BUILDDIR)/applet-installed-cref
mont-applet-card: $(MONT_APPLET_BUILDDIR)/applet-installed-card
mont-applet-eeprom: $(MONT_APPLET_BUILDDIR)/rsa_eeprom.created

# XXX the applet id is also hardwired in config!
MONT_RSA_APPLET_ID_NAME:=$(MONT_RSA_PACKAGE).app

MONT_RSA_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(MONT_RSA_APPLET_ID_NAME))
MONT_RSA_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(MONT_RSA_APPLET_ID_NAME))
MONT_RSA_PACKAGE_VERSION:=1.0

# MONT_RSA_PACKAGE_ID = 0x64:0x73:0x5f:0x6f:0x76:0x32:0x5f:0x6d:0x72:0x73:0x61                    
# MONT_RSA_APPLET_ID  = 0x64:0x73:0x5f:0x6f:0x76:0x32:0x5f:0x6d:0x72:0x73:0x61:0x2e:0x61:0x70:0x70


MONT_RSA_APPLET_SOURCES:=\
	front/PACKAGE/RSA_mont_card.java

MONT_RSA_APPLET_PACK_SOURCES:=\
	$(subst PACKAGE,$(MONT_RSA_PACKAGE), \
		$(SHARED_RSA_APPLET_SOURCES) $(MONT_RSA_APPLET_SOURCES))


FULL_MONT_RSA_APPLET_SOURCES:=$(addprefix $(BUILD)/mont_rsa_card/, \
		$(MONT_RSA_APPLET_PACK_SOURCES))

# use -target 5 for 2.2.2
$(BUILD)/mont_rsa_card/front/javac_compiled: $(FULL_MONT_RSA_APPLET_SOURCES)
	javac -g -source 1.2 -target 1.2 -classpath \
		$(JCCP):$(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE) \
		-Xlint \
		$(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE)/*.java
	touch $@

# $(BUILD)/mont_rsa_card/front/javac_compiled: $(RSA_APPLET_SOURCES)
# 	javac -g -target 5 -classpath \
# 		$(JCCP):$(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE) \
# 		-Xlint \
# 		$(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE)/*.java
# 	touch $@


# .PRECIOUS: $(BUILD)/mont_rsa_card/util/$(MONT_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/mont_rsa_card/bignat/$(MONT_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE)/%.prejava

$(BUILD)/mont_rsa_card/util/$(MONT_RSA_PACKAGE)/%.prejava: ../util/%.java config \
		$(BUILD)/mont_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_MONT_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/mont_rsa_card/bignat/$(MONT_RSA_PACKAGE)/%.prejava: ../bignat/%.java config \
		$(BUILD)/mont_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_MONT_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE)/%.prejava: %.java config \
		$(BUILD)/mont_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat \
		-DOV_MONT_RSA_TEST_APPLET $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/mont_rsa_card/directory-exists:
	mkdir -p $(BUILD)/mont_rsa_card/front/$(MONT_RSA_PACKAGE)
	(cd $(BUILD)/mont_rsa_card; ln -s front util)
	(cd $(BUILD)/mont_rsa_card; ln -s front bignat)
	touch $(BUILD)/mont_rsa_card/directory-exists


.PHONY: mont-applet-cpp
mont-applet-cpp: $(FULL_MONT_RSA_APPLET_SOURCES)


$(MONT_APPLET_BUILDDIR)/$(MONT_RSA_PACKAGE).cap: \
		$(BUILD)/mont_rsa_card/front/javac_compiled \
		../tools/Applet_id.class ../tools/converter_errors
	@echo MONT_RSA_PACKAGE_ID = $(MONT_RSA_PACKAGE_ID)
	@echo 'MONT_RSA_APPLET_ID  =' $(MONT_RSA_APPLET_ID)
	$(JCKIT221)/bin/converter -classdir $(BUILD)/mont_rsa_card/front \
		-out EXP JCA CAP \
		-exportpath $(JCEP):$(BUILD)/mont_rsa_card/front \
		-applet $(MONT_RSA_APPLET_ID) \
		$(MONT_RSA_PACKAGE).RSA_applet $(MONT_RSA_PACKAGE) \
		$(MONT_RSA_PACKAGE_ID) $(MONT_RSA_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors -classdir \
					$(BUILD)/mont_rsa_card/front


.PHONY: verify-mont-applet
verify-mont-applet: $(MONT_APPLET_BUILDDIR)/$(MONT_RSA_PACKAGE).cap
	$(JCKIT221)/bin/verifycap \
	   $(JCKIT221)/api_export_files/java/lang/javacard/lang.exp \
        $(JCKIT221)/api_export_files/javacard/security/javacard/security.exp \
       $(JCKIT221)/api_export_files/javacard/framework/javacard/framework.exp \
	   $(JCKIT221)/api_export_files/javacardx/crypto/javacard/crypto.exp \
	   $<


#############################################################################
## Squaring RSA Applet (using  ((x+y)^2 - x^2 - y^2)/2
##

SQUARE_RSA_PACKAGE:=ds_ov2_srsa

SQUARE_APPLET_BUILDDIR:=$(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE)/javacard

square-applet: $(SQUARE_APPLET_BUILDDIR)/$(SQUARE_RSA_PACKAGE).cap
square-applet-jcop: $(SQUARE_APPLET_BUILDDIR)/applet-installed-jcop
square-applet-jcwde: $(SQUARE_APPLET_BUILDDIR)/applet-installed-jcwde
square-applet-cref: $(SQUARE_APPLET_BUILDDIR)/applet-installed-cref
square-applet-card: $(SQUARE_APPLET_BUILDDIR)/applet-installed-card
square-applet-eeprom: $(SQUARE_APPLET_BUILDDIR)/rsa_eeprom.created

# XXX the applet id is also hardwired in config!
SQUARE_RSA_APPLET_ID_NAME:=$(SQUARE_RSA_PACKAGE).app

SQUARE_RSA_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(SQUARE_RSA_APPLET_ID_NAME))
SQUARE_RSA_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(SQUARE_RSA_APPLET_ID_NAME))
SQUARE_RSA_PACKAGE_VERSION:=1.0

# SQUARE_RSA_PACKAGE_ID = 0x64:0x73:0x5f:0x6f:0x76:0x32:0x5f:0x73:0x72:0x73:0x61                    
# SQUARE_RSA_APPLET_ID  = 0x64:0x73:0x5f:0x6f:0x76:0x32:0x5f:0x73:0x72:0x73:0x61:0x2e:0x61:0x70:0x70


SQUARE_RSA_APPLET_SOURCES:=\
	front/PACKAGE/RSA_squared_card.java

SQUARE_RSA_APPLET_PACK_SOURCES:=\
	$(subst PACKAGE,$(SQUARE_RSA_PACKAGE), \
		$(SHARED_RSA_APPLET_SOURCES) $(SQUARE_RSA_APPLET_SOURCES))


FULL_SQUARE_RSA_APPLET_SOURCES:=$(addprefix $(BUILD)/square_rsa_card/, \
		$(SQUARE_RSA_APPLET_PACK_SOURCES))

# use -target 5 for 2.2.2
$(BUILD)/square_rsa_card/front/javac_compiled: $(FULL_SQUARE_RSA_APPLET_SOURCES)
	javac -g -source 1.2 -target 1.2 -classpath \
		$(JCCP):$(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE) \
		-Xlint \
		$(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE)/*.java
	touch $@

# $(BUILD)/square_rsa_card/front/javac_compiled: $(RSA_APPLET_SOURCES)
# 	javac -g -target 5 -classpath \
# 		$(JCCP):$(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE) \
# 		-Xlint \
# 		$(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE)/*.java
# 	touch $@


# .PRECIOUS: $(BUILD)/square_rsa_card/util/$(SQUARE_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/square_rsa_card/bignat/$(SQUARE_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE)/%.prejava

$(BUILD)/square_rsa_card/util/$(SQUARE_RSA_PACKAGE)/%.prejava: ../util/%.java config \
		$(BUILD)/square_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_SQUARE_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/square_rsa_card/bignat/$(SQUARE_RSA_PACKAGE)/%.prejava: ../bignat/%.java config \
		$(BUILD)/square_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_SQUARE_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE)/%.prejava: %.java config \
		$(BUILD)/square_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat \
		-DOV_SQUARE_RSA_TEST_APPLET $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/square_rsa_card/directory-exists:
	mkdir -p $(BUILD)/square_rsa_card/front/$(SQUARE_RSA_PACKAGE)
	(cd $(BUILD)/square_rsa_card; ln -s front util)
	(cd $(BUILD)/square_rsa_card; ln -s front bignat)
	touch $(BUILD)/square_rsa_card/directory-exists


$(SQUARE_APPLET_BUILDDIR)/$(SQUARE_RSA_PACKAGE).cap: \
		$(BUILD)/square_rsa_card/front/javac_compiled \
		../tools/Applet_id.class ../tools/converter_errors
	@echo SQUARE_RSA_PACKAGE_ID = $(SQUARE_RSA_PACKAGE_ID)
	@echo 'SQUARE_RSA_APPLET_ID  =' $(SQUARE_RSA_APPLET_ID)
	$(JCKIT221)/bin/converter -classdir $(BUILD)/square_rsa_card/front \
		-out EXP JCA CAP \
		-exportpath $(JCEP):$(BUILD)/square_rsa_card/front \
		-applet $(SQUARE_RSA_APPLET_ID) \
		$(SQUARE_RSA_PACKAGE).RSA_applet $(SQUARE_RSA_PACKAGE) \
		$(SQUARE_RSA_PACKAGE_ID) $(SQUARE_RSA_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors -classdir \
					$(BUILD)/square_rsa_card/front


.PHONY: square-applet-cpp
square-applet-cpp: $(FULL_SQUARE_RSA_APPLET_SOURCES)


.PHONY: verify-square-applet
verify-square-applet: $(SQUARE_APPLET_BUILDDIR)/$(SQUARE_RSA_PACKAGE).cap
	$(JCKIT221)/bin/verifycap \
	   $(JCKIT221)/api_export_files/java/lang/javacard/lang.exp \
        $(JCKIT221)/api_export_files/javacard/security/javacard/security.exp \
       $(JCKIT221)/api_export_files/javacard/framework/javacard/framework.exp \
	   $(JCKIT221)/api_export_files/javacardx/crypto/javacard/crypto.exp \
	   $<


#############################################################################
## Square 4 RSA Applet (using  ((x+y)^2 - (x-y)^2)/4)
##

SQUARE4_RSA_PACKAGE:=ds_ov2_s4rsa

SQUARE4_APPLET_BUILDDIR:=$(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE)/javacard

square4-applet: $(SQUARE4_APPLET_BUILDDIR)/$(SQUARE4_RSA_PACKAGE).cap
square4-applet-jcop: $(SQUARE4_APPLET_BUILDDIR)/applet-installed-jcop
square4-applet-jcwde: $(SQUARE4_APPLET_BUILDDIR)/applet-installed-jcwde
square4-applet-cref: $(SQUARE4_APPLET_BUILDDIR)/applet-installed-cref
square4-applet-card: $(SQUARE4_APPLET_BUILDDIR)/applet-installed-card
square4-applet-eeprom: $(SQUARE4_APPLET_BUILDDIR)/rsa_eeprom.created

# XXX the applet id is also hardwired in config!
SQUARE4_RSA_APPLET_ID_NAME:=$(SQUARE4_RSA_PACKAGE).app

SQUARE4_RSA_PACKAGE_ID=\
	$(shell java -cp ../tools Applet_id -package $(SQUARE4_RSA_APPLET_ID_NAME))
SQUARE4_RSA_APPLET_ID=\
	$(shell java -cp ../tools Applet_id $(SQUARE4_RSA_APPLET_ID_NAME))
SQUARE4_RSA_PACKAGE_VERSION:=1.0

# SQUARE4_RSA_PACKAGE_ID = 
# SQUARE4_RSA_APPLET_ID  = 


SQUARE4_RSA_APPLET_SOURCES:=\
	front/PACKAGE/RSA_squared_card.java

SQUARE4_RSA_APPLET_PACK_SOURCES:=\
	$(subst PACKAGE,$(SQUARE4_RSA_PACKAGE), \
		$(SHARED_RSA_APPLET_SOURCES) $(SQUARE4_RSA_APPLET_SOURCES))


FULL_SQUARE4_RSA_APPLET_SOURCES:=$(addprefix $(BUILD)/square4_rsa_card/, \
		$(SQUARE4_RSA_APPLET_PACK_SOURCES))

# use -target 5 for 2.2.2
$(BUILD)/square4_rsa_card/front/javac_compiled: \
				$(FULL_SQUARE4_RSA_APPLET_SOURCES)
	javac -g -source 1.2 -target 1.2 -classpath \
		$(JCCP):$(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE) \
		-Xlint \
		$(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE)/*.java
	touch $@

# $(BUILD)/square4_rsa_card/front/javac_compiled: $(RSA_APPLET_SOURCES)
# 	javac -g -target 5 -classpath \
# 		$(JCCP):$(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE) \
# 		-Xlint \
# 		$(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE)/*.java
# 	touch $@


# .PRECIOUS: $(BUILD)/square4_rsa_card/util/$(SQUARE4_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/square4_rsa_card/bignat/$(SQUARE4_RSA_PACKAGE)/%.prejava
# .PRECIOUS: $(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE)/%.prejava

$(BUILD)/square4_rsa_card/util/$(SQUARE4_RSA_PACKAGE)/%.prejava: \
		../util/%.java config \
		$(BUILD)/square4_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_SQUARE4_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/square4_rsa_card/bignat/$(SQUARE4_RSA_PACKAGE)/%.prejava: \
		../bignat/%.java config \
		$(BUILD)/square4_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DOV_SQUARE4_RSA_TEST_APPLET \
					$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE)/%.prejava: \
		%.java config \
		$(BUILD)/square4_rsa_card/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat \
		-DOV_SQUARE4_RSA_TEST_APPLET $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/square4_rsa_card/directory-exists:
	mkdir -p $(BUILD)/square4_rsa_card/front/$(SQUARE4_RSA_PACKAGE)
	(cd $(BUILD)/square4_rsa_card; ln -s front util)
	(cd $(BUILD)/square4_rsa_card; ln -s front bignat)
	touch $(BUILD)/square4_rsa_card/directory-exists


$(SQUARE4_APPLET_BUILDDIR)/$(SQUARE4_RSA_PACKAGE).cap: \
		$(BUILD)/square4_rsa_card/front/javac_compiled \
		../tools/Applet_id.class ../tools/converter_errors
	@echo SQUARE4_RSA_PACKAGE_ID = $(SQUARE4_RSA_PACKAGE_ID)
	@echo 'SQUARE4_RSA_APPLET_ID  =' $(SQUARE4_RSA_APPLET_ID)
	$(JCKIT221)/bin/converter -classdir $(BUILD)/square4_rsa_card/front \
		-out EXP JCA CAP \
		-exportpath $(JCEP):$(BUILD)/square4_rsa_card/front \
		-applet $(SQUARE4_RSA_APPLET_ID) \
		$(SQUARE4_RSA_PACKAGE).RSA_applet $(SQUARE4_RSA_PACKAGE) \
		$(SQUARE4_RSA_PACKAGE_ID) $(SQUARE4_RSA_PACKAGE_VERSION) \
		2>&1 | ../tools/converter_errors -classdir \
					$(BUILD)/square4_rsa_card/front


.PHONY: square4-applet-cpp
square4-applet-cpp: $(FULL_SQUARE4_RSA_APPLET_SOURCES)


.PHONY: verify-square4-applet
verify-square4-applet: $(SQUARE4_APPLET_BUILDDIR)/$(SQUARE4_RSA_PACKAGE).cap
	$(JCKIT221)/bin/verifycap \
	   $(JCKIT221)/api_export_files/java/lang/javacard/lang.exp \
        $(JCKIT221)/api_export_files/javacard/security/javacard/security.exp \
       $(JCKIT221)/api_export_files/javacard/framework/javacard/framework.exp \
	   $(JCKIT221)/api_export_files/javacardx/crypto/javacard/crypto.exp \
	   $<


#############################################################################
## Plain RSA Host Testframe
##

plain-rsa-host-testframe: $(BUILD)/plain_rsa_host_test/javac_compiled

PLAIN_RSA_HOST_TEST_ONLY_SOURCES:=\
	front/RSA_plain_card.java


RSA_HOST_TEST_ONLY_SOURCES:=\
	bignat/Fake_rsa_exponent.java \
	front/RSA_card_protocol_test_stubs.java \
	front/RSA_card_debug_test_stubs.java \
	\
	util/Void_method.java \
	util/Empty_void_method.java \
	front/Host_testframe.java



RSA_SHARED_TEST_SOURCES:=\
	util/Misc.java \
	util/Misc_host.java \
	util/APDU_Serializable.java \
	util/Serializable_array.java \
	util/APDU_byte.java \
	util/APDU_short.java \
	util/APDU_boolean.java \
	util/APDU_byte_array.java \
	util/Protocol_step.java \
	util/Protocol.java \
	util/Registered_protocols.java \
	util/Host_protocol.java \
	util/Convert_serializable.java \
	util/Response_status.java \
	util/Response_apdu.java \
	util/Card_terminal.java \
	util/BigIntUtil.java \
	util/Message_digest_wrapper.java \
	util/Security_parameter.java \
	util/PrintWriter_APDUListener.java \
	util/Unsecure_rsa_key.java \
	util/Reference.java \
	util/Option.java \
	util/Bool_option.java \
	util/Int_option.java \
	util/Bool_int_option.java \
	util/Value_option.java \
	util/BigInteger_inputs.java \
	util/BigInt_input_option.java \
	util/BigInt_hex_input_option.java \
	util/Commandline.java \
	util/Parse_commandline.java \
	bignat/Bignat.java \
	bignat/Modulus.java \
	bignat/Bignat_array.java \
	bignat/RSA_exponent_interface.java \
	bignat/Vector.java \
	bignat/APDU_BigInteger.java \
	bignat/Host_modulus.java \
	bignat/Host_vector.java \
	front/RSA_protocol_messages.java \
	front/State.java \
	front/Test_state.java \
	front/Hash_size.java \
	front/Signature.java \
	front/Host_signature.java \
	front/RSA_data.java \
	front/RSA_card_protocol_description.java \
	front/RSA_card_debug_description.java \
	front/Front_protocols.java \
	front/PTLS_rsa_parameters.java \
	front/Applet_type.java \
	front/RSA_host_card.java \
	front/RSA_card_debug.java \
	front/Card_services.java \
	front/Testframe.java


# The following sources are only needed if this directory is compiled 
# with the int/long bignat configuration. For the byte/short configuration
# BIGNAT_INT_SOURCES can safely left empty.
BIGNAT_INT_SOURCES:=\
	util/APDU_long.java


PLAIN_RSA_HOST_TEST_SOURCES:=\
	$(addprefix $(BUILD)/plain_rsa_host_test/ds/ov2/,\
		$(PLAIN_RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_SHARED_TEST_SOURCES) \
		$(BIGNAT_INT_SOURCES))


$(BUILD)/plain_rsa_host_test/javac_compiled: $(PLAIN_RSA_HOST_TEST_SOURCES)
	javac -classpath \
		$(BUILD)/plain_rsa_host_test:$(JCCP):$(GPM) \
		-Xlint \
		$(BUILD)/plain_rsa_host_test/ds/ov2/*/*.java
	touch $@


$(BUILD)/plain_rsa_host_test/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/plain_rsa_host_test/ds/ov2/util
	mkdir -p $(BUILD)/plain_rsa_host_test/ds/ov2/bignat
	mkdir -p $(BUILD)/plain_rsa_host_test/ds/ov2/front
	touch $(BUILD)/plain_rsa_host_test/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/plain_rsa_host_test/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/plain_rsa_host_test/ds/ov2/bignat/%.prejava
#.PRECIOUS: $(BUILD)/plain_rsa_host_test/ds/ov2/front/%.prejava

$(BUILD)/plain_rsa_host_test/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/plain_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DPLAIN_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/plain_rsa_host_test/ds/ov2/bignat/%.prejava: ../bignat/%.java config \
		$(BUILD)/plain_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DPLAIN_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/plain_rsa_host_test/ds/ov2/front/%.prejava: %.java config \
		$(BUILD)/plain_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat -DPLAIN_APPLET_TESTFRAME \
		$(CPPFLAGS) $$(pwd)/$< >> $@


.PHONY: plain-rsa-host-testframe-cpp
plain-rsa-host-testframe-cpp: $(PLAIN_RSA_HOST_TEST_SOURCES)


#############################################################################
## Montgomerizing RSA Host Testframe
##

mont-rsa-host-testframe: $(BUILD)/mont_rsa_host_test/javac_compiled

MONT_RSA_HOST_TEST_ONLY_SOURCES:=\
	front/RSA_mont_card.java


MONT_RSA_HOST_TEST_SOURCES:=\
	$(addprefix $(BUILD)/mont_rsa_host_test/ds/ov2/,\
		$(MONT_RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_SHARED_TEST_SOURCES) \
		$(BIGNAT_INT_SOURCES))


$(BUILD)/mont_rsa_host_test/javac_compiled: $(MONT_RSA_HOST_TEST_SOURCES)
	javac -classpath \
		$(BUILD)/mont_rsa_host_test:$(JCCP):$(GPM) \
		-Xlint \
		$(BUILD)/mont_rsa_host_test/ds/ov2/*/*.java
	touch $@


$(BUILD)/mont_rsa_host_test/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/mont_rsa_host_test/ds/ov2/util
	mkdir -p $(BUILD)/mont_rsa_host_test/ds/ov2/bignat
	mkdir -p $(BUILD)/mont_rsa_host_test/ds/ov2/front
	touch $(BUILD)/mont_rsa_host_test/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/mont_rsa_host_test/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/mont_rsa_host_test/ds/ov2/bignat/%.prejava
#.PRECIOUS: $(BUILD)/mont_rsa_host_test/ds/ov2/front/%.prejava

$(BUILD)/mont_rsa_host_test/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/mont_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DMONT_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/mont_rsa_host_test/ds/ov2/bignat/%.prejava: ../bignat/%.java config \
		$(BUILD)/mont_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DMONT_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/mont_rsa_host_test/ds/ov2/front/%.prejava: %.java config \
		$(BUILD)/mont_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat -DMONT_APPLET_TESTFRAME \
		$(CPPFLAGS) $$(pwd)/$< >> $@


.PHONY: mont-rsa-host-testframe-cpp
mont-rsa-host-testframe-cpp: $(MONT_RSA_HOST_TEST_SOURCES)


#############################################################################
## Squaring RSA Host Testframe (for the applet with ((x+y)^2 - x^2 - y^2)/2)
##

square-rsa-host-testframe: $(BUILD)/square_rsa_host_test/javac_compiled

SQUARE_RSA_HOST_TEST_ONLY_SOURCES:=\
	front/RSA_squared_card.java


SQUARE_RSA_HOST_TEST_SOURCES:=\
	$(addprefix $(BUILD)/square_rsa_host_test/ds/ov2/,\
		$(SQUARE_RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_SHARED_TEST_SOURCES) \
		$(BIGNAT_INT_SOURCES))


$(BUILD)/square_rsa_host_test/javac_compiled: $(SQUARE_RSA_HOST_TEST_SOURCES)
	javac -classpath \
		$(BUILD)/square_rsa_host_test:$(JCCP):$(GPM) \
		-Xlint \
		$(BUILD)/square_rsa_host_test/ds/ov2/*/*.java
	touch $@


$(BUILD)/square_rsa_host_test/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/square_rsa_host_test/ds/ov2/util
	mkdir -p $(BUILD)/square_rsa_host_test/ds/ov2/bignat
	mkdir -p $(BUILD)/square_rsa_host_test/ds/ov2/front
	touch $(BUILD)/square_rsa_host_test/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/square_rsa_host_test/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/square_rsa_host_test/ds/ov2/bignat/%.prejava
#.PRECIOUS: $(BUILD)/square_rsa_host_test/ds/ov2/front/%.prejava

$(BUILD)/square_rsa_host_test/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/square_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DSQUARE_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/square_rsa_host_test/ds/ov2/bignat/%.prejava: ../bignat/%.java config \
		$(BUILD)/square_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DSQUARE_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/square_rsa_host_test/ds/ov2/front/%.prejava: %.java config \
		$(BUILD)/square_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat -DSQUARE_APPLET_TESTFRAME \
		$(CPPFLAGS) $$(pwd)/$< >> $@


.PHONY: square-rsa-host-testframe-cpp
square-rsa-host-testframe-cpp: $(SQUARE_RSA_HOST_TEST_SOURCES)


#############################################################################
## Square 4 RSA Host Testframe (for the applet with ((x+y)^2 - (x-y)^2)/2)
##

square4-rsa-host-testframe: $(BUILD)/square4_rsa_host_test/javac_compiled

SQUARE4_RSA_HOST_TEST_ONLY_SOURCES:=\
	front/RSA_squared_card.java


SQUARE4_RSA_HOST_TEST_SOURCES:=\
	$(addprefix $(BUILD)/square4_rsa_host_test/ds/ov2/,\
		$(SQUARE4_RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_HOST_TEST_ONLY_SOURCES) \
		$(RSA_SHARED_TEST_SOURCES) \
		$(BIGNAT_INT_SOURCES))


$(BUILD)/square4_rsa_host_test/javac_compiled: $(SQUARE4_RSA_HOST_TEST_SOURCES)
	javac -classpath \
		$(BUILD)/square4_rsa_host_test:$(JCCP):$(GPM) \
		-Xlint \
		$(BUILD)/square4_rsa_host_test/ds/ov2/*/*.java
	touch $@


$(BUILD)/square4_rsa_host_test/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/square4_rsa_host_test/ds/ov2/util
	mkdir -p $(BUILD)/square4_rsa_host_test/ds/ov2/bignat
	mkdir -p $(BUILD)/square4_rsa_host_test/ds/ov2/front
	touch $(BUILD)/square4_rsa_host_test/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/square4_rsa_host_test/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/square4_rsa_host_test/ds/ov2/bignat/%.prejava
#.PRECIOUS: $(BUILD)/square4_rsa_host_test/ds/ov2/front/%.prejava

$(BUILD)/square4_rsa_host_test/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/square4_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DSQUARE4_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/square4_rsa_host_test/ds/ov2/bignat/%.prejava: ../bignat/%.java config \
		$(BUILD)/square4_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DSQUARE4_APPLET_TESTFRAME $(CPPFLAGS) \
		$$(pwd)/$< >> $@

$(BUILD)/square4_rsa_host_test/ds/ov2/front/%.prejava: %.java config \
		$(BUILD)/square4_rsa_host_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat -DSQUARE4_APPLET_TESTFRAME \
		$(CPPFLAGS) $$(pwd)/$< >> $@


.PHONY: square4-rsa-host-testframe-cpp
square4-rsa-host-testframe-cpp: $(SQUARE4_RSA_HOST_TEST_SOURCES)


#############################################################################
## RSA Card Testframe (host application for the real applets)
##

rsa-card-testframe: $(BUILD)/rsa_card_test/javac_compiled

RSA_CARD_TEST_ONLY_SOURCES:=\
	front/RSA_card_protocol_stubs.java \
	front/RSA_card_debug_stubs.java \
	front/Card_testframe.java


RSA_CARD_TEST_SOURCES:=\
	$(addprefix $(BUILD)/rsa_card_test/ds/ov2/,\
		$(RSA_CARD_TEST_ONLY_SOURCES) \
		$(RSA_SHARED_TEST_SOURCES) \
		$(BIGNAT_INT_SOURCES))


$(BUILD)/rsa_card_test/javac_compiled: $(RSA_CARD_TEST_SOURCES)
	javac -classpath \
		$(BUILD)/rsa_card_test:$(JCCP):$(GPM) \
		-Xlint \
		$(BUILD)/rsa_card_test/ds/ov2/*/*.java
	touch $@


$(BUILD)/rsa_card_test/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/rsa_card_test/ds/ov2/util
	mkdir -p $(BUILD)/rsa_card_test/ds/ov2/bignat
	mkdir -p $(BUILD)/rsa_card_test/ds/ov2/front
	touch $(BUILD)/rsa_card_test/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/rsa_card_test/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/rsa_card_test/ds/ov2/bignat/%.prejava
#.PRECIOUS: $(BUILD)/rsa_card_test/ds/ov2/front/%.prejava

$(BUILD)/rsa_card_test/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/rsa_card_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DHOST_TESTFRAME $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/rsa_card_test/ds/ov2/bignat/%.prejava: ../bignat/%.java config \
		$(BUILD)/rsa_card_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DHOST_TESTFRAME $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/rsa_card_test/ds/ov2/front/%.prejava: %.java config \
		$(BUILD)/rsa_card_test/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat -DHOST_TESTFRAME \
		$(CPPFLAGS) $$(pwd)/$< >> $@


.PHONY: rsa-card-testframe-cpp
rsa-card-testframe-cpp: $(RSA_CARD_TEST_SOURCES)

#############################################################################
## documentation
##

DOCFILES:= \
	package-info.java \
	Card_testframe.java \
	Hash_size.java \
	Host_signature.java \
	Host_testframe.java \
	PTLS_rsa_parameters.java \
	Front_protocols.java \
	RSA_applet.java \
	RSA_card_debug_description.java \
	RSA_card_debug.java \
	RSA_card_debug_stubs.java \
	RSA_card_debug_test_stubs.java \
	RSA_plain_card.java \
	RSA_mont_card.java \
	RSA_squared_card.java \
	RSA_card_protocol_description.java \
	RSA_card_protocol_stubs.java \
	RSA_card_protocol_test_stubs.java \
	RSA_data.java \
	Applet_type.java \
	RSA_host_card.java \
	RSA_protocol_messages.java \
	Signature.java \
	State.java \
	Testframe.java \
	Card_services.java \
	Test_state.java


../_doc_build_dir/ds/ov2/front/doc_sources_copied: \
		$(addprefix ../_doc_build_dir/ds/ov2/front/, $(DOCFILES))
	touch $@

../_doc_build_dir/ds/ov2/front/%.prejava: %.java config
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat \
		-DJAVADOC $(CPPFLAGS) $$(pwd)/$< >> $@


#############################################################################
## Start script
##

ifeq ($(strip $(JCKIT222)),)
JCKIT:=$(JCKIT221)
else
JCKIT:=$(JCKIT222)
endif

TOCLEAN+= plainhosttestframe
plainhosttestframe: plainhosttestframe.in Makefile
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		-e 's|GPM=.*|GPM=$(GPM)|' \
		< $< > $@
	chmod +x $@

TOCLEAN+= monthosttestframe
monthosttestframe: monthosttestframe.in Makefile
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		-e 's|GPM=.*|GPM=$(GPM)|' \
		< $< > $@
	chmod +x $@

TOCLEAN+= squarehosttestframe
squarehosttestframe: squarehosttestframe.in Makefile
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		-e 's|GPM=.*|GPM=$(GPM)|' \
		< $< > $@
	chmod +x $@

TOCLEAN+= square4hosttestframe
square4hosttestframe: square4hosttestframe.in Makefile
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		-e 's|GPM=.*|GPM=$(GPM)|' \
		< $< > $@
	chmod +x $@

TOCLEAN+= cardtestframe
cardtestframe: cardtestframe.in Makefile
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		-e 's|GPM=.*|GPM=$(GPM)|' \
		< $< > $@
	chmod +x $@


#############################################################################
## Misc
##


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
