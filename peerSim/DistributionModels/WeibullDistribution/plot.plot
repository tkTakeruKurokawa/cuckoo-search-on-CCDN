# set xrange [0:10]
set xlabel 'Node Identifier'
set yrange [0.0:0.01]

set ylabel 'Failure Rate'

plot "plot.tsv"  with boxes notitle lw 1 lc rgb "blue",\
"plot.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'plot.eps'
set terminal postscript eps color
replot