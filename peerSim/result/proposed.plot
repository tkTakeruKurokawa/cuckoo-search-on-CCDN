elements=1
directoryName="Contents"
array paths[elements]=["Contents500"]
array colors[6]=["red", "blue", "forest-green", "magenta", "dark-orange", "black"]



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Storage Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Storage[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Processing Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Processing[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Transmission Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Transmission[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Total Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Total[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Cumulative Total Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Total[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Request Hops of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Hops[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Cumulative Request Hops of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Hops[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Failed Requests of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Fails[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Cumulative Failed Requests of Proposed'

plot for [i = 1:elements] paths[i]."/Cumulative_Fails[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cumulative_Fails.eps'
set terminal postscript eps color
replot




set xrange [0:500]
set yrange [0:]
set xlabel 'Cycle'
set ylabel 'Average Storage Full Nodes of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Full_Storage[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Full_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:]
set xlabel 'Cycle'
set ylabel 'Average Processing Full Nodes of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Full_Processing[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Full_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
set yrange [0:]
set xlabel 'Cycle'
set ylabel 'Average Transmission Full Links of Proposed'

plot for [i = 1:elements] paths[i]."/Average_Full_Transmission[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Full_Transmission.eps'
set terminal postscript eps color
replot