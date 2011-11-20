
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color eps size 15cm,10cm
#epsset terminal postscript color eps size 7.5cm,5cm
#pngset terminal png

set key left
set xlabel "number size in bits"
set xrange [ 0 : 2100 ]
#set xrange [ 100 : 150 ]
set xtics 256,256
#shortset xtics 64,64
#shortset xrange [ 0 : 330 ]

set yrange [0 : ]
#epsset ytics 0,20
#eps#shortset ytics 0,1
set ylabel "time in sec"


f(x) = a*x**2 + b*x + c
g(x) = e*x**2 + f*x + g
#FIT_LIMIT = 1e-6

#fit f(x) 'montgomery-mult-h1.data' using ($2 * 8):5 via a, b, c
#fit g(x) 'montgomery-mult-h1-wireless.data' using ($2 * 8):5 via e, f, g


set title "Comparison of squared multiplication"

#exponent(a,b)=exp(log(a) * b)

set pointsize 1.5
#pngset pointsize 1

plot \
     "squared-mult-h3-wired.data" using 2:6 t '(a+b)^2 - a^2 - b^2', \
     "squared-mult-4-h3-wired.data" using 2:3 t '(a+b)^2 - (a - b)^2'

#     "squared-mult-h4-wired.data" using 2:6 t '(a+b)^2 - a^2 - b^2', \
