
reset

set grid
unset mouse
set terminal x11 persist
#pngset terminal png

set key left
set xlabel "base number size in bits (exponent size according to Lenstra)"
set xrange [ 0 : 2100 ]
set xtics 256,256
set yrange [0 : 11]
#set ytics 1,1
set ylabel "time in sec"

set y2label "exponent size in bit"
set y2tics 40,40
set y2range [0 : 220]
#shortset y2range [0 : 40]
#set y2range [0 : 40]

set title "Java Card - NFC phone performance comparison on multi power\nThe card uses the RSA cipher and squaring multiplication. The phone uses simultaneous\nsquaring with Montgomery multiplication in the int/long Bignat configuration."

set pointsize 1.5
#pngset pointsize 1

plot "vector-squared-rsa-exp-base-5-h3-wired.data" using 2:5 \
                     t 'card 5 bases, wired', \
     "vector-squared-rsa-exp-base-5-h3-wireless.data" using 2:5 \
                     t 'card 5 bases, wireless', \
     "phone-6212-vector-exp-base-5.data" using 2:5 \
                     t 'phone, 6212, 5 bases, NFC', \
     "phone-vector-exp-base-5.data" using 2:5 \
                     t 'phone, 6131, 5 bases, NFC', \
     "exponent-size.data" using 4:7 t 'exponent size' axes x1y2
