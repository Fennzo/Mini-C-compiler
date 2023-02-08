public class Parser
{
    public static final int OP         = 10;    // "+", "-", "*", "/"
    public static final int RELOP      = 11;    // "<", ">", "=", "!=", "<=", ">="
    public static final int ASSIGN     = 13;    // "<-"
    public static final int LPAREN     = 14;    // "("
    public static final int RPAREN     = 15;    // ")"
    public static final int SEMI       = 16;    // ";"
    public static final int COMMA      = 17;    // ","
    public static final int FUNCRET    = 18;    // "->"
    public static final int NUM        = 19;    // number
    public static final int ID         = 20;    // identifier
    public static final int BEGIN      = 21;    // "{"
    public static final int END        = 22;    // "}"
    public static final int INT        = 23;    // "int"
    public static final int PRINT      = 24;    // "print"
    public static final int VAR        = 25;    // "var"
    public static final int FUNC       = 26;    // "func"
    public static final int IF         = 27;    // "if"
    public static final int ELSE       = 29;    // "else"
    public static final int WHILE      = 30;    // "while"
    public static final int VOID       = 31;    // "void"

    Compiler         compiler;
    Lexer            lexer;     // lexer.yylex() returns token-name
    public ParserVal yylval;    // yylval contains token-attribute
    private boolean isSuccess = true;

    public Parser(java.io.Reader r, Compiler compiler) throws Exception
    {
        this.compiler = compiler;
        this.lexer    = new Lexer(r, this);
    }

        public int yyparse() throws Exception
        {
            while ( true )
            {
                int token = lexer.yylex();  // get next token-name
                Object attr = yylval.obj;   // get      token-attribute
                String tokenname = "SEMI";
                int lineno = lexer.lineno;
                int column = lexer.column;

                switch (token)
                {
                    case 0:
                        // EOF is reached
                        if (isSuccess) {
                            System.out.println("Success!");
                            return 0;
                        }
                        break;
                    case -1:
                        // lexical error is found
                        isSuccess = false;
                        lineno = lexer.lineno;
                        column = lexer.column;
                        System.out.println("Error! There is a lexical error at " + lineno + ":" + column + ".");
                        System.exit(0);
                    case OP:
                        tokenname = "OP";
                        break;
                    case RELOP:
                        tokenname = "RELOP";
                        break;
                    case ASSIGN:
                        tokenname = "ASSIGN";
                        break;
                    case LPAREN:
                        tokenname = "LPAREN";
                        break;
                    case RPAREN:
                        tokenname = "RPAREN";
                        break;
                    case SEMI:
                        tokenname = "SEMI";
                        break;
                    case COMMA:
                        tokenname = "COMMA";
                        break;
                    case FUNCRET:
                        tokenname = "FUNCRET";
                        break;
                    case NUM:
                        tokenname = "NUM";
                        break;
                    case ID:
                        tokenname = "ID";
                        break;
                    case BEGIN:
                        tokenname = "BEGIN";
                        break;
                    case END:
                        tokenname = "END";
                        break;
                    case INT:
                        tokenname = "INT";
                        break;
                    case PRINT:
                        tokenname = "PRINT";
                        break;
                    case VAR:
                        tokenname = "VAR";
                        break;
                    case FUNC:
                        tokenname = "FUNC";
                        break;
                    case IF:
                        tokenname = "IF";
                        break;
                    case ELSE:
                        tokenname = "ELSE";
                        break;
                    case WHILE:
                        tokenname = "WHILE";
                        break;
                    case VOID:
                        tokenname = "VOID";
                        break;
                    default:
                        tokenname = "UNKNOWN";
                        break;
                }
                System.out.println("<" + tokenname + ", token-attr: + \"" + attr + "\", "+ lineno + ":" + column + ">");
           }
        }
}
