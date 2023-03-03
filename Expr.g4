grammar Expr;

//LC (sintactico)
prog:   (expr NEWLINE)+         #Pr
;
expr:   expr op=('*'|'/') expr  #MulDiv
    |   expr op=('+'|'-') expr  #SumRes
    |   ENT                     #Ent
    |   ID                      #Id
    |   '(' expr ')'            #Paren
    |   ID '=' expr             #Asig
;

//LR (lexico)
Asig: '=';
Mul: '*';
Div: '/';
Sum: '+';
Res: '-';
ID : [a-zA-Z]+[a-zA-Z0-9]*;
NEWLINE : [\n\r]+;
BLANK   : [ \t] -> skip;
ENT     : [0-9]+ ;
