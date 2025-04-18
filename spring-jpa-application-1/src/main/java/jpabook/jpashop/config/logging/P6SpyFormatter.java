package jpabook.jpashop.config.logging;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.util.Locale;

import static jpabook.jpashop.config.logging.HighlightUtil.CYAN;
import static jpabook.jpashop.config.logging.HighlightUtil.GRAY;
import static jpabook.jpashop.config.logging.HighlightUtil.GREEN;
import static jpabook.jpashop.config.logging.HighlightUtil.YELLOW;
import static jpabook.jpashop.config.logging.HighlightUtil.color;
import static jpabook.jpashop.config.logging.HighlightUtil.highlightSqlKeywords;

public class P6SpyFormatter implements MessageFormattingStrategy {

    private static final String NEW_LINE = "\n";
    private static final String TAB = "\t";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (sql.trim().isEmpty()) {
            return formatByCommand(category);
        }
        return formatBySql(sql, category) + getAdditionalMessages(elapsed);
    }

    private String formatByCommand(String category) {
        return NEW_LINE
                + color("Execute Command :", GRAY)
                + NEW_LINE
                + TAB + category
                + NEW_LINE
                + color("----------------------------------------------------------------------------------------------------", GRAY);
    }

    private String formatBySql(String sql, String category) {
        String header = isStatementDDL(sql, category) ?
                color("Execute DDL :", YELLOW)
                : color("Execute DML :", GREEN);

        return NEW_LINE
                + header
                + normalizeSql(highlightSqlKeywords(FormatStyle.BASIC.getFormatter().format(sql)));
    }

    private String getAdditionalMessages(long elapsed) {
        return NEW_LINE
                + color(String.format("Execution Time: %s ms", elapsed), CYAN)
                + color("----------------------------------------------------------------------------------------------------", GRAY);
    }

    private boolean isStatementDDL(String sql, String category) {
        return Category.STATEMENT.getName().equals(category) &&
                isDDL(sql.trim().toLowerCase(Locale.ROOT));
    }

    private boolean isDDL(String lowerSql) {
        return lowerSql.startsWith("create")
                || lowerSql.startsWith("alter")
                || lowerSql.startsWith("drop")
                || lowerSql.startsWith("comment");
    }

    // 주석과 SQL이 붙어있으면 줄바꿈 추가
    private String normalizeSql(String sql) {
        return sql.replaceAll("(\\*/)(\\s*)(\\S)", "$1\n$3");
    }
}
