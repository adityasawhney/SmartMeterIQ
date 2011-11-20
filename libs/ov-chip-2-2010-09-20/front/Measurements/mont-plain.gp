
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color eps size 15cm,10cm
#pngset terminal png

set key right
set xlabel "RSA key size in bits (exponent size according to Lenstra)"
#set xrange [ 500 :  ]
set xtics 0,128
set yrange [0 : ]
set ylabel "time in minutes"

f1(x) = a1*x**3 + b1*x**2 + c1*x + d1
f2(x) = a2*x**3 + b2*x**2 + c2*x + d2
#FIT_LIMIT = 1e-6

#fit f1(x) 'test-size-h3-wired.gate-data' using 2:($5 / 60) via a1, b1, c1, d1
#fit f2(x) 'mont-test-size-h3-wired.gate-data' \
#          using 2:($5 / 60) via a2, b2, c2, d2

set title "Plain and Montgomerizing RSA applet (4 bases, wired)"

set pointsize 1.5
#pngset pointsize 1

plot \
     "test-size-h3-wired.resign-data" using 2:($5 / 60) t 'resigning plain', \
     "test-size-h3-wired.gate-data" using 2:($5 / 60) t 'prove plain', \
     "mont-test-size-h3-wired.resign-data" using 2:($5 / 60) \
                                                  t 'resigning mont', \
     "mont-test-size-h3-wired.gate-data" using 2:($5 / 60) t 'prove mont'

#     f2(x) t "approximation"

