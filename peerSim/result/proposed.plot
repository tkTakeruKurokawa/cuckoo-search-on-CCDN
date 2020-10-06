elements=4
directoryName="Nodes"
array paths[elements]=["Nodes50", "Nodes100", "Nodes500", "Nodes1000"]
array colors[6]=["red", "blue", "forest-green", "magenta", "dark-orange", "black"]



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Average Storage Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Storage[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Average Processing Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Processing[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Average Transmission Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Transmission[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Average Total Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Total[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Total Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Total[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Average Request Hops of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Hops[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Request Hops of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Hops[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Average Failed Requests of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Fails[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Failed Requests of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Fails[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:]
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Lack of Processing of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Lack_of_Processing[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Lack_of_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:]
set xlabel 'Number of Cycles'
set ylabel 'Cumulative Lack of Transmission of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Lack_of_Transmission[Cuckoo_Search].tsv" with lines title paths[i] lw 3 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Lack_of_Transmission.eps'
set terminal postscript eps color
replot
