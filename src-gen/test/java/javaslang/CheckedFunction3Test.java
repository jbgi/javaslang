/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class CheckedFunction3Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction3.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(CheckedFunction3.lift((o1, o2, o3) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() throws Throwable {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.apply(null)).isNotNull();
        assertThat(f.apply(null, null)).isNotNull();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNonNull() {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> null;
        assertThat(f.isApplicableTo(1, 2, 3)).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNull2() {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.isApplicableTo(new Object(), null, new Object())).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNull3() {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.isApplicableTo(new Object(), new Object(), null)).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityToTypes() {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, Integer.class, Integer.class)).isTrue();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType1() {
        final CheckedFunction3<Number, Number, Number, Number> f = (i1, i2, i3) -> null;
        assertThat(f.isApplicableToTypes(String.class, Integer.class, Integer.class)).isFalse();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType2() {
        final CheckedFunction3<Number, Number, Number, Number> f = (i1, i2, i3) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, String.class, Integer.class)).isFalse();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType3() {
        final CheckedFunction3<Number, Number, Number, Number> f = (i1, i2, i3) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, Integer.class, String.class)).isFalse();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.arity()).isEqualTo(3);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        final CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, Object>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        final CheckedFunction1<Tuple3<Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> i1 + i2 + i3 + integer.getAndIncrement();
        final CheckedFunction3<Integer, Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2, 3);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2, 3)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 , 4 )).isEqualTo(2  + 3  + 4  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 , 4 )).isEqualTo(2  + 3  + 4  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> null;
        final CheckedFunction3<Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> null;
        final CheckedFunction3<Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> null;
        final CheckedFunction3<Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    private static final CheckedFunction3<Integer, Integer, Integer, Integer> recurrent1 = (i1, i2, i3) -> i1 <= 0 ? i1 : CheckedFunction3Test.recurrent2.apply(i1 - 1, i2, i3) + 1;
    private static final CheckedFunction3<Integer, Integer, Integer, Integer> recurrent2 = CheckedFunction3Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11, 11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction3<Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldGetType() {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> null;
        final CheckedFunction3.Type<Integer, Integer, Integer, Integer> type = f.getType();
        assertThat(type.parameterType1()).isEqualTo(Integer.class);
        assertThat(type.parameterType2()).isEqualTo(Integer.class);
        assertThat(type.parameterType3()).isEqualTo(Integer.class);
        assertThat(type.toString()).isEqualTo("(java.lang.Integer, java.lang.Integer, java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldGetReturnType() {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f = (i1, i2, i3) -> null;
        assertThat(f.getType().returnType()).isEqualTo(Integer.class);
    }

    @Test
    public void testTypesEquals() {
        final CheckedFunction3<Integer, Integer, Integer, Integer> f1 = (i1, i2, i3) -> null;
        final CheckedFunction3<Integer, Integer, Integer, Integer> f2 = (i1, i2, i3) -> null;
        final CheckedFunction3<Integer, Integer, Integer, String> f3 = (i1, i2, i3) -> null;
        final CheckedFunction3<String, Integer, Integer, Integer> f4 = (i1, i2, i3) -> null;
        final CheckedFunction3.Type<Integer, Integer, Integer, Integer> t1 = f1.getType();
        assertThat(t1).isEqualTo(t1);
        assertThat(t1).isNotEqualTo(11);
        assertThat(f1.getType()).isEqualTo(f2.getType());
        assertThat(f1.getType()).isNotEqualTo(f3.getType());
        assertThat(f1.getType()).isNotEqualTo(f4.getType());
    }
}