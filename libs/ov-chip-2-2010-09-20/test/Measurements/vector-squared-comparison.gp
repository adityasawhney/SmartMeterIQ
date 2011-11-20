
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color eps size 15cm,10cm
#pngset terminal png

set key left
set xlabel "base number size in bits (exponent size according to Lenstra)"
set xrange [ 450 : 2060 ]
set xtics 512,128
set yrange [0 : 10]
set ytics 1,1
set ylabel "time in sec"

set y2label "exponent size in bit"
set y2tics 0,20
set y2range [0 : 200]
#shortset y2range [0 : 40]
#set y2range [0 : 40]

f1(x) = a1*x**3 + b1*x**1 + c1*x + d1
f2(x) = a2*x**3 + b2*x**2 + c2*x + d2
#FIT_LIMIT = 1e-6

set title "squared 2 / squared 4 comparison for modular vector powers"

set pointsize 1.5
#pngset pointsize 1

plot \
     "vector-squared-rsa-exp-base-4-h3-wired.data" using 2:5 \
                     t '4 bases, squared 2', \
     "vector-squared-4-rsa-exp-base-4-h3-wired.data" using 2:5 \
                     t '4 bases, squared 4', \
     "exponent-size.data" using 4:7 t 'exponent size' axes x1y2