#!/bin/bash

src_dir="./src"
resources_dir="./resources"
class_dir="./bin"
lib="./lib"
jar_dir="./jar"
temp_jardir="./tempjar"

pushd ${jar_dir}/ 
mkdir ${temp_jardir} 
cp ../${lib}/vorbis/*.jar ${temp_jardir}/ 
pushd ${temp_jardir}/ 
for i in *.jar;do jar xf "$i";done 
rm "./META-INF/MANIFEST.MF"
rm *.jar
popd 
popd
