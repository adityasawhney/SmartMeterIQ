
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "divident size in bits"
set xrange [ 0 : 2050 ]
#shortset xrange [ 0 : 330 ]
#set xrange [ 100 : 150 ]
#set xrange [ 0 : 330 ]
#set xrange [ 120 : 240 ]
#set yrange [0 : 10]
set ylabel "time in sec"

f(x) = b*x + c
#FIT_LIMIT = 1e-6

#fit f(x) 'div-equal-h1.data' using ($2 * 8):5 via b, c


set title "Division (divisor same size as divident)"
#shortset title "Division (divisor same size as divident, detail up to 320 bits)"

#exponent(a,b)=exp(log(a) * b)

set pointsize 1.5
#pngset pointsize 1

plot "div-equal-h1.data" using ($2 * 8):5 t 'wired',\
     "div-equal-h1-wireless.data" using ($2 * 8):5 t 'wireless'

#f(x) t "approximation"
     

