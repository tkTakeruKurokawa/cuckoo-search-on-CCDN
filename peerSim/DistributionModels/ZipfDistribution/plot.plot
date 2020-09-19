set xrange [0:]
set xlabel 'Content Identifier'
set yrange [0:]
set ylabel 'Popularity'

plot "plot.tsv"  with boxes notitle lw 1 lc rgb "blue",\
"plot.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'plot.eps'
set terminal postscript eps color
replot