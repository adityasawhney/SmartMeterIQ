## 
## OV-chip 2.0 project
## 
## Digital Security (DS) group at Radboud Universiteit Nijmegen
## 
## Copyright (C) 2008, 2009, 2009
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
## Created 12.9.08 by Hendrik
## 
## test plots Makefile
## 
## $Id: Makefile,v 1.16 2010-02-18 11:42:50 tews Exp $

GRAPHS:= montgomery-mult.png montgomery-mult.detail.png \
	div.png div.detail.png \
	div-equal.png div-equal.detail.png \
	vector-exp.png vector-exp-detail.png \
	rsa-exp-small-exp.png rsa-exp-big-exp.png \
	rsa-exp.png rsa-exp.detail.png \
	vector-rsa-exp.png \
	new-nxp.png \
	squared-mult.png short-squared-mult.png \
	vector-squared-rsa-exp.png \
	phone-card-vector.png


FIRSTS:=vector-squared-rsa-exp-base-4-h4-wired.data-without-first \
	vector-squared-4-rsa-exp-base-4-h3-wired.data-without-first \
	vector-squared-rsa-exp-base-4-h3-wired.data-without-first


all: $(GRAPHS) $(FIRSTS)


div.png: div-h2.data div-h1-wireless.data
div.detail.png: div-h2.data div-h1-wireless.data

vector-rsa-exp-simple.eps: vector-rsa-exp-base-4-h3-wired.data \
				vector-rsa-exp-base-4-h4-wireless.data


%.detail.gp: %.gp
	sed -e 's/#shortset/set/' < $^ > $@

%.png.gp: %.gp
	sed -e 's/#pngset/set/' < $^ > $@

%.png: %.png.gp
	gnuplot $< > $@


%.eps.gp: %.gp
	sed -e 's/#epsset/set/' < $^ > $@

%.eps: %.eps.gp
	gnuplot $< > $@



%.data-without-first: %.data
	sed -e '/## Effective size/ {n;d}' < $^ > $@


clean:
	rm -f *.png *.detail.gp *.eps.gp *330.gp *.eps
