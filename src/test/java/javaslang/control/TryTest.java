/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Serializables;
import javaslang.control.Failure.NonFatal;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TryTest {

    private static final String OK = "ok";
    private static final String FAILURE = "failure";

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfSuccess() {
        assertThat(new Success<>(1).exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfSuccess() {
        assertThat(new Success<>(1).exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfFailure() {
        assertThat(failure().exists(e -> true)).isFalse();
    }

    @Test(expected = Error.class)
    public void shouldNotHoldPropertyExistsWhenPredicateThrows() {
        new Success<>(1).exists(e -> {
            throw new Error("error");
        });
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfSuccess() {
        assertThat(new Success<>(1).forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfSuccess() {
        assertThat(new Success<>(1).forAll(i -> i == 2)).isFalse();
    }

    @Test // a property holds for all elements of no elements
    public void shouldNotHoldPropertyForAllOfFailure() {
        assertThat(failure().forAll(e -> true)).isTrue();
    }

    @Test(expected = Error.class)
    public void shouldNotHoldPropertyForAllWhenPredicateThrows() {
        new Success<>(1).forAll(e -> {
            throw new Error("error");
        });
    }

    // -- iterator

    @Test
    public void shouldReturnIteratorOfSuccess() {
        assertThat((Iterator<Integer>) new Success<>(1).iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfFailure() {
        assertThat((Iterator<Object>) failure().iterator()).isNotNull();
    }

    // -- Try.of

    @Test
    public void shouldCreateSuccessWhenCallingTryOfSupplier() {
        assertThat(Try.of(() -> 1) instanceof Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryOfSupplier() {
        assertThat(Try.of(() -> {
            throw new Error("error");
        }) instanceof Failure).isTrue();
    }

    // -- Try.run

    @Test
    public void shouldCreateSuccessWhenCallingTryRunRunnable() {
        assertThat(Try.run(() -> {
        }) instanceof Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryRunRunnable() {
        assertThat(Try.run(() -> {
            throw new Error("error");
        }) instanceof Failure).isTrue();
    }

    // -- Failure.Cause

    @Test(expected = Failure.Fatal.class)
    public void shouldDetectFatalException() throws Exception {
        NonFatal.of(new OutOfMemoryError());
    }

    @Test
    public void shouldDetectNonFatalException() throws Exception {
        final NonFatal cause = NonFatal.of(new Exception());
        assertThat(cause).isNotNull();
    }

    @Test
    public void shouldSubsequentlyHandOverCause() {
        final Supplier<?> inner = () -> {
            throw new UnknownError("\uD83D\uDCA9");
        };
        final Supplier<?> outer = () -> Try.of(inner::get).get();
        try {
            Try.of(outer::get).get();
            Assertions.fail("Exception expected");
        } catch (Failure.Fatal x) {
            Assertions.assertThat(x.getCause().getMessage()).isEqualTo("\uD83D\uDCA9");
        } catch (Throwable x) {
            Assertions.fail("Unexpected exception type: " + x.getClass().getName());
        }
    }

    // -- Failure.NonFatal

    @Test
    public void shouldReturnAndNotThrowOnNonFatal() {
        final NonFatal cause = NonFatal.of(new Exception());
        assertThat(NonFatal.of(cause) instanceof NonFatal).isTrue();
    }

    @Test
    public void shouldReturnToStringOnNonFatal() {
        final Exception exception = new java.lang.Exception();
        final NonFatal cause = NonFatal.of(exception);
        assertThat(cause.toString()).isEqualTo("NonFatal(" + exception.toString() + ")");
    }

    @Test
    public void shouldReturnHasCodeOnNonFatal() {
        final Exception exception = new java.lang.Exception();
        final NonFatal cause = NonFatal.of(exception);
        assertThat(cause.hashCode()).isEqualTo(Objects.hashCode(exception));
    }

    // -- Failure.Fatal
    @Test
    public void shouldReturnToStringOnFatal() {
        try {
            Try.of(() -> {
                throw new UnknownError();
            });
            fail("Exception Expected");
        } catch (Failure.Fatal x) {
            assertThat(x.toString()).isEqualTo("Fatal(java.lang.UnknownError)");
        }
    }

    @Test
    public void shouldReturnHashCodeOnFatal() {
        UnknownError error = new UnknownError();
        try {
            Try.of(() -> {
                throw error;
            });
            fail("Exception Expected");
        } catch (Failure.Fatal x) {
            assertThat(x.hashCode()).isEqualTo(Objects.hashCode(error));
        }
    }

    @Test
    public void shouldReturnEqualsOnFatal() {
        UnknownError error = new UnknownError();
        try {
            Try.of(() -> {
                throw error;
            });
            fail("Exception Expected");
        } catch (Failure.Fatal x) {
            try {
                Try.of(() -> {
                    throw error;
                });
                fail("Exception Expected");
            } catch (Failure.Fatal fatal) {
                assertThat(x.equals(fatal)).isEqualTo(true);
            }
        }
    }

    // -- Failure

    @Test
    public void shouldDetectFailureOfRunnable() {
        assertThat(Try.of(() -> {
            throw new RuntimeException();
        }).isFailure()).isTrue();
    }

    @Test(expected = Failure.Fatal.class)
    public void shouldPassThroughFatalException() {
        Try.of(() -> {
            throw new UnknownError();
        });
    }

    @Test
    public void shouldDetectFailureOnNonFatalException() {
        assertThat(failure().isFailure()).isTrue();
    }

    @Test
    public void shouldDetectNonSuccessOnFailure() {
        assertThat(failure().isSuccess()).isFalse();
    }

    @Test(expected = NonFatal.class)
    public void shouldThrowWhenGetOnFailure() {
        failure().get();
    }

    @Test
    public void shouldReturnElseWhenOrElseOnFailure() {
        assertThat(failure().orElse(OK)).isEqualTo(OK);
    }

    @Test
    public void shouldReturnElseWhenOrElseGetOnFailure() {
        assertThat(failure().orElseGet(x -> OK)).isEqualTo(OK);
    }

    @Test
    public void shouldRunElseWhenOrElseRunOnFailure() {
        final String[] result = new String[1];
        failure().orElseRun(x -> result[0] = OK);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowOtherWhenOrElseThrowOnFailure() {
        failure().orElseThrow(x -> new IllegalStateException(OK));
    }

    @Test
    public void shouldRecoverOnFailure() {
        assertThat(failure().recover(x -> OK).get()).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverWithOnFailure() {
        assertThat(TryTest.<String> failure().recoverWith(x -> success()).get()).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverWithThrowingOnFailure() {
        final RuntimeException error = error();
        assertThat(failure().recoverWith(x -> {
            throw error;
        })).isEqualTo(new Failure<>(error));
    }

    @Test
    public void shouldConsumeThrowableWhenCallingOnFailureGivenFailure() {
        final String[] result = new String[] { FAILURE };
        failure().onFailure(x -> result[0] = OK);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test
    public void shouldReturnNewFailureWhenCallingOnFailureAndThrowingGivenFailure() {
        final Try<Throwable> actual = failure().onFailure(x -> {
            throw new Error(OK);
        }).failed();
        assertThat(actual.get().getMessage()).isEqualTo(OK);
    }

    @Test
    public void shouldConvertFailureToOption() {
        assertThat(failure().toOption().isDefined()).isFalse();
    }

    @Test
    public void shouldConvertFailureToEither() {
        assertThat(failure().toEither().isLeft()).isTrue();
    }

    @Test
    public void shouldConvertFailureToJavaOptional() {
        assertThat(failure().toJavaOptional().isPresent()).isFalse();
    }

    @Test
    public void shouldFilterMatchingPredicateOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.filter(s -> true)).isEqualTo(actual);
    }

    @Test
    public void shouldFilterNonMatchingPredicateOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.filter(s -> false)).isEqualTo(actual);
    }

    @Test
    public void shouldFilterWithExceptionOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.filter(this::filter)).isEqualTo(actual);
    }

    @Test
    public void shouldReturnIdentityWhenFilterTryOnFailure() {
        final Try<String> identity = failure();
        assertThat(identity.filterTry(s -> true)).isEqualTo(identity);
    }

    @Test
    public void shouldFlatMapOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.flatMap(s -> Try.of(() -> s + "!"))).isEqualTo(actual);
    }

    @Test
    public void shouldFlatMapWithExceptionOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.flatMap(this::flatMap)).isEqualTo(actual);
    }

    @Test
    public void shouldFlatMapTryOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.flatMapTry(s -> Try.of(() -> s + "!"))).isEqualTo(actual);
    }

    @Test
    public void shouldForEachOnFailure() {
        final List<String> actual = new ArrayList<>();
        TryTest.<String> failure().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    public void shouldMapOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.map(s -> s + "!")).isEqualTo(actual);
    }

    @Test
    public void shouldMapWithExceptionOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.map(this::map)).isEqualTo(actual);
    }

    @Test
    public void shouldCreateFailureOnNonFatalException() {
        assertThat(failure().failed().get().getClass().getName()).isEqualTo(RuntimeException.class.getName());
    }

    @Test
    public void shouldComposeFailureWithAndThenWhenFailing() {
        final Try<Void> actual = Try.run(() -> {
            throw new Error("err1");
        }).andThen(() -> {
            throw new Error("err2");
        });
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.Error: err1)");
    }

    @Test
    public void shouldChainSuccessWithMapTry() {
        final Try<Integer> actual = Try.of(() -> 100)
                .mapTry(x -> x + 100)
                .mapTry(x -> x + 50);

        final Try<Integer> expected = new Success<>(250);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldChainFailureWithMapTry() {
        final Try<Integer> actual = Try.of(() -> 100)
                .mapTry(x -> x + 100)
                .mapTry(x -> Integer.parseInt("aaa") + x)   //Throws exception.
                .mapTry(x -> x / 2);
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.NumberFormatException: For input string: \"aaa\")");
    }

    @Test
    public void shouldChainConsumableSuccessWithAndThen() {
        final Try<Integer> actual = Try.of(() -> new ArrayList<Integer>())
                .andThen(arr -> arr.add(10))
                .andThen(arr -> arr.add(30))
                .andThen(arr -> arr.add(20))
                .mapTry(arr -> arr.get(1));

        final Try<Integer> expected = new Success<>(30);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldChainConsumableFailureWithAndThen() {
        final Try<Integer> actual = Try.of(() -> new ArrayList<Integer>())
                .andThen(arr -> arr.add(10))
                .andThen(arr -> arr.add(Integer.parseInt("aaa"))) //Throws exception.
                .andThen(arr -> arr.add(20))
                .mapTry(arr -> arr.get(1));
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.NumberFormatException: For input string: \"aaa\")");
    }

    // peek

    @Test
    public void shouldPeekFailure() {
        final List<Object> list = new ArrayList<>();
        assertThat(failure().peek(list::add)).isEqualTo(failure());
        assertThat(list.isEmpty()).isTrue();
    }

    // equals

    @Test
    public void shouldEqualFailureIfObjectIsSame() {
        final Failure<?> success = new Failure<>(error());
        assertThat(success).isEqualTo(success);
    }

    @Test
    public void shouldNotEqualFailureIfObjectIsNull() {
        assertThat(new Failure<>(error())).isNotNull();
    }

    @Test
    public void shouldNotEqualFailureIfObjectIsOfDifferentType() {
        assertThat(new Failure<>(error()).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualFailure() {
        assertThat(new Failure<>(error())).isEqualTo(new Failure<>(error()));
    }

    // hashCode

    @Test
    public void shouldHashFailure() {
        final Throwable error = error();
        assertThat(new Failure<>(error).hashCode()).isEqualTo(Objects.hashCode(error));
    }

    // toString

    @Test
    public void shouldConvertFailureToString() {
        assertThat(new Failure<>(error()).toString()).isEqualTo("Failure(java.lang.RuntimeException: error)");
    }

    // serialization

    @Test
    public void shouldSerializeDeserializeFailure() {
        final Object actual = Serializables.deserialize(Serializables.serialize(new Failure<>(error())));
        assertThat(actual.toString()).isEqualTo(new Failure<>(error()).toString());
    }

    // -- Success

    @Test
    public void shouldDetectSuccessOfRunnable() {
        assertThat(Try.run(() -> System.out.println("side-effect")).isSuccess()).isTrue();
    }

    @Test
    public void shouldDetectSuccess() {
        assertThat(success().isSuccess()).isTrue();
    }

    @Test
    public void shouldDetectNonFailureOnSuccess() {
        assertThat(success().isFailure()).isFalse();
    }

    @Test
    public void shouldGetOnSuccess() {
        assertThat(success().get()).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseOnSuccess() {
        assertThat(success().orElse(null)).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseGetOnSuccess() {
        assertThat(success().orElseGet(x -> null)).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseRunOnSuccess() {
        final String[] result = new String[] { OK };
        success().orElseRun(x -> result[0] = FAILURE);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseThrowOnSuccess() {
        assertThat(success().orElseThrow(x -> null)).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverOnSuccess() {
        assertThat(success().recover(x -> null).get()).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverWithOnSuccess() {
        assertThat(success().recoverWith(x -> null).get()).isEqualTo(OK);
    }

    @Test
    public void shouldNotConsumeThrowableWhenCallingOnFailureGivenSuccess() {
        final String[] result = new String[] { OK };
        success().onFailure(x -> result[0] = FAILURE);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test
    public void shouldConvertSuccessToOption() {
        assertThat(success().toOption().get()).isEqualTo(OK);
    }

    @Test
    public void shouldConvertSuccessToEither() {
        assertThat(success().toEither().isRight()).isTrue();
    }

    @Test
    public void shouldConvertSuccessToJavaOptional() {
        assertThat(success().toJavaOptional().get()).isEqualTo(OK);
    }

    @Test
    public void shouldFilterTryMatchingPredicateOnSuccess() {
        assertThat(success().filterTry(s -> true).get()).isEqualTo(OK);
    }

    @Test
    public void shouldFilterMatchingPredicateOnSuccess() {
        assertThat(success().filter(s -> true).get()).isEqualTo(OK);
    }

    @Test(expected = NonFatal.class)
    public void shouldFilterNonMatchingPredicateOnSuccess() {
        success().filter(s -> false).get();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFilterWithExceptionOnSuccess() {
        success().filter(s -> {
            throw new RuntimeException("xxx");
        }).get();
    }

    @Test
    public void shouldFlatMapOnSuccess() {
        assertThat(success().flatMap(s -> Try.of(() -> s + "!")).get()).isEqualTo(OK + "!");
    }

    @Test
    public void shouldFlatMapOnIterable() {
        final Iterable<Integer> iterable = Arrays.asList(1, 2, 3);
        assertThat(success().flatMap(s -> iterable).get()).isEqualTo(1);
    }

    @Test(expected = NonFatal.class)
    public void shouldFlatMapOnEmptyIterable() {
        final Iterable<Integer> iterable = Collections.emptyList();
        success().flatMap(s -> iterable).get();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFlatMapWithExceptionOnSuccess() {
        success().flatMap(s -> {
            throw new RuntimeException("xxx");
        }).get();
    }

    @Test
    public void shouldForEachOnSuccess() {
        final List<String> actual = new ArrayList<>();
        success().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(OK));
    }

    @Test
    public void shouldMapOnSuccess() {
        assertThat(success().map(s -> s + "!").get()).isEqualTo(OK + "!");
    }

    @Test(expected = NonFatal.class)
    public void shouldMapWithExceptionOnSuccess() {
        success().map(s -> {
            throw new RuntimeException("xxx");
        }).get();
    }

    @Test(expected = NonFatal.class)
    public void shouldThrowWhenCallingFailedOnSuccess() {
        success().failed().get();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenCallingGetCauseOnSuccess() {
        success().getCause();
    }

    @Test
    public void shouldComposeSuccessWithAndThenWhenFailing() {
        final Try<Void> actual = Try.run(() -> {
        }).andThen(() -> {
            throw new Error("failure");
        });
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.Error: failure)");
    }

    @Test
    public void shouldComposeSuccessWithAndThenWhenSucceeding() {
        final Try<Void> actual = Try.run(() -> {
        }).andThen(() -> {
        });
        final Try<Void> expected = new Success<>(null);
        assertThat(actual).isEqualTo(expected);
    }

    // peek

    @Test
    public void shouldPeekSuccess() {
        final List<Object> list = new ArrayList<>();
        assertThat(success().peek(list::add)).isEqualTo(success());
        assertThat(list.isEmpty()).isFalse();
    }

    @Test(expected = RuntimeException.class)
    public void shouldPeekSuccessAndThrow() {
        success().peek(t -> failure().get());
    }

    // equals

    @Test
    public void shouldEqualSuccessIfObjectIsSame() {
        final Success<?> success = new Success<>(1);
        assertThat(success).isEqualTo(success);
    }

    @Test
    public void shouldNotEqualSuccessIfObjectIsNull() {
        assertThat(new Success<>(1)).isNotNull();
    }

    @Test
    public void shouldNotEqualSuccessIfObjectIsOfDifferentType() {
        assertThat(new Success<>(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualSuccess() {
        assertThat(new Success<>(1)).isEqualTo(new Success<>(1));
    }

    // hashCode

    @Test
    public void shouldHashSuccess() {
        assertThat(new Success<>(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertSuccessToString() {
        assertThat(new Success<>(1).toString()).isEqualTo("Success(1)");
    }

    // serialization

    @Test
    public void shouldSerializeDeserializeSuccess() {
        final Object actual = Serializables.deserialize(Serializables.serialize(new Success<>(1)));
        assertThat(actual).isEqualTo(new Success<>(1));
    }

    // -- Checked Functions

    @Test
    public void shouldCreateIdentityCheckedFunction() {
        assertThat(Function.identity()).isNotNull();
    }

    @Test
    public void shouldEnsureThatIdentityCheckedFunctionReturnsIdentity() throws Throwable {
        assertThat(Function.identity().apply(1)).isEqualTo(1);
    }

    // -- helpers

    private RuntimeException error() {
        return new RuntimeException("error");
    }

    private static <T> Try<T> failure() {
        return Try.of(() -> {
            throw new RuntimeException();
        });
    }

    private <T> boolean filter(T t) {
        throw new RuntimeException("xxx");
    }

    private <T> Try<T> flatMap(T t) {
        throw new RuntimeException("xxx");
    }

    private <T> T map(T t) {
        throw new RuntimeException("xxx");
    }

    private Try<String> success() {
        return Try.of(() -> "ok");
    }
}