
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color eps size 15cm,10cm
#pngset terminal png

set key left
set xlabel "number size in bits"
set xrange [ 0 : 520 ]
#set xrange [ 100 : 150 ]
set xtics 64,64
#shortset xtics 64,64
#shortset xrange [ 0 : 330 ]

set yrange [0 : 0.3]
#epsset ytics 0,20
#eps#shortset ytics 0,1
set ylabel "time in sec"


f(x) = a*x**2 + b*x + c
g(x) = e*x**2 + f*x + g
#FIT_LIMIT = 1e-6

#fit f(x) 'montgomery-mult-h1.data' using ($2 * 8):5 via a, b, c
#fit g(x) 'montgomery-mult-h1-wireless.data' using ($2 * 8):5 via e, f, g


set title "Comparison of short squared multiplications"
#shortset title "Montgomery multiplication (detail up to 320 bits)"
#epsset title
#eps#shortset title "Montgomery multiplication"

#exponent(a,b)=exp(log(a) * b)

set pointsize 1.5
#pngset pointsize 1

plot \
     "short-squared-mult-h3-wired.data" using 2:6 t 'short square 2 multiplication, wired', \
     "short-square-4-mult-h3-wired.data" using 2:5 t 'short square 4 multiplication, wired', \
     "normal-mult-h4-wired.data" using 2:6 t 'normal multiplication, wired'

#     f(x) t "approximation",\
#     g(x) t "approximation"
     
