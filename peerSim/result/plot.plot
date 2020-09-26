filePath="./Nodes100"

set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Storage Usage'

plot filePath."/Average_Storage[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Average_Storage[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Average_Storage[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Average_Storage[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Processing Usage'

plot filePath."/Average_Processing[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Average_Processing[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Average_Processing[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Average_Processing[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Transmission Usage'

plot filePath."/Average_Transmission[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Average_Transmission[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Average_Transmission[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Average_Transmission[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Total Usage'

plot filePath."/Average_Total[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Average_Total[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Average_Total[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Average_Total[Cuckoo_search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Cumulative Total Usage'

plot filePath."/Cumulative_Total[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Cumulative_Total[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Cumulative_Total[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Cumulative_Total[Cuckoo_search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Request Hops'

plot filePath."/Average_Hops[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Average_Hops[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Average_Hops[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Average_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Cumulative Request Hops'

plot filePath."/Cumulative_Hops[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Cumulative_Hops[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Cumulative_Hops[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Cumulative_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Failed Requests'

plot filePath."/Average_Fails[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Average_Fails[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Average_Fails[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Average_Fails[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Cumulative Failed Requests'

plot filePath."/Cumulative_Fails[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Cumulative_Fails[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Cumulative_Fails[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Cumulative_Fails[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:] 
set xlabel 'Cycle'
set ylabel 'Cumulative Lack of Processing'

plot filePath."/Cumulative_Lack_of_Processing[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Cumulative_Lack_of_Processing[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Cumulative_Lack_of_Processing[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Cumulative_Lack_of_Processing[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\
# "Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output filePath.'/eps/Cumulative_Lack_of_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:] 
set xlabel 'Cycle'
set ylabel 'Cumulative Lack of Transmission'

plot filePath."/Cumulative_Lack_of_Transmission[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Cumulative_Lack_of_Transmission[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Cumulative_Lack_of_Transmission[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Cumulative_Lack_of_Transmission[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\
# "Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output filePath.'/eps/Cumulative_Lack_of_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Requests'

plot filePath."/Average_Requests[Cuckoo_Search].tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output filePath.'/eps/Average_Requests.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Failed Servers'

plot filePath."/Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output filePath.'/eps/Number_of_Failed_Servers.eps'
set terminal postscript eps color
replot



set autoscale x 
# set yrange 
set xlabel 'Hops'
set ylabel 'Number of Failed Requests'

plot filePath."/Failed_Hops_Distribution[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Failed_Hops_Distribution[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Failed_Hops_Distribution[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Failed_Hops_Distribution[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Failed_Request_Hops.eps'
set terminal postscript eps color
replot
