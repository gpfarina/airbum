##Makefile per compilare il progetto e creare un file jar. Anche utile per 
## dumpare la cartella
##Notare che viene usato -source 5 in quanto -source 6 non e' valido dato che
## non sono state aggiunte modifiche di linguaggio nella versione 6 (vedere man javac)

src_dir=src
resources_dir=resources
class_dir=bin
lib=lib
jar_dir=jar
jar_name=airbum.jar
temp_jardir=tempjar

all:
	@echo "-Compilando i sorgenti:..."
	javac -sourcepath ${src_dir} ${src_dir}/Main.java  -d ${class_dir} -source 5 -encoding UTF-8 
	@echo "..Lancia il gioco con ./airbum.sh" 
jarfile:
	make all	
	@echo "Creando il file jar:..."
	/bin/bash createjar.sh
	jar mcf ${jar_dir}/MANIFEST.MF ${jar_dir}/${jar_name} -C ${class_dir}/ . -C ${resources_dir}/ . -C ${jar_dir}/tempjar/ .
	rm -rf ${jar_dir}/${temp_jardir}
	@echo "..Lancia il gioco con java -jar jar/airbum.jar"
clean:
	rm ${class_dir}/* ${jar_dir}/${jar_name} -rf
	
