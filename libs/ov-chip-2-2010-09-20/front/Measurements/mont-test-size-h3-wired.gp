
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color eps size 15cm,10cm
#pngset terminal png

set key left
set xlabel "RSA key size in bits (exponent size according to Lenstra)"
set xrange [0 : 530 ]
#shortset xrange [0: 180]
set xtics 32,32
set yrange [0 : ]
set ylabel "time in minutes"

#f1(x) = a1*x**3 + b1*x**1 + c1*x + d1
#f2(x) = a2*x**3 + b2*x**2 + c2*x + d2
#FIT_LIMIT = 1e-6

#fit f1(x) 'vector-exp-base-5-fac-5-h5-wired.data' using 2:5 via a1, b1, c1, d1
#fit f2(x) 'vector-exp-base-5-fac-5-h3-wireless.data' \
#          using 2:5 via a2, b2, c2, d2

set title "Basic OV Chip protocols, Montgomery variant (4 bases, wired, Montgomery only)"

set pointsize 1.5
#pngset pointsize 1

plot \
     "mont-test-size-h3-wired.resign-data" using 2:($9 / 60) \
                                             t 'resigning, step 1', \
     "mont-test-size-h3-wired.resign-data" using 2:($10 / 60) \
                                             t 'resigning, step 2', \
     "mont-test-size-h3-wired.resign-data" using 2:($5 / 60) \
                                             t 'resigning, total time', \
     "mont-test-size-h3-wired.gate-data" using 2:($8 / 60) t 'prove, step 1', \
     "mont-test-size-h3-wired.gate-data" using 2:($9 / 60) t 'prove, step 2', \
     "mont-test-size-h3-wired.gate-data" using 2:($5 / 60) t 'prove, total time'

