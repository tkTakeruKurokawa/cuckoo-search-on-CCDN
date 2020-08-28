#!/bin/bash

rm result/eps/*.eps
rm result/*.tsv
rm result/*.txt
java -cp "src:peersim-1.0.5.jar:jep-2.3.0.jar:djep-1.0.0.jar" peersim.Simulator src/main/config.txt
cd ./result
gnuplot plot.plot
cd ../