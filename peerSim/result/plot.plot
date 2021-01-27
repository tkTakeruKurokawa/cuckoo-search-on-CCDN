filePath="./Nodes1000/9"


####################################################################################################
set autoscale x
# set yrange
set xlabel 'Number of Replicas'
set ylabel 'Average Shortest Path Availability'
set key left top

plot filePath."/Transition_Replica_Availability.tsv" with lines title "Average Replica" lw 3 lc rgb "red",\
filePath."/Transition_Whole_Availability.tsv"  with lines title "Average Whole"  lw 3 lc rgb "blue",\

unset table
set output filePath.'/eps/Average_Shortest_Path_Availability.eps'
set terminal postscript eps color
replot



set autoscale x
# set yrange
set xlabel 'Number of Replicas'
set ylabel 'Operational Cost'

plot filePath."/Transition_Cost.tsv" with lines notitle lw 3 lc rgb "magenta"

unset table
set output filePath.'/eps/Operational_Cost.eps'
set terminal postscript eps color
replot



set autoscale x
# set yrange
set xlabel 'Number of Replicas'
set ylabel 'Value Of Objective Function'
set key right top

plot filePath."/Transition_Replica_Evaluation.tsv" with lines title "Average Replica" lw 3 lc rgb "red",\
filePath."/Transition_Whole_Evaluation.tsv"  with lines title "Average Whole"  lw 3 lc rgb "blue",\

unset table
set output filePath.'/eps/Value_of_Objective_Function.eps'
set terminal postscript eps color
replot



####################################################################################################



# set xrange [0:500]
# # set yrange
# set xlabel 'Number of Cycles'
# set ylabel 'Average Storage Usage'
# set key left top

# plot filePath."/Average_Storage[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
# filePath."/Average_Storage[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
# filePath."/Average_Storage[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
# filePath."/Average_Storage[Cuckoo_Search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

# unset table
# set output filePath.'/eps/Average_Storage.eps'
# set terminal postscript eps color
# replot



# set xrange [0:500]
# # set yrange
# set xlabel 'Number of Cycles'
# set ylabel 'Average Processing Usage'
# set key left top

# plot filePath."/Average_Processing[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
# filePath."/Average_Processing[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
# filePath."/Average_Processing[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
# filePath."/Average_Processing[Cuckoo_Search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

# unset table
# set output filePath.'/eps/Average_Processing.eps'
# set terminal postscript eps color
# replot



# set xrange [0:500]
# # set yrange
# set xlabel 'Number of Cycles'
# set ylabel 'Average Transmission Usage'
# set key left top

# plot filePath."/Average_Transmission[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
# filePath."/Average_Transmission[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
# filePath."/Average_Transmission[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
# filePath."/Average_Transmission[Cuckoo_Search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

# unset table
# set output filePath.'/eps/Average_Transmission.eps'
# set terminal postscript eps color
# replot



# set xrange [0:500]
# # set yrange
# set xlabel 'Number of Cycles'
# set ylabel 'Average Total Usage'
# set key left top

# plot filePath."/Average_Total[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
# filePath."/Average_Total[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
# filePath."/Average_Total[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
# filePath."/Average_Total[Cuckoo_search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

# unset table
# set output filePath.'/eps/Average_Total.eps'
# set terminal postscript eps color
# replot



# set xrange [0:500]
# # set yrange
# set xlabel 'Number of Cycles'
# set ylabel 'Average Request Hops'
# set key right top

# plot filePath."/Average_Hops[Original_Only].tsv" with lines title "Original Only" lw 3 lc rgb "magenta",\
# filePath."/Average_Hops[Random].tsv"  with lines title "Random"  lw 3 lc rgb "forest-green",\
# filePath."/Average_Hops[Greedy].tsv"  with lines title "Greedy"  lw 3 lc rgb "blue",\
# filePath."/Average_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 3 lc rgb "red",\

# unset table
# set output filePath.'/eps/Average_Hops.eps'
# set terminal postscript eps color
# replot



# set xrange [0:500]
# # set yrange
# set xlabel 'Number of Cycles'
# set ylabel 'Average Failed Requests'
# set key left top

# plot filePath."/Average_Fails[Original_Only].tsv" with lines title "Original Only" lw 3 lc rgb "magenta",\
# filePath."/Average_Fails[Random].tsv"  with lines title "Random"  lw 3 lc rgb "forest-green",\
# filePath."/Average_Fails[Greedy].tsv"  with lines title "Greedy"  lw 3 lc rgb "blue",\
# filePath."/Average_Fails[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 3 lc rgb "red",\

