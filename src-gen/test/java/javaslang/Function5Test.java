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

public class Function5Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4, Object o5) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(Function5.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(Function5.lift((o1, o2, o3, o4, o5) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.apply(null)).isNotNull();
        assertThat(f.apply(null, null)).isNotNull();
        assertThat(f.apply(null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null)).isNotNull();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNonNull() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.isApplicableTo(1, 2, 3, 4, 5)).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNull2() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.isApplicableTo(new Object(), null, new Object(), new Object(), new Object())).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNull3() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.isApplicableTo(new Object(), new Object(), null, new Object(), new Object())).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNull4() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.isApplicableTo(new Object(), new Object(), new Object(), null, new Object())).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityOfNull5() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.isApplicableTo(new Object(), new Object(), new Object(), new Object(), null)).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityToTypes() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, Integer.class, Integer.class, Integer.class, Integer.class)).isTrue();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType1() {
        final Function5<Number, Number, Number, Number, Number, Number> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.isApplicableToTypes(String.class, Integer.class, Integer.class, Integer.class, Integer.class)).isFalse();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType2() {
        final Function5<Number, Number, Number, Number, Number, Number> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, String.class, Integer.class, Integer.class, Integer.class)).isFalse();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType3() {
        final Function5<Number, Number, Number, Number, Number, Number> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, Integer.class, String.class, Integer.class, Integer.class)).isFalse();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType4() {
        final Function5<Number, Number, Number, Number, Number, Number> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, Integer.class, Integer.class, String.class, Integer.class)).isFalse();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType5() {
        final Function5<Number, Number, Number, Number, Number, Number> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.isApplicableToTypes(Integer.class, Integer.class, Integer.class, Integer.class, String.class)).isFalse();
    }

    @Test
    public void shouldGetArity() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.arity()).isEqualTo(5);
    }

    @Test
    public void shouldCurry() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        final Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Object>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        final Function1<Tuple5<Object, Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() {
        final AtomicInteger integer = new AtomicInteger();
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5 + integer.getAndIncrement();
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2, 3, 4, 5);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2, 3, 4, 5)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null, null, null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    private static final Function5<Integer, Integer, Integer, Integer, Integer, Integer> recurrent1 = (i1, i2, i3, i4, i5) -> i1 <= 0 ? i1 : Function5Test.recurrent2.apply(i1 - 1, i2, i3, i4, i5) + 1;
    private static final Function5<Integer, Integer, Integer, Integer, Integer, Integer> recurrent2 = Function5Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() {
        assertThat(recurrent1.apply(11, 11, 11, 11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22, 22, 22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        final Function1<Object, Object> after = o -> null;
        final Function5<Object, Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldGetType() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        final Function5.Type<Integer, Integer, Integer, Integer, Integer, Integer> type = f.getType();
        assertThat(type.parameterType1()).isEqualTo(Integer.class);
        assertThat(type.parameterType2()).isEqualTo(Integer.class);
        assertThat(type.parameterType3()).isEqualTo(Integer.class);
        assertThat(type.parameterType4()).isEqualTo(Integer.class);
        assertThat(type.parameterType5()).isEqualTo(Integer.class);
        assertThat(type.toString()).isEqualTo("(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldGetReturnType() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        assertThat(f.getType().returnType()).isEqualTo(Integer.class);
    }

    @Test
    public void testTypesEquals() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f1 = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f2 = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, String> f3 = (i1, i2, i3, i4, i5) -> null;
        final Function5<String, Integer, Integer, Integer, Integer, Integer> f4 = (i1, i2, i3, i4, i5) -> null;
        final Function5.Type<Integer, Integer, Integer, Integer, Integer, Integer> t1 = f1.getType();
        assertThat(t1).isEqualTo(t1);
        assertThat(t1).isNotEqualTo(11);
        assertThat(f1.getType()).isEqualTo(f2.getType());
        assertThat(f1.getType()).isNotEqualTo(f3.getType());
        assertThat(f1.getType()).isNotEqualTo(f4.getType());
    }
}