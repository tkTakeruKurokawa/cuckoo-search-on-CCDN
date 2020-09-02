set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Storage Usage'

plot "Cost_of_Storage[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
"Cost_of_Storage[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
"Cost_of_Storage[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
"Cost_of_Storage[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_of_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Processing Usage'

plot "Cost_of_Processing[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
"Cost_of_Processing[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
"Cost_of_Processing[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
"Cost_of_Processing[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_of_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Transmission Usage'

plot "Cost_of_Transmission[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
"Cost_of_Transmission[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
"Cost_of_Transmission[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
"Cost_of_Transmission[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_of_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Total Cost'

plot "Cost_Total[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
"Cost_Total[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
"Cost_Total[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
"Cost_Total[Cuckoo_search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Requests'

plot "Number_of_Requests[Cuckoo_Search].tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output './eps/Number_of_Requests.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Hops'

plot "Number_of_Hops[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
"Number_of_Hops[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
"Number_of_Hops[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
"Number_of_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output './eps/Number_of_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Fails'

plot "Number_of_Fails[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
"Number_of_Fails[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
"Number_of_Fails[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
"Number_of_Fails[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output './eps/Number_of_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Hops'

plot "Average_Hops[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
"Average_Hops[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
"Average_Hops[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
"Average_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\
# "Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output './eps/Average_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Failed Servers'

plot "Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output './eps/Number_of_Failed_Servers.eps'
set terminal postscript eps color
replot
