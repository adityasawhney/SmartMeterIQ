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
## Created 11.12.08 by Hendrik
## 
## frontoffice performance plots Makefile
## 
## $Id: Makefile,v 1.13 2010-09-20 11:20:15 tews Exp $

GRAPHS:=test-size-h3-wired.png test-size-h3-wireless.png \
	mont-shortcut-resign.png mont-shortcut-gate.png mont-shortcut.png \
	square-test-size-h3-wired.png square-test-size-h3-wired-smooth.png \
	mont-test-size-h3-wired.png mont-plain.png

all: $(GRAPHS)

data: mont-shortcut-h3-wired.resign-data \
	mont-shortcut-h3-wired.gate-data \
	test-size-h3-wired.resign-data \
	test-size-h3-wired.gate-data \
	test-size-h3-wireless.resign-data \
	test-size-h3-wireless.gate-data \
	test-size-blue-wireless.resign-data \
	test-size-blue-wireless.gate-data \
	mont-test-size-h3-wired.resign-data \
	mont-test-size-h3-wired.gate-data \
	square-test-size-h3-wired.resign-data \
	square-test-size-h3-wired.gate-data \
	square-test-size-attr-2-h3-wired.resign-data \
	square-test-size-attr-2-h3-wired.gate-data \
	square-4-test-size-h3-wired.resign-data \
	square-4-test-size-h3-wired.gate-data \
	square-4-test-size-attr-2-h3-wired.resign-data \
	square-4-test-size-attr-2-h3-wired.gate-data \
	square-4-test-size-attr-1-h3-wired.resign-data \
	square-4-test-size-attr-1-h3-wired.gate-data \
	square-4-test-size-attr-1-blue-wireless.resign-data \
	square-4-test-size-attr-1-blue-wireless.gate-data \
	square-4-test-size-attr-1-768-h3-wired.resign-data \
	square-4-test-size-attr-1-768-h3-wired.gate-data \
	square-4-test-size-attr-1-768-blue-wireless.resign-data \
	square-4-test-size-attr-1-768-blue-wireless.gate-data \

partial: mont-test-size-h3-wired-simple.eps test-size-h3-wired-simple.eps
	cp $^ ../../../documents/papers/partial-experience

test-size-h3-wired.png: \
	test-size-h3-wired.resign-data \
	test-size-h3-wired.gate-data

test-size-h3-wireless.png: \
	test-size-h3-wireless.resign-data \
	test-size-h3-wireless.gate-data

mont-shortcut-resign.png mont-shortcut-gate.png mont-shortcut.png: \
	mont-shortcut-h3-wired.resign-data \
	mont-shortcut-h3-wired.gate-data

square-test-size-h3-wired.png square-test-size-h3-wired-smooth.png: \
	square-test-size-h3-wired.resign-data \
	square-test-size-h3-wired.gate-data

mont-test-size-h3-wired.png: \
	mont-test-size-h3-wired.resign-data \
	mont-test-size-h3-wired.gate-data


%.resign-data: %.data
	grep '^resign' < $< > $@

%.gate-data: %.data
	grep '^gate' < $< > $@

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


clean:
	rm -f *.png *.png.gp *.resign-data *.gate-data
