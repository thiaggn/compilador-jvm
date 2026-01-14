javacc  -OUTPUT_DIRECTORY:"./src/parser" "./Parser.jj" &&

javac -d "./out" `
	"./src/parser/*.java" `
	"./src/ast/*.java" `
	"./src/ast/simbolo/*.java" `
	"./src/analisador/*.java" `
	"./src/intermediario/*.java" `
	"./src/*.java" &&

Get-Content Entrada2.txt | java -cp "./out" Main