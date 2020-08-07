set xrange [0:10]
set xlabel 'Magnification'
set yrange [0.0:]

set ylabel 'Number of Node'

plot "plot.tsv"  with boxes notitle lw 1 lc rgb "blue",\
"plot.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'plot.eps'
set terminal postscript eps color
replot