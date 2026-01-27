javacc  -OUTPUT_DIRECTORY:"./src/parser" "./Parser.jj" &&

javac -d "./bin" `
	"./src/parser/*.java" `
	"./src/ast/*.java" `
	"./src/ir/*.java" `
	"./src/*.java" &&

java -cp "./bin" Main @args &&

java -jar jasmin.jar ./saida/Programa.j -d ./saida &&

java -cp "./saida" Programa

