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
## Created 6.10.08 by Hendrik
## 
## emulator wrapper test Makefile
## 
## $Id: Makefile,v 1.5 2010-09-20 13:55:35 tews Exp $


# load configuration
include ../../ConfigMakefile

# LocalMakefile overwrite config and let it set default target
-include LocalMakefile


# default build target
# if you don't like it, put one in LocalMakefile, eg:
# myfirsttarget: xxx
all: owrapper
byte: owrapper.byte


.PHONY: windows-cpp
windows-cpp:
	echo nothing to do here


TOCLEAN += jckit_path.ml
jckit_path.ml: jckit_path.ml.in
	sed -e "s|JCKIT_PATH|$(JCKIT222)|" < $^ > $@

TOCLEAN += owrapper
owrapper: owrapper.ml jckit_path.ml
	ocamlopt.opt -o owrapper unix.cmxa jckit_path.ml owrapper.ml

owrapper.byte: owrapper.ml jckit_path.ml
	ocamlc.opt -g -o owrapper unix.cma jckit_path.ml owrapper.ml


.PHONY: clean
clean:
	rm -rf $(TOCLEAN)
	rm -rf *.cmi *.cmo *.o *.cmx
