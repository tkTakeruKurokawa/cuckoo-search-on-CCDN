set xrange [0.9:3.1]
set xlabel 'Magnification'
# set yrange [0.0:15]


set ylabel 'Number of Nodes'

plot "plot.tsv"  with boxes notitle lw 1 lc rgb "blue",\
"plot.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'plot.eps'
set terminal postscript eps color
replot