
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "base number size in bits (exponent size according to Lenstra)"
set xrange [ 450 : 2080]
set xtics 512,128
set yrange [0 : ]
set ylabel "time in sec"
#
set y2label "exponent size in bit"
set y2tics
set y2range [0 : 225]
#shortset y2range [0 : 40]
#set y2range [0 : 40]

f1(x) = a1*x**3 + b1*x**1 + c1*x + d1
f2(x) = a2*x**3 + b2*x**2 + c2*x + d2
#FIT_LIMIT = 1e-6

#fit f1(x) 'vector-exp-base-5-fac-5-h5-wired.data' using 2:5 via a1, b1, c1, d1
#fit f2(x) 'vector-exp-base-5-fac-5-h3-wireless.data' \
#          using 2:5 via a2, b2, c2, d2

set title "multi power via RSA encryption and Montgomery multiplication"

set pointsize 1.5
#pngset pointsize 1

plot \
     "exponent-size.data" using 4:7 t 'exponent size' axes x1y2, \
     "vector-rsa-exp-base-5-h3-wired.data" using 2:5 \
                     t '5 bases, wired', \
     "vector-rsa-exp-base-5-h3-wireless.data" using 2:5 \
                     t '5 bases, wireless', \
     "vector-rsa-exp-base-4-h3-wired.data" using 2:5 \
     		     t '4 bases, wired', \
     "vector-rsa-exp-base-4-h4-wireless.data" using 2:5 \
     		     t '4 bases, wireless'