# unset table
# set output filePath.'/eps/Average_Fails.eps'
# set terminal postscript eps color
# replot



####################################################################################################



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

plot filePath."/Cumulative_Total[Original_Only].tsv"  with lines title "Original Only"  lw 3 lc rgb "magenta",\
filePath."/Cumulative_Total[Random].tsv" with lines title "Random" lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Total[Greedy].tsv" with lines title "Greedy" lw 3 lc rgb "blue",\
filePath."/Cumulative_Total[Cuckoo_search].tsv" with lines title "Proposed" lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Request Hops'
set key left top

plot filePath."/Cumulative_Hops[Original_Only].tsv" with lines title "Original Only" lw 3 lc rgb "magenta",\
filePath."/Cumulative_Hops[Random].tsv"  with lines title "Random"  lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Hops[Greedy].tsv"  with lines title "Greedy"  lw 3 lc rgb "blue",\
filePath."/Cumulative_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Failed Requests'
set key left top

plot filePath."/Cumulative_Fails[Original_Only].tsv" with lines title "Original Only" lw 3 lc rgb "magenta",\
filePath."/Cumulative_Fails[Random].tsv"  with lines title "Random"  lw 3 lc rgb "forest-green",\
filePath."/Cumulative_Fails[Greedy].tsv"  with lines title "Greedy"  lw 3 lc rgb "blue",\
filePath."/Cumulative_Fails[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Cumulative_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Number of Requests'

plot filePath."/Number_of_Requests[Cuckoo_Search].tsv" with lines notitle lw 3 lc rgb "magenta"

unset table
set output filePath.'/eps/Number_of_Requests.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange
set xlabel 'Number of Cycles'
set ylabel 'Number of Failure Servers'

plot filePath."/Number_of_Failure_Servers.tsv" with lines notitle lw 3 lc rgb "magenta"

unset table
set output filePath.'/eps/Number_of_Failure_Servers.eps'
set terminal postscript eps color
replot



set autoscale x
# set yrange
set xlabel 'Place of Failed Hop'
set ylabel 'Number of Failed Requests'
set key right top

plot filePath."/Failed_Hops_Distribution[Original_Only].tsv" with lines title "Original Only" lw 3 lc rgb "magenta",\
filePath."/Failed_Hops_Distribution[Random].tsv"  with lines title "Random"  lw 3 lc rgb "forest-green",\
filePath."/Failed_Hops_Distribution[Greedy].tsv"  with lines title "Greedy"  lw 3 lc rgb "blue",\
filePath."/Failed_Hops_Distribution[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 3 lc rgb "red",\

unset table
set output filePath.'/eps/Failed_Request_Hops.eps'
set terminal postscript eps color
replot



# set xrange [0:500]
# set yrange [0:]
# set xlabel 'Number of Cycles'
# set ylabel 'Cumulative Lack of Processing'
# set key left top

# plot filePath."/Cumulative_Lack_of_Processing[Original_Only].tsv" with lines title "Original Only" lw 3 lc rgb "magenta",\
# filePath."/Cumulative_Lack_of_Processing[Random].tsv"  with lines title "Random"  lw 3 lc rgb "forest-green",\
# filePath."/Cumulative_Lack_of_Processing[Greedy].tsv"  with lines title "Greedy"  lw 3 lc rgb "blue",\
# filePath."/Cumulative_Lack_of_Processing[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 3 lc rgb "red",\
# # "Failed_Servers.tsv" with lines notitle lw 3 lc rgb "magenta"

# unset table
# set output filePath.'/eps/Cumulative_Lack_of_Processing.eps'
# set terminal postscript eps color
# replot



# set xrange [0:500]
# set yrange [0:]
# set xlabel 'Number of Cycles'
# set ylabel 'Cumulative Lack of Transmission'
# set key left top

# plot filePath."/Cumulative_Lack_of_Transmission[Original_Only].tsv" with lines title "Original Only" lw 3 lc rgb "magenta",\
# filePath."/Cumulative_Lack_of_Transmission[Random].tsv"  with lines title "Random"  lw 3 lc rgb "forest-green",\
# filePath."/Cumulative_Lack_of_Transmission[Greedy].tsv"  with lines title "Greedy"  lw 3 lc rgb "blue",\
# filePath."/Cumulative_Lack_of_Transmission[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 3 lc rgb "red",\
# # "Failed_Servers.tsv" with lines notitle lw 3 lc rgb "magenta"

# unset table
# set output filePath.'/eps/Cumulative_Lack_of_Transmission.eps'
# set terminal postscript eps color
# replot
