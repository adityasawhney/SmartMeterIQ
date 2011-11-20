
reset

set grid
unset mouse
set terminal x11 persist
#set terminal postscript color eps size 15cm,10cm
#set terminal postscript color
#pngset terminal png

set key left
set xlabel "base size in bits"
set xrange [ 0 : 2100 ]
#set yrange [ 0 : 300]
#set yrange [ 0 : 1]
#set xrange [ 100 : 150 ]
#set xrange [ 0 : 500 ]
#set xrange [ 120 : 240 ]
#set yrange [0 : 10]
set ylabel "mult time in sec"

set y2label "rsa time in sec"
set y2range [0 : 0.5]
set y2tics 0.1


#f1(x) = a1*x**2 + b1*x + c1
#f2(x) = a2*x**2 + b2*x + c2
#FIT_LIMIT = 1e-6

#fit f1(x) 'rsa-exp-h5-wired.data' using 2:8 via a1, b1, c1
#fit f2(x) 'rsa-exp-h3-wireless.data' using 2:6 via a2, b2, c2


set title "new NXP card performance"

#exponent(a,b)=exp(log(a) * b)

#set pointsize 1.5
#pngset pointsize 1

plot "montgomery-mult-bluenxp-wireless.data" using 2:6 \
                  t 'mont mult new NXP card wireless' axes x1y1, \
     "montgomery-mult-h5-wireless.data" using 2:6 \
                  t 'mont mult JCOP 4.1 wireless' axes x1y1, \
     "montgomery-mult-h5-wired.data" using 2:6 \
                  t 'mont mult JCOP 4.1 wired' axes x1y1, \
     "rsa-exp-exp-200-bluenxp-wireless.data" using 2:8 \
                  t '200 bit rsa exponent new NXP card wireless' axes x1y2, \
     "rsa-exp-exp-200-h5-wireless.data" using 2:8 \
                  t '200 bit rsa exponent JCOP 4.1 wireless' axes x1y2, \
     "rsa-exp-exp-200-h5-wired.data" using 2:8 \
                  t '200 bit rsa exponent JCOP 4.1 wired' axes x1y2
