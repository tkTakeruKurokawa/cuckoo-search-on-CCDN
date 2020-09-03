elements=3
directoryName="FailureMagnification"
array paths[elements]=["FailureMagnification0.1", "FailureMagnification0.5", "FailureMagnification1.0"]
array colors[6]=["red", "blue", "forest-green", "magenta", "dark-orange", "black"]



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Storage Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Cost_of_Storage[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Processing Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Cost_of_Processing[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Transmission Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Cost_of_Transmission[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Total Usage of Proposed'

plot for [i = 1:elements] paths[i]."/Cost_Total[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Request Hops of Proposed'

plot for [i = 1:elements] paths[i]."/Number_of_Hops[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Failed Requests of Proposed'

plot for [i = 1:elements] paths[i]."/Number_of_Fails[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Fails.eps'
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
