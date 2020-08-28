set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Storage Usage'

plot "Cost_of_Storage[Origin_Only].tsv"  with lines title "Origin Only"  lw 1 lc rgb "blue",\
"Cost_of_Storage[Cuckoo_Search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_of_Storage.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Processing Usage'

plot "Cost_of_Processing[Origin_Only].tsv"  with boxes title "Origin Only"  lw 1 lc rgb "blue",\
"Cost_of_Processing[Cuckoo_Search].tsv" with boxes title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_of_Processing.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Transmission Usage'

plot "Cost_of_Transmission[Origin_Only].tsv"  with boxes title "Origin Only"  lw 1 lc rgb "blue",\
"Cost_of_Transmission[Cuckoo_Search].tsv" with boxes title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_of_Transmission.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Total Cost'

plot "Cost_Total[Origin_Only].tsv"  with lines title "Origin Only"  lw 1 lc rgb "blue",\
"Cost_Total[Cuckoo_search].tsv" with lines title "Proposed" lw 1 lc rgb "red",\

unset table
set output './eps/Cost_Total.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Requests'

# set style fill solid border lc rgb "black"
plot "Number_of_Requests[Cuckoo_Search].tsv" with boxes notitle lw 1 lc rgb "magenta"

unset table
set output './eps/Number_of_Requests.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Hops'

plot "Number_of_Hops[Origin_Only].tsv" with boxes title "Origin Only" lw 1 lc rgb "blue",\
"Number_of_Hops[Cuckoo_Search].tsv"  with boxes title "Proposed"  lw 1 lc rgb "red",\

unset table
set output './eps/Number_of_Hops.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Number of Fails'

plot "Number_of_Fails[Origin_Only].tsv" with boxes title "Origin Only" lw 1 lc rgb "blue",\
"Number_of_Fails[Cuckoo_Search].tsv"  with boxes title "Proposed"  lw 1 lc rgb "red",\

unset table
set output './eps/Number_of_Fails.eps'
set terminal postscript eps color
replot



set xrange [0:500]
# set yrange 
set xlabel 'Cycle'
set ylabel 'Average Hops'

plot "Average_Hops[Origin_Only].tsv" with lines title "Origin Only" lw 1 lc rgb "blue",\
"Average_Hops[Cuckoo_Search].tsv"  with lines title "Proposed"  lw 1 lc rgb "red",\
# "Failed_Servers.tsv" with boxes notitle lw 1 lc rgb "magenta"

unset table
set output './eps/Average_Hops.eps'
set terminal postscript eps color
replot
