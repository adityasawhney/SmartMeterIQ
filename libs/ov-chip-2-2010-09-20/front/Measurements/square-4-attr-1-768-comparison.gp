
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
set yrange [0 : 14]
set ylabel "time in sec"


set title "Square 4 applet"

set pointsize 1.5
#pngset pointsize 1

plot \
     "square-4-test-size-attr-1-h3-wired.resign-data" using 2:5 \
                                t 'resign h3' smooth sbezier with lines, \
     "square-4-test-size-attr-1-h3-wired.gate-data" using 2:5 \
                                t 'proof h3' smooth sbezier with lines, \
     "square-4-test-size-attr-1-blue-wireless.resign-data" using 2:5 \
                                t 'resign blue' smooth sbezier with lines, \
     "square-4-test-size-attr-1-blue-wireless.gate-data" using 2:5 \
                                t 'proof blue' smooth sbezier with lines, \
     "square-4-test-size-attr-1-768-h3-wired.resign-data" using 2:5 \
                                t 'resign h3 768' smooth sbezier with lines, \
     "square-4-test-size-attr-1-768-h3-wired.gate-data" using 2:5 \
                                t 'proof h3 768' smooth sbezier with lines, \
     "square-4-test-size-attr-1-768-blue-wireless.resign-data" using 2:5 \
                                t 'resign blue 768' smooth sbezier with lines, \
     "square-4-test-size-attr-1-768-blue-wireless.gate-data" using 2:5 \
                                t 'proof blue 768' smooth sbezier with lines
