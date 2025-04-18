package jpabook.jpashop.config.logging;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightUtil {

    // ANSI 컬러 코드
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String GRAY = "\u001B[90m";

    private static final Set<String> SQL_KEYWORDS = Set.of(
            // DDL 관련 키워드
            "create", "drop", "alter", "table", "view", "index", "procedure",
            "sequence", "function", "trigger", "database", "schema", "grant", "revoke",

            // DML 관련 키워드
            "select", "from", "where", "insert", "into", "values", "update",
            "set", "delete", "join", "inner", "left", "right", "on", "as",

            // 기타 SQL 키워드
            "group", "by", "order", "having", "limit", "offset", "distinct",
            "and", "or", "not", "null", "is", "in", "like", "between", "exists",
            "union", "all", "case", "when", "then", "else", "end", "cascade"
    );

    private static final Pattern SQL_KEYWORDS_PATTERN;

    static {
        // 정규식 패턴 컴파일 (단어 경계 \b 사용, 대소문자 무시)
        String patternString = "\\b(" + String.join("|", SQL_KEYWORDS) + ")\\b";
        SQL_KEYWORDS_PATTERN = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    /**
     * SQL 예약어에 ANSI 컬러 적용
     */
    public static String highlightSqlKeywords(String sql) {
        Matcher matcher = SQL_KEYWORDS_PATTERN.matcher(sql);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String keyword = matcher.group();
            matcher.appendReplacement(sb, PURPLE + keyword + RESET);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 일반 텍스트에 색상 입히기
     */
    public static String color(String text, String ansiColor) {
        return ansiColor + text + RESET;
    }
}
