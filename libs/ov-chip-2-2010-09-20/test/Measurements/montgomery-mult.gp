
reset

set grid
unset mouse
set terminal x11 persist
#epsset terminal postscript color eps size 7.5cm,5cm
#pngset terminal png

set key left
set xlabel "number size in bits"
set xrange [ 0 : 2100 ]
#set xrange [ 100 : 150 ]
set xtics 256,256
#shortset xtics 32,32
#shortset xrange [ 0 : 330 ]

set yrange [0 : ]
#shortset yrange [0:3]
set ylabel "time in sec"


f(x) = a*x**2 + b*x + c
g(x) = e*x**2 + f*x + g
#FIT_LIMIT = 1e-6

#fit f(x) 'montgomery-mult-h1.data' using ($2 * 8):5 via a, b, c
#fit g(x) 'montgomery-mult-h1-wireless.data' using ($2 * 8):5 via e, f, g


set title "Montgomery multiplication"
#shortset title "Montgomery multiplication (detail up to 320 bits)"
#epsset title
#eps#shortset title "Montgomery multiplication"

exponent(a,b)=exp(log(a) * b)

set pointsize 1.5
#pngset pointsize 1

plot "montgomery-mult-h5-wired.data" using 2:6 t 'wired', \
     "montgomery-mult-h5-wireless.data" using 2:6 t 'wireless'


#     f(x) t "approximation",\
#     g(x) t "approximation"
     
