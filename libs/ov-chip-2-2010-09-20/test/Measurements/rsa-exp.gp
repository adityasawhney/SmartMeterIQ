
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "base size in bits"
set xrange [ 0 : 2050 ]
set xtics 256,256
set yrange [ 0 : 0.8]
#shortset xrange [ 500 : 1400 ]
#shortset yrange [ 0 : 0.3 ]
#set xrange [ 0 : 320 ]
#set xrange [ 120 : 240 ]
#set yrange [0 : 10]
set ylabel "time in sec"

set y2label "exponent size in bit"
set y2range [0 : 240]
set y2tics 60,60
#shortset y2tics 30,30
#set y2range [0 : 65]
#shortset y2range [0 : 180]
#set y2range [0 : 40]

f1(x) = a1*x**2 + b1*x + c1
f2(x) = a2*x**2 + b2*x + c2
#FIT_LIMIT = 1e-6

#fit f1(x) 'rsa-exp-h5-wired.data' using 2:8 via a1, b1, c1
#fit f2(x) 'rsa-exp-h3-wireless.data' using 2:6 via a2, b2, c2


set title "modPow implemented with RSA, exponent size according to Lenstra"
#shortset title "modPow implemented with RSA, exponent size acc. to Lenstra (detail)"


#exponent(a,b)=exp(log(a) * b)

set pointsize 1.5
#pngset pointsize 1

plot "rsa-exp-h5-wired.data" using 2:8 t 'wired encrypt only',\
     "rsa-exp-h5-wired.data" using 2:11 t 'wired modPow',\
     "rsa-exp-h3-wireless.data" using 2:8 t 'wireless encrypt only',\
     "rsa-exp-h3-wireless.data" using 2:11 t 'wireless modPow', \
     "exponent-size.data" using 4:7 t 'exponent size' axes x1y2

