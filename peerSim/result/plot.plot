filePath="./FailureMagnification1.0"

set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Storage Usage'

plot filePath."/Cost_of_Storage[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Cost_of_Storage[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Cost_of_Storage[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Cost_of_Storage[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Processing Usage'

plot filePath."/Cost_of_Processing[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Cost_of_Processing[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Cost_of_Processing[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Cost_of_Processing[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Transmission Usage'

plot filePath."/Cost_of_Transmission[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Cost_of_Transmission[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Cost_of_Transmission[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Cost_of_Transmission[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Total Usage'

plot filePath."/Cost_Total[Original_Only].tsv"  with lines title "Original Only"  lw 1 lc rgb "magenta",\
filePath."/Cost_Total[Random].tsv" with lines title "Random" lw 1 lc rgb "forest-green",\
filePath."/Cost_Total[Greedy].tsv" with lines title "Greedy" lw 1 lc rgb "blue",\
filePath."/Cost_Total[Cuckoo_search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Request Hops'

plot filePath."/Number_of_Hops[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Number_of_Hops[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Number_of_Hops[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Number_of_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Failed Requests'

plot filePath."/Number_of_Fails[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Number_of_Fails[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Number_of_Fails[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Number_of_Fails[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\

unset table
set output filePath.'/eps/Average_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Requests'

plot filePath."/Number_of_Requests[Cuckoo_Search].tsv" with lines notitle lw 1 lc rgb "magenta"

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



set xrange [0:500]
set yrange [0:] 
set xlabel 'Cycle'
set ylabel 'Average Storage Full Nodes'

plot filePath."/Average_Full_Storage[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Average_Full_Storage[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Average_Full_Storage[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Average_Full_Storage[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\
# "Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output filePath.'/eps/Average_Full_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:] 
set xlabel 'Cycle'
set ylabel 'Average Processing Full Nodes'

plot filePath."/Average_Full_Processing[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Average_Full_Processing[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Average_Full_Processing[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Average_Full_Processing[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\
# "Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output filePath.'/eps/Average_Full_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:] 
set xlabel 'Cycle'
set ylabel 'Average Transmission Full Links'

plot filePath."/Average_Full_Transmission[Original_Only].tsv" with lines title "Original Only" lw 1 lc rgb "magenta",\
filePath."/Average_Full_Transmission[Random].tsv"  with lines title "Random"  lw 1 lc rgb "forest-green",\
filePath."/Average_Full_Transmission[Greedy].tsv"  with lines title "Greedy"  lw 1 lc rgb "blue",\
filePath."/Average_Full_Transmission[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\
# "Failed_Servers.tsv" with lines notitle lw 1 lc rgb "magenta"

unset table
set output filePath.'/eps/Average_Full_Transmission.eps'
set terminal postscript eps color
replot
