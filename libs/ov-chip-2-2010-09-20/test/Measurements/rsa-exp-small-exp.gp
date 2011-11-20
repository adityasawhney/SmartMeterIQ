
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "base size in bits"
set xrange [ 0 : 2050 ]
set yrange [ 0 : 0.3 ]
#set xrange [ 100 : 150 ]
#set xrange [ 0 : 320 ]
#set xrange [ 120 : 240 ]
#set yrange [0 : 10]
set ylabel "time in sec"

#f1(x) = a1*x**2 + b1*x + c1
#f2(x) = a2*x**2 + b2*x + c2
#FIT_LIMIT = 1e-6

#fit f1(x) 'rsa-exp-h5-wired.data' using 2:8 via a1, b1, c1
#fit f2(x) 'rsa-exp-h3-wireless.data' using 2:6 via a2, b2, c2


set title "RSA public key encryption with fixed exponent and varying base size"

#exponent(a,b)=exp(log(a) * b)

set pointsize 1.5
#pngset pointsize 1

plot "rsa-exp-exp-17-h5-wired.data" using 2:8 t '17 bit exponent', \
     "rsa-exp-exp-104-h5-wired.data" using 2:8 t '104 bit exponent', \
     "rsa-exp-exp-200-h5-wired.data" using 2:8 t '200 bit exponent', \
     "rsa-exp-exp-512-h5-wired.data" using 2:8 t '512 bit exponent'
