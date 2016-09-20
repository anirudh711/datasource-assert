package net.ttddyy.dsproxy.test.hamcrest;

import net.ttddyy.dsproxy.test.StatementBatchExecution;
import org.junit.Assert;
import org.junit.Test;

import static net.ttddyy.dsproxy.test.hamcrest.QueriesHolderAssertions.queries;
import static net.ttddyy.dsproxy.test.hamcrest.QueriesHolderAssertions.queryTypes;
import static net.ttddyy.dsproxy.test.hamcrest.QueryHolderAssertions.delete;
import static net.ttddyy.dsproxy.test.hamcrest.QueryHolderAssertions.insert;
import static net.ttddyy.dsproxy.test.hamcrest.QueryHolderAssertions.select;
import static net.ttddyy.dsproxy.test.hamcrest.QueryHolderAssertions.update;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.fail;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.4
 */
public class QueriesHolderAssertionsTest {


    @Test
    public void queriesWithIndex() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("foo");
        sbe.getQueries().add("bar");
        sbe.getQueries().add("baz");

        Assert.assertThat(sbe, queries(0, is("foo")));
        Assert.assertThat(sbe, queries(1, is("bar")));
        Assert.assertThat(sbe, queries(2, is("baz")));

        Assert.assertThat(sbe, queries(0, startsWith("f")));
    }

    @Test
    public void queriesWithIndexWhenIndexIsOutOfBound() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("foo");
        sbe.getQueries().add("bar");
        sbe.getQueries().add("baz");

        try {
            Assert.assertThat(sbe, queries(100, is("foo")));
            fail("assertion should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: queries[100] exists\n     but: queries[] size was 3");
        }
    }

    @Test
    public void queriesWithIndexWhenExpectationMismatch() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("foo");

        try {
            Assert.assertThat(sbe, queries(0, is("FOO")));
            fail("assertion should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: queries[0] is \"FOO\"\n     but: queries[0] was \"foo\"");
        }
    }

    @Test
    public void queriesWithCollectionMatcher() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("foo");
        sbe.getQueries().add("bar");
        sbe.getQueries().add("baz");

        Assert.assertThat(sbe, queries(hasItem("foo")));
        Assert.assertThat(sbe, queries(hasItems("foo", "bar")));
        Assert.assertThat(sbe, queries(hasItems(startsWith("f"), is("bar"))));

        Assert.assertThat(sbe, queries(hasSize(3)));
    }

    @Test
    public void queriesWithCollectionWhenExpectationMismatch() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("foo");

        try {
            Assert.assertThat(sbe, queries(hasItem("FOO")));
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: queries[] a collection containing \"FOO\"\n     but: queries[] mismatches were: [was \"foo\"]");
        }
    }


    @Test
    public void queryTypesWithIndex() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("SELECT");
        sbe.getQueries().add("INSERT");
        sbe.getQueries().add("UPDATE");
        sbe.getQueries().add("DELETE");

        Assert.assertThat(sbe, queryTypes(0, select()));
        Assert.assertThat(sbe, queryTypes(1, insert()));
        Assert.assertThat(sbe, queryTypes(2, update()));
        Assert.assertThat(sbe, queryTypes(3, delete()));
    }

    @Test
    public void queryTypesWithIndexWhenExpectationMismatch() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("SELECT");

        try {
            Assert.assertThat(sbe, queryTypes(0, update()));
            fail("assertion should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: queries[0] query type \"UPDATE\"\n     but: queries[0] query type was \"SELECT\" (SELECT)");
        }
    }

    @Test
    public void queryTypesWithIndexWhenOutOfIndex() {
        StatementBatchExecution sbe = new StatementBatchExecution();
        sbe.getQueries().add("SELECT");

        try {
            Assert.assertThat(sbe, queryTypes(100, select()));
            fail("assertion should fail");
        } catch (AssertionError e) {
            assertThat(e).hasMessage("\nExpected: queries[100] exists\n     but: queries[] size was 1");
        }
    }

}
