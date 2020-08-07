set xlabel 'Cycle'
set ylabel 'Number of Faiure Node'
set yrange [0:10]

set style fill solid
plot "plot.tsv"  with boxes notitle lw 1 lc rgb "blue"
# plot "plot.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'plot.eps'
set terminal postscript eps color
replot