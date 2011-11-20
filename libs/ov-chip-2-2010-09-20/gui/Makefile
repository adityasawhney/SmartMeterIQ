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
## Created 18.12.08 by Hendrik
## 
## GUI Makefile
## 
## $Id: Makefile,v 1.30 2010-09-20 13:55:35 tews Exp $


# load configuration
include ../ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


# default build target
# if you don't like it, put one in LocalMakefile, eg:
# myfirsttarget: xxx
.PHONY: all
all: gui


.PHONY: core
core: all

.PHONY: windows-cpp
windows-cpp: gui-cpp


# The name of the build dir is a bit more complicated now.
# That makes collissions more unlikely and simplifies the 
# related emacs lisp code.
BUILD:=_java_build_dir


JCCP:=$(JCKIT221)/lib/api.jar
JCEP:=$(JCKIT221)/api_export_files


#############################################################################
## GUI
##

gui: $(BUILD)/rsa_gui/javac_compiled

RSA_GUI_SOURCES:=\
	util/Action.java \
	util/Misc.java \
	util/Misc_host.java \
	util/APDU_Serializable.java \
	util/APDU_byte.java \
	util/APDU_short.java \
	util/APDU_boolean.java \
	util/APDU_byte_array.java \
	util/Serializable_array.java \
	util/BigIntUtil.java \
	util/Response_status.java \
	util/Response_apdu.java \
	util/Protocol_step.java \
	util/Protocol.java \
	util/Registered_protocols.java \
	util/Convert_serializable.java \
	util/Host_protocol.java \
	util/Security_parameter.java \
	util/Card_terminal.java \
	util/Unsecure_rsa_key.java \
	util/PrintWriter_APDUListener.java \
	util/Reference.java \
	util/Option.java \
	util/String_option.java \
	util/Int_option.java \
	util/Bool_option.java \
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
	front/PTLS_rsa_parameters.java \
	front/Hash_size.java \
	front/Signature.java \
	front/Host_signature.java \
	front/RSA_data.java \
	front/RSA_card_protocol_description.java \
	front/RSA_card_debug_description.java \
	front/Front_protocols.java \
	front/RSA_card_protocol_stubs.java \
	front/Applet_type.java \
	front/RSA_host_card.java \
	front/RSA_card_debug_stubs.java \
	front/RSA_card_debug.java \
	gui/Gui_state.java \
	gui/Card_protocols.java \
	gui/Gui_protocol_messages.java \
	gui/Gui_actions.java \
	gui/Bases_actions.java \
	gui/Config_actions.java \
	gui/Applet_list_actions.java \
	gui/Printable_aid.java \
	gui/Terminal_thread.java \
	gui/Ov_demo.java \
	netbeans/Config_window.java \
	netbeans/Base_selection.java \
	netbeans/Applet_list_window.java \
	netbeans/Progress_messages.java \
	netbeans/Ov_demo_gui.java


FULL_RSA_GUI_SOURCES:=\
	$(addprefix $(BUILD)/rsa_gui/ds/ov2/, $(RSA_GUI_SOURCES))


$(BUILD)/rsa_gui/javac_compiled: $(FULL_RSA_GUI_SOURCES)
	javac -classpath \
		$(BUILD)/rsa_gui:$(JCCP):$(GPM) \
		-Xlint \
		$(BUILD)/rsa_gui/ds/ov2/util/*.java \
		$(BUILD)/rsa_gui/ds/ov2/bignat/*.java \
		$(BUILD)/rsa_gui/ds/ov2/front/*.java \
		$(BUILD)/rsa_gui/ds/ov2/gui/*.java
	touch $@


$(BUILD)/rsa_gui/ds/ov2/directory-exists:
	mkdir -p $(BUILD)/rsa_gui/ds/ov2/util
	mkdir -p $(BUILD)/rsa_gui/ds/ov2/bignat
	mkdir -p $(BUILD)/rsa_gui/ds/ov2/front
	mkdir -p $(BUILD)/rsa_gui/ds/ov2/gui
	(cd $(BUILD)/rsa_gui/ds/ov2; ln -s gui netbeans)
	touch $(BUILD)/rsa_gui/ds/ov2/directory-exists

#.PRECIOUS: $(BUILD)/rsa_gui/ds/ov2/util/%.prejava
#.PRECIOUS: $(BUILD)/rsa_gui/ds/ov2/bignat/%.prejava
#.PRECIOUS: $(BUILD)/rsa_gui/ds/ov2/front/%.prejava
#.PRECIOUS: $(BUILD)/rsa_gui/ds/ov2/gui/%.prejava

$(BUILD)/rsa_gui/ds/ov2/util/%.prejava: ../util/%.java config \
		$(BUILD)/rsa_gui/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DHOST_TESTFRAME $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/rsa_gui/ds/ov2/bignat/%.prejava: ../bignat/%.java config \
		$(BUILD)/rsa_gui/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DHOST_TESTFRAME $(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/rsa_gui/ds/ov2/front/%.prejava: ../front/%.java config \
		$(BUILD)/rsa_gui/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat -DHOST_TESTFRAME \
		$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/rsa_gui/ds/ov2/gui/%.prejava: %.java config \
		$(BUILD)/rsa_gui/ds/ov2/directory-exists
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -I ../bignat -DHOST_TESTFRAME \
		$(CPPFLAGS) $$(pwd)/$< >> $@

$(BUILD)/rsa_gui/ds/ov2/netbeans/%.prejava: \
			netbeans/ov_demo_gui/src/ds/ov2/gui/%.java \
			$(BUILD)/rsa_gui/ds/ov2/directory-exists
	echo '# 1 "'$$(pwd)/$<'"' > $@
	cat $< >> $@


# additional dependencies
../front/RSA_card_protocol_description.java \
	../front/RSA_card_protocol_stubs.java \
	../front/RSA_card_debug_description.java \
	../front/RSA_card_debug_stubs.java:
	$(MAKE) -C ../front idl-files


.PHONY: gui-cpp
gui-cpp: $(FULL_RSA_GUI_SOURCES)

#############################################################################
## documentation
##

DOCFILES:= \
	package-info.java \
	Bases_actions.java \
	Card_protocols.java \
	Config_actions.java \
	Gui_actions.java \
	Gui_protocol_messages.java \
	Gui_state.java \
	Ov_demo.java \
	Ov_demo_gui.java \
	Config_window.java \
	Applet_list_actions.java \
	Applet_list_window.java \
	Base_selection.java \
	Printable_aid.java \
	Terminal_thread.java \
	Progress_messages.java

Ov_demo_gui.java: 
	ln -s netbeans/ov_demo_gui/src/ds/ov2/gui/Ov_demo_gui.java .

Config_window.java:
	ln -s netbeans/ov_demo_gui/src/ds/ov2/gui/Config_window.java .

Base_selection.java:
	ln -s netbeans/ov_demo_gui/src/ds/ov2/gui/Base_selection.java

Applet_list_window.java:
	ln -s netbeans/ov_demo_gui/src/ds/ov2/gui/Applet_list_window.java

Progress_messages.java:
	ln -s netbeans/ov_demo_gui/src/ds/ov2/gui/Progress_messages.java

../_doc_build_dir/ds/ov2/gui/doc_sources_copied: \
		$(addprefix ../_doc_build_dir/ds/ov2/gui/, $(DOCFILES))
	touch $@

../_doc_build_dir/ds/ov2/gui/%.prejava: %.java config
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

TOCLEAN+= gui
gui: gui.in Makefile
	sed -e 's|OFFCARD=.*|OFFCARD=$(OFFCARD)|' \
		-e 's|JCKIT=.*|JCKIT=$(JCKIT)|' \
		-e 's|GPM=.*|GPM=$(GPM)|' \
		< $< > $@
	chmod +x $@


#############################################################################
## Misc
##

.PRECIOUS: %.java

%.java: %.prejava
	rm -f $@
	sed -e 's|^# |//# |' <$< >$@
	chmod -w $@


tags: TAGS

.PHONY: TAGS
TAGS:
	etags --members *java ../util/*java ../bignat/*java


../tools/Applet_id.class: ../tools/Applet_id.java
	$(MAKE) -C ../tools Applet_id.class

../tools/converter_errors: ../tools/converter_errors.ml
	$(MAKE) -C ../tools converter_errors


gui-links:
	ln -s netbeans/ov_demo_gui/src/ds/ov2/gui/*java .


.PHONY: clean
clean:
	rm -rf $(TOCLEAN)
	rm -rf $(BUILD)
