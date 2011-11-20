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
## $Id: Makefile,v 1.7 2010-09-20 13:55:35 tews Exp $


.PHONY: all
all: Applet_id.class converter_errors

.PHONY: core
core: all

.PHONY: windows-cpp
windows-cpp:
	echo nothing to do here for windows-zip


Applet_id.class: Applet_id.java
	javac Applet_id.java

TOCLEAN+=converter_errors
converter_errors: converter_errors.ml
	ocamlopt.opt -o converter_errors converter_errors.ml

TOCLEAN+=port_dump
port_dump: port_dump.ml
	ocamlopt.opt -o port_dump unix.cmxa port_dump.ml

TOCLEAN+=xor_bytes
xor_bytes:
	ocamlopt.opt -o xor_bytes xor_bytes.ml

clean:
	rm -f $(TOCLEAN)
	rm -f *.class *.{cmi,cmo,cmx,o} 
	rm -f a.out
