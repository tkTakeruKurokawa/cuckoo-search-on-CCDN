#!/bin/bash -u

declare -a names=("Contents" "Users" "Trials" "Storage" "Processing" "Transmission" "FailureMagnification")
declare -a parameters=("totalContents" "users" "totalTrials" "maxStorageCapacity" "maxProcessingCapacity" "maxTransmissionCapacity" "failureMagnification")

echo "Changed Parameter"
echo "   0. Number of Contents"
echo "   1. Number of Users"
echo "   2. Number of Trials"
echo "   3. Storage Capacity"
echo "   4. Processing Capacity"
echo "   5. Transmission Capacity"
echo "   6. Failure Magnification"
echo "Select a Number"
read answer 

parameterName=${parameters[$answer]}
directoryName=${names[$answer]}

echo "Input values"
declare -a array=()
read array

values=""

for value in ${array[@]}; do
  path="${directoryName}${value}"
  rm result/${path}/eps/*.eps
  rm result/${path}/*.tsv
  rm result/${path}/*.txt

  java ChangeParameter ${parameterName} ${value} ${directoryName}
  values="$values$value,"
  java -cp "src:peersim-1.0.5.jar:jep-2.3.0.jar:djep-1.0.0.jar" peersim.Simulator src/main/config.txt
  cd ./result/
  gnuplot plot.plot
  cd ../
done

rm -rf result/proposed/directoryName
java ChangeParameter ${values} ${directoryName}

cd ./result/
gnuplot proposed.plot
cd ../