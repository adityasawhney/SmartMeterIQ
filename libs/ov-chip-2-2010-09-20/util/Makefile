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
## util Makefile
## 
## $Id: Makefile,v 1.19 2009-06-02 09:56:03 tews Exp $


-include LocalMakefile


#############################################################################
## documentation
##

DOCFILES:= \
	package-info.java \
	Action.java \
	APDU_boolean.java \
	APDU_byte.java \
	APDU_byte_array.java \
	APDU_long.java \
	APDU_Serializable.java \
	APDU_short_array.java \
	APDU_short.java \
	BigIntUtil.java \
	Card_protocol.java \
	Card_terminal.java \
	Convert_serializable.java \
	Empty_void_method.java \
	Host_protocol.java \
	Installation_arguments.java \
	Message_digest_wrapper.java \
	Misc_host.java \
	Misc.java \
	Protocol.java \
	Protocol_applet.java \
	Protocol_step.java \
	Registered_protocols.java \
	Resizable_buffer.java \
	Response_apdu.java \
	Response_status.java \
	Security_parameter.java \
	Serializable_array.java \
	Unsecure_rsa_key.java \
	Void_method.java \
	PrintWriter_APDUListener.java \
	Reference.java \
	Option.java \
	Bool_option.java \
	Int_option.java \
	String_option.java \
	Value_option.java \
	Bool_int_option.java \
	BigInteger_inputs.java \
	BigInt_input_option.java \
	BigInt_hex_input_option.java \
	Commandline.java \
	Parse_commandline.java


../_doc_build_dir/ds/ov2/util/doc_sources_copied: \
		$(addprefix ../_doc_build_dir/ds/ov2/util/, $(DOCFILES))
	touch $@

../_doc_build_dir/ds/ov2/util/%.prejava: %.java config
	cat ../not-edit-warning.java > $@
	cpp -C -nostdinc -I . -DJAVADOC $(CPPFLAGS) $$(pwd)/$< >> $@



#############################################################################
## misc
##

.PRECIOUS: %.java

%.java: %.prejava
	rm -f $@
	sed -e 's|^# |//# |' <$< >$@
	chmod -w $@

clean:
	echo nothing to clean here
