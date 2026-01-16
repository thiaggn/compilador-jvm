javacc  -OUTPUT_DIRECTORY:"./src/parser" "./Parser.jj" &&

javac -d "./out" `
	"./src/parser/*.java" `
	"./src/ast/*.java" `
	"./src/analisador/*.java" `
	"./src/intermediario/*.java" `
	"./src/*.java" &&

java -cp "./out" Main @args