import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {

    public int lineno;   // line number
    public int column;   // column
    private TransitionDiagram td;
    private int colCounter = 0;
    private Parser yyparser; // parent parser object
    private java.io.Reader reader;   // input stream
    public Lexer(java.io.Reader reader, Parser yyparser) throws Exception {
        this.reader = new BufferedReader(reader, 10); // double buffer
        this.yyparser = yyparser;
        column = 0;
        lineno = 1;
    }

    public char NextChar() throws Exception {
        reader.mark(5);
        int data = reader.read();

        colCounter++;
        return (char) data;
    }

    private void unNextChar() throws Exception {
        reader.reset();
        colCounter--;
        if (colCounter < 0) {
            lineno--;
        }
    }

    public int Fail() {
        return -1;
    }

    // * If yylex reach to the end of file, return  0
    // * If there is an lexical error found, return -1
    // * If a proper lexeme is determined, return token <token-id, token-attribute> as follows:
    //   1. set token-attribute into yyparser.yylval
    //   2. return token-id defined in Parser
    //   token attribute can be lexeme, line number, colume, etc.
    public int yylex() throws Exception {
        int state = 0;
        StringBuilder lexeme = new StringBuilder();

        char c = NextChar();
        while(Character.isWhitespace(c) || c == '\n') {
            if (Character.isWhitespace(c) && c != '\n') {
                c = NextChar();
            }
            if (c == '\n') {
                lineno++;
                colCounter = 0;
                c = NextChar();
            }
        }
        column = colCounter;
        while (true) {

            switch (state) {
                case 0:
                    if (c == ';') {
                        yyparser.yylval = new ParserVal((Object) ";");
                        return Parser.SEMI;
                    }
                else if (c == '+' || c == '*' || c == '/') {
                        yyparser.yylval = new ParserVal((Object) c);
                        return Parser.OP;
                    } else if (c == '-') {
                        if ( NextChar() == '>'){
                            yyparser.yylval = new ParserVal((Object) "->");
                            return Parser.FUNCRET;
                        }
                        else {
                            unNextChar();
                            yyparser.yylval = new ParserVal((Object) "-");
                            return Parser.OP;
                        }
                    }  else if (c == '(') {
                        yyparser.yylval = new ParserVal((Object) "(");
                        return Parser.LPAREN;
                    } else if (c == ')') {
                        yyparser.yylval = new ParserVal((Object) ")");
                        return Parser.RPAREN;
                    } else if (c == '='  ) {
                        yyparser.yylval = new ParserVal((Object) "=");
                        return Parser.RELOP;
                    } else if (c == '{') {
                        yyparser.yylval = new ParserVal((Object) "{");
                        return Parser.BEGIN;
                    } else if (c == '}') {
                        yyparser.yylval = new ParserVal((Object) "}");
                        return Parser.END;
                    }else if (c == ',') {
                        yyparser.yylval = new ParserVal((Object) ",");
                        return Parser.COMMA;
                    }else if (c == '!') {
                        state = 8;
                        break;
                    }else if (c == '>') {
                        state = 9;
                        break;
                    } else if (c == '<') {
                        state = 10;
                        break;
                    } else if (Character.isDigit(c)) {
                        state = 11;
                        break;
                    } else if (Character.isLetter(c)) {
                        state = 12;
                        break;
                    } else if ((Character.getNumericValue(c) ==  -1)) { // EOF
                        return 0;
                    } else {
                        return Fail();
                    }
                case 8:
                    if(NextChar() == '!'){
                        yyparser.yylval = new ParserVal((Object) "!=");
                        return Parser.RELOP;
                    }
                case 9:
                    if(NextChar() == '='){
                        yyparser.yylval = new ParserVal((Object) ">=");
                        return Parser.RELOP;
                    }
                    else{
                       unNextChar();
                        yyparser.yylval = new ParserVal((Object) ">");
                        return Parser.RELOP;
                    }
                case 10:
                    if(NextChar() == '-'){
                        yyparser.yylval = new ParserVal((Object) "<-");
                        return Parser.ASSIGN;
                    }
                    else if(NextChar() == '='){
                        yyparser.yylval = new ParserVal((Object) "<=");
                        return Parser.RELOP;
                    }
                    else{
                         unNextChar();
                        yyparser.yylval = new ParserVal((Object) "<");
                        return Parser.RELOP;
                    }
                case 11:
                    while (Character.isDigit(c) || c == '.') {
                        lexeme.append(c);
                        c = NextChar();
                        if ( lexeme.toString().contains(".."))
                            return Fail();
                    }
                    unNextChar();
                    yyparser.yylval = new ParserVal((Object) lexeme.toString());
                    return Parser.NUM;
                case 12:
                    while (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
                        lexeme.append(c);
                        c = NextChar();
                    }
                   unNextChar();
                    if (lexeme.toString().equalsIgnoreCase("IF" )) {
                        yyparser.yylval = new ParserVal((Object) "IF");
                        return Parser.IF;
                    } else if (lexeme.toString().equalsIgnoreCase("PRINT")) {
                        yyparser.yylval = new ParserVal((Object) "PRINT");
                        return Parser.PRINT;
                    } else if (lexeme.toString().equalsIgnoreCase("VAR")) {
                        yyparser.yylval = new ParserVal((Object) "VAR");
                        return Parser.VAR;
                    } if (lexeme.toString().equalsIgnoreCase("INT")) {
                            yyparser.yylval = new ParserVal((Object) "INT");
                            return Parser.INT;
                    } else if (lexeme.toString().equalsIgnoreCase("ELSE")) {
                        yyparser.yylval = new ParserVal((Object) "ELSE");
                        return Parser.ELSE;
                    } else if (lexeme.toString().equalsIgnoreCase("FUNC")) {
                        yyparser.yylval = new ParserVal((Object) "FUNC");
                        return Parser.FUNC;
                    } else if (lexeme.toString().equalsIgnoreCase("WHILE")) {
                        yyparser.yylval = new ParserVal((Object) "WHILE");
                        return Parser.WHILE;
                    } else if (lexeme.toString().equalsIgnoreCase("VOID")) {
                        yyparser.yylval = new ParserVal((Object) "VOID");
                        return Parser.VOID;
                    }
                    else {
                        yyparser.yylval = new ParserVal((Object) lexeme.toString());
                        return Parser.ID;
                    }
                default:
                    return Fail();
            }
        }
    }


}

