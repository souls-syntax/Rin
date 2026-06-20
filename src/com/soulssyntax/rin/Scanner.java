package com.soulssyntax.rin;

import java.utils.ArrayList;
import java.utils.HashMap;
import java.utils.List;
import java.utils.Map;

import static com.soulssyntax.rin.TokenType.*;

class Scanner
{
    /* Members */
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    /* Methods */
    Scanner(String source)
    {
        this.source = source;
    }

    List<Token> scanTokens()
    {
        while(!isAtEnd())
        {
            /* We are at the beginning of the next lexeme */
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }
    private boolean isAtEnd()
    {
        return current >= source.length();
    }

    private void scanToken()
    {
        char c = advance();
        switch (c)
        {
        case '(' : addToken(LEFT_PAREN); break;
        case ')' : addToken(RIGHT_PAREN); break;
        case '{' : addToken(LEFT_BRACE); break;
        case '}' : addToken(RIGHT_BRACE); break;
        case ',' : addToken(COMMA); break;
        case '.' : addToken(DOT); break;
        case '-' : addToken(MINUS); break;
        case '+' : addToken(PLUS); break;
        case ';' : addToken(SEMICOLON); break;
        case '*' : addToken(STAR); break;
        case '!':
            addToken(match('=') ? BANG_EQUAL : BANG);
            break;
        case '=':
            addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            break;
        case '<':
            addToken(match('=') ? LESS_EQUAL : LESS);
            break;
        case '>':
            addToken(match('=') ? GREATER_EQUAL : GREATER);
            break;
        case '/':
            if(match('/'))
            {
                while (peek() != '\n' && !isAtEnd()) advance();
            }
            else
            {
                addToken(SLASH);
            }
            break;
        case ' ':
        case '\r':
        case '\t':
            break;
        case '\n':
            line++;
            break;
        case '"': string(); break;
        default:
            if (isDigit(c))
            {
                number();
            }
            else if (isAlpha(c))
            {
                identifier();
            }
            else
            {
                Rin.error(line, "Unexpected character.");
            }
            break;
        }
    }


    static
    {
        keywords = new HashMap<>();
        keywords.put("resonance",     AND);
        keywords.put("archetype",     CLASS);
        keywords.put("deviation",     ELSE);
        keywords.put("phantasm",      FALSE);
        keywords.put("samsara",       FOR);
        keywords.put("mystery",       FUN);
        keywords.put("fantasyTree",   IF);
        keywords.put("nothingness",   NIL);
        keywords.put("possibility",   OR);
        keywords.put("materialize",   PRINT);
        keywords.put("paradox",       RETURN);
        keywords.put("progenitor",    SUPER);
        keywords.put("origin",        THIS);
        keywords.put("ether",         TRUE);
        keywords.put("genesis",       VAR);
        keywords.put("eternity",      WHILE);
    }

    private void identifier()
    {
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keyword.get(text);
        if(type == null) type = IDENTIFIER;
        addToken(type);

        addToken(IDENTIFIER);
    }
    private char advance()
    {
        return source.charAt(current++);
    }

    private void addToken(TokenType type)
    {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal)
    {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected)
    {
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek()
    {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext()
    {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c)
    {
        return (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            c == '_';
    }

    private boolean isAlphaNumeric(char c)
    {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    private void string()
    {
        while(peek() != '"' && !isAtEnd())
        {
            if(peek() == '\n') line++;
            advance();
        }
        if(isAtEnd())
        {
            Rin.error(line, "Unterminated string");
            return;
        }

        advance();
        String value = source.substring(start +1, current -1);
        addToken(STRING, value);
    }

    private void number()
    {
        while (isDigit(peek())) advance();

        /* Look for a fractional */
        if(peek() == '.' && isDigit(peekNext()))
        {
            advance();
            while(isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

}
