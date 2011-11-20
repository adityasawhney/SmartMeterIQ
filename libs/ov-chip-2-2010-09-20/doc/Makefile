## 
## OV-chip 2.0 project
## 
## Digital Security (DS) group at Radboud Universiteit Nijmegen
## 
## Copyright (C) 2009
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
## Created 16.2.09 by Hendrik
## 
## doc makefile
## 
## $Id: Makefile,v 1.15 2010-09-20 13:55:34 tews Exp $

GOSH=../tools/gosh/gosh
GOSHHTML=../tools/gosh/html.gosh

-include LocalMakefile

.PHONY: all
all: guide

.PHONY: core
core: guide

.PHONY: xdoc
xdoc:
	$(MAKE) -C .. doc

.PHONY: windows-cpp
windows-cpp:
	echo Nothing to do here


FIGURES:=applet-architecture long-apdu components
FIGUREFILES:=$(addsuffix .eps, $(FIGURES)) $(addsuffix .jpg, $(FIGURES))

hacker-guide.tex: hacker-guide
	$(GOSH) --style ov-guide.gosh $< > $@

.PHONY: guide
guide: $(FIGUREFILES) hacker-guide.tex
	$(GOSH) --style $(GOSHHTML) hacker-guide > hacker-guide.html
	$(MAKE) dolatex
	bibtex hacker-guide
	$(MAKE) dolatex
	$(MAKE) dolatex
	$(MAKE) hacker-guide.ps
	$(MAKE) dopdflatex


.PHONY: dolatex
dolatex:
	latex \\nonstopmode\\input{hacker-guide.tex}

.PHONY: dopdflatex
dopdflatex:
	pdflatex \\nonstopmode\\input{hacker-guide.tex}

hacker-guide-2up.ps: hacker-guide.dvi
	2up -o $@ hacker-guide.dvi


%.ps: %.dvi
	dvips -P outline -o $@ $<

# figures for latex
%.eps: %.fig
	fig2dev -Leps $< $@


%.eps: %.dia
	dia -t eps $<

%.jpg: %.eps
	convert $< $@
