set xrange [0:100]
set xlabel 'Content Size(MB)'
set yrange [0:]
set ylabel 'Number of Contents'

plot "plot.tsv"  with boxes notitle lw 1 lc rgb "blue",\
"plot.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'plot.eps'
set terminal postscript eps color
replot