elements=3
directoryName="Contents"
array paths[elements]=["Contents100", "Contents500", "Contents1000"]
array colors[6]=["red", "blue", "forest-green", "magenta", "dark-orange", "black"]



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Storage Usage'

plot for [i = 1:elements] paths[i]."/Cost_of_Storage[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cost_of_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Processing Usage'

plot for [i = 1:elements] paths[i]."/Cost_of_Processing[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cost_of_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Transmission Usage'

plot for [i = 1:elements] paths[i]."/Cost_of_Transmission[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Cost_of_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Hops'

plot for [i = 1:elements] paths[i]."/Number_of_Hops[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Number_of_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Fails'

plot for [i = 1:elements] paths[i]."/Number_of_Fails[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Number_of_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Hops'

plot for [i = 1:elements] paths[i]."/Average_Hops[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Total Cost'

plot for [i = 1:elements] paths[i]."/Cost_Total[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Total_Cost.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Storage Full Nodes'

plot for [i = 1:elements] paths[i]."/Average_Full_Storage[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Full_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Processing Full Nodes'

plot for [i = 1:elements] paths[i]."/Average_Full_Processing[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Full_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Transmission Full Nodes'

plot for [i = 1:elements] paths[i]."/Average_Full_Transmission[Cuckoo_Search].tsv" with lines title paths[i] lw 1 lc rgb colors[i],\

unset table
set output 'proposed/'.directoryName.'/Average_Full_Transmission.eps'
set terminal postscript eps color
replot
