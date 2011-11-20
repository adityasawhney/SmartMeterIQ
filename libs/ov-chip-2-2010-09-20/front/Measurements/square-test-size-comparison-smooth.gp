
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "RSA key size in bits (exponent size according to Lenstra)"
set xrange [ 460 : 2030 ]
set xtics 512,128
set yrange [0 : ]
set ylabel "time in sec"

#f1(x) = a1*x**3 + b1*x**1 + c1*x + d1
#f2(x) = a2*x**3 + b2*x**2 + c2*x + d2
#FIT_LIMIT = 1e-6

#fit f1(x) 'vector-exp-base-5-fac-5-h5-wired.data' using 2:5 via a1, b1, c1, d1
#fit f2(x) 'vector-exp-base-5-fac-5-h3-wireless.data' \
#          using 2:5 via a2, b2, c2, d2

set title "Square 2 / square 4 comparison for the OV Chip protocols (4 bases, wired, RSA)"

set pointsize 1.5
#pngset pointsize 1

plot \
     "square-test-size-h3-wired.resign-data" using 2:5 \
                                t 'square 2 resign' smooth sbezier with lines, \
     "square-test-size-h3-wired.gate-data" using 2:5 \
                                t 'square 2 proof' smooth sbezier with lines, \
     "square-4-test-size-h3-wired.resign-data" using 2:5 \
                                t 'square 4 resign' smooth sbezier with lines, \
     "square-4-test-size-h3-wired.gate-data" using 2:5 \
                                t 'square 4 proof' smooth sbezier with lines
