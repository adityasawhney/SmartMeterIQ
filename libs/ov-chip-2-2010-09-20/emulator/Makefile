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
## subdirectory distributor makefile
## 
## $Id: Makefile,v 1.6 2010-09-20 13:55:35 tews Exp $


SUBDIRS:=jcop smartcardio wrapper

.PHONY: all
all:
	for d in $(SUBDIRS) ; do $(MAKE) -C $$d all || exit; done


.PHONY: windows-cpp
windows-cpp:
	for d in $(SUBDIRS) ; do $(MAKE) -C $$d windows-cpp || exit; done


.PHONY: clean
clean:
	for d in $(SUBDIRS) ; do $(MAKE) -C $$d clean; done


.PHONY: core
core: 
	@echo Nothing to do here, because there are only optional parts.
