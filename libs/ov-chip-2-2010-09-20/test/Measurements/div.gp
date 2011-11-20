
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "divident size in bits"
set xrange [ 0 : 2100 ]
set xtics 256,256
#shortset xrange [ 0 : 330 ]
#set xrange [ 100 : 150 ]
#set xrange [ 0 : 320 ]
#set xrange [ 120 : 240 ]
set yrange [0 :]
set ylabel "time in sec"

f1(x) = a1*x**2 + b1*x + c1
f2(x) = a2*x**2 + b2*x + c2
#FIT_LIMIT = 1e-6

fit f1(x) 'div-50-h3.data' using 2:5 via a1, b1, c1
fit f2(x) 'div-h1-wireless.data' using ($2 * 8):5 via a2, b2, c2


set title "Division (divisor half as long as divident)"
#shortset title "Division (divisor half as long as divident, detail up to 320 bits)"

#exponent(a,b)=exp(log(a) * b)

set pointsize 1.5
#pngset pointsize 1

plot \
     "div-50-h3.data" using 2:5 t 'wired', \
     f1(x) t "approximation", \
     "div-h1-wireless.data" using ($2 * 8):5 t 'wireless',\
     f2(x) t "approximation"
     

