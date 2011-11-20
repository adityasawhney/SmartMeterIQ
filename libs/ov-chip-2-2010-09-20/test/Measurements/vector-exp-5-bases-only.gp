
reset

set grid
unset mouse
set terminal x11 persist
#epsset terminal postscript color eps size 7cm,6cm
#pngset terminal png

set key left
set xlabel "base number size in bits"
set xrange [ 0 : 530 ]
#set xrange [ 0 : 1300 ]
#shortset xrange [ 0 : 100 ]
#set xrange [ 0 : 100 ]
set xtics 128,128
set yrange [0 : ]
#set yrange [ 0 : 130]
#shortset yrange [ 0 : 16]
#set yrange [ 0 : 16]
#set xrange [ 100 : 50 ]
#set xrange [ 0 : 100 ]
#set xrange [ 0 : 2048 ]
#set xrange [ 120 : 240 ]
#set yrange [0 : 10]
set ylabel "time in min"

set y2label "exponent size in bit"
set y2tics
#set y2range [0 : 65]
#shortset y2range [0 : 40]
#set y2range [0 : 40]

f1(x) = a1*x**3 + b1*x**2 + c1*x + d1
f2(x) = a2*x**3 + b2*x**2 + c2*x + d2
#FIT_LIMIT = 1e-6

set title "multi exponent via simultaneous repeated squaring"
#shortset title "multi exponent via simultaneous repeated squaring (up to 100 bits)"

set pointsize 1.5
#pngset pointsize 1

fit f1(x) 'vector-exp-base-5-fac-5-h3-wired.data' using 2:($5 / 60) via a1, b1, c1, d1


plot \
     "vector-exp-base-5-fac-5-h3-wired.data" using 2:($5 / 60) \
                     t 'wired', \
     f1(x) t 'approx', \
     "vector-exp-base-5-fac-5-h3-wireless.data" using 2:($5 / 60) \
                     t 'wireless', \
     "exponent-size.data" using 4:7 t 'exponent' axes x1y2

