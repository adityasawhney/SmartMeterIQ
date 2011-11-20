
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "base number size in bits (exponent size according to Lenstra)"
set xrange [ 0 : 530 ]
#shortset xrange [ 0 : 128 ]
set xtics 16,16
set yrange [0 : 23]
set yrange [ 0 : ]
#shortset yrange [ 0 : 1]
#set yrange [ 0 : 16]
#set xrange [ 100 : 50 ]
#set xrange [ 0 : 100 ]
#set xrange [ 0 : 2048 ]
#set xrange [ 120 : 240 ]
#set yrange [0 : 10]
set ylabel "time in minutes"

set y2label "exponent size in bit"
set y2range [0 : 115]
set y2tics 25,25
#shortset y2range [0 : 40]
#set y2range [0 : 40]

f1(x) = a1*x**3 + b1*x**1 + c1*x + d1
f2(x) = a2*x**3 + b2*x**2 + c2*x + d2
#FIT_LIMIT = 1e-6

fit f1(x) 'vector-exp-base-5-fac-5-h3-wired.data' using 2:($5 / 60) via a1, b1, c1, d1
fit f2(x) 'vector-exp-base-5-fac-5-h3-wireless.data' \
          using 2:($5 / 60) via a2, b2, c2, d2

set title "multi exponent via simultaneous repeated squaring (all factors precomputed)"
#shortset title "multi exponent via simultaneous repeated squaring (up to 100 bits)"

set pointsize 1.5
#pngset pointsize 1

plot \
     "vector-exp-base-3-fac-3-h3-wired.data" using 2:($5 / 60) \
                     t '3 bases, 3 precomputed, wired', \
     "vector-exp-base-5-fac-5-h3-wired.data" using 2:($5 / 60) \
                     t '5 bases, 5 precomputed, wired', \
     f1(x) t "approximation", \
     "vector-exp-base-3-fac-3-h3-wireless.data" using 2:($5 / 60) \
                     t '3 bases, 3 precomputed, wireless', \
     "vector-exp-base-5-fac-5-h3-wireless.data" using 2:($5 / 60) \
                     t '5 bases, 5 precomputed, wireless', \
     f2(x) t "approximation", \
     "exponent-size.data" using 4:7 t 'exponent size' axes x1y2

