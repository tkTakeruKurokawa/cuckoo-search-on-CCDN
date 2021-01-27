filePath="./Nodes1000/"



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Storage Usage'
set key left top

plot filePath."/Cumulative_Storage[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
filePath."/Cumulative_Storage[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Storage[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
filePath."/Cumulative_Storage[Cuckoo_Search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Processing Usage'
set key left top

plot filePath."/Cumulative_Processing[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
filePath."/Cumulative_Processing[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Processing[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
filePath."/Cumulative_Processing[Cuckoo_Search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Transmission Usage'
set key left top

plot filePath."/Cumulative_Transmission[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
filePath."/Cumulative_Transmission[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Transmission[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
filePath."/Cumulative_Transmission[Cuckoo_Search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Total Usage'
set key left top

plot filePath."/Cumulative_Total[Original_Only].tsv" using 1:2  with lines title "Original Only"  lw 3 lc rgb "magenta",\
filePath."/Cumulative_Total[Random].tsv" using 1:2 with lines title "Random" lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Total[Greedy].tsv" using 1:2 with lines title "Greedy" lw 3 lc rgb "blue",\
filePath."/Cumulative_Total[Cuckoo_search].tsv" using 1:2 with lines title "Proposed" lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Request Hops'
set key left top

plot filePath."/Cumulative_Hops[Original_Only].tsv" using 1:2 with lines title "Original Only" lw 3 lc rgb "magenta",\
filePath."/Cumulative_Hops[Random].tsv" using 1:2  with lines title "Random"  lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Hops[Greedy].tsv" using 1:2  with lines title "Greedy"  lw 3 lc rgb "blue",\
filePath."/Cumulative_Hops[Cuckoo_Search].tsv" using 1:2  with lines title "Proposed"  lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Failed Requests'
set key left top

plot filePath."/Cumulative_Fails[Original_Only].tsv" using 1:2 with lines title "Original Only" lw 3 lc rgb "magenta",\
filePath."/Cumulative_Fails[Random].tsv" using 1:2  with lines title "Random"  lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Fails[Greedy].tsv" using 1:2  with lines title "Greedy"  lw 3 lc rgb "blue",\
filePath."/Cumulative_Fails[Cuckoo_Search].tsv" using 1:2  with lines title "Proposed"  lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Number of Requests'

plot filePath."/Number_of_Requests.tsv" using 1:2 with lines notitle lw 3 lc rgb "magenta"

unset table
set output filePath.'/eps/Number_of_Requests.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Number of Failure Servers'

plot filePath."/Number_of_Failure_Servers.tsv" using 1:2 with lines notitle lw 3 lc rgb "magenta"

unset table
set output filePath.'/eps/Number_of_Failure_Servers.eps'
set terminal postscript eps color
replot



set autoscale x
# set yrange
set xlabel 'Place of Failed Hop'
set ylabel 'Number of Failed Requests'
set key right top

plot filePath."/Failed_Hops_Distribution[Original_Only].tsv" using 1:2 with lines title "Original Only" lw 3 lc rgb "magenta",\
filePath."/Failed_Hops_Distribution[Random].tsv" using 1:2  with lines title "Random"  lw 3 lc rgb "forest-green",\
filePath."/Failed_Hops_Distribution[Greedy].tsv" using 1:2  with lines title "Greedy"  lw 3 lc rgb "blue",\
filePath."/Failed_Hops_Distribution[Cuckoo_Search].tsv" using 1:2  with lines title "Proposed"  lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Failed_Request_Hops.eps'
set terminal postscript eps color
replot



set autoscale x
set yrange[0.0:1.0]
set xlabel 'Number of Cycles'
set ylabel 'Success Ratio of Requests'
set key left bottom

plot filePath."/Success_Ratio[Original_Only].tsv" using 1:2 with lines title "Original Only" lw 3 lc rgb "magenta",\
filePath."/Success_Ratio[Random].tsv" using 1:2  with lines title "Random"  lw 3 lc rgb "forest-green",\
filePath."/Success_Ratio[Greedy].tsv" using 1:2  with lines title "Greedy"  lw 3 lc rgb "blue",\
filePath."/Success_Ratio[Cuckoo_Search].tsv" using 1:2  with lines title "Proposed"  lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Success_Ratio.eps'
set terminal postscript eps color
replot
