/*
 * MIT License
 *
 * Copyright (c) 2017 Franz Granlund
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.franzgranlund;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Either<L, R> extends Serializable {
    long serialVersionUID = -42L;

    static <L, R> Either<L, R> right(final R right) {
        Objects.requireNonNull(right);
        return new Right<>(right);
    }

    static <L, R> Either<L, R> left(final L left) {
        Objects.requireNonNull(left);
        return new Left<>(left);
    }

    boolean isRight();

    boolean isLeft();

    R getRight();

    R getRightOrElseGet(Function<? super L, ? extends R> other);

    L getLeft();

    L getLeftOrElseGet(Function<? super R, ? extends L> other);

    /**
     * Folds either the left or the right side of this Either
     *
     * @param leftMapper  maps the left value if this is a Left
     * @param rightMapper maps the right value if this is a Right
     * @param <U>         type of the folded value
     * @return A value of type U that is the result of the mapper, be it leftMapper or rightMapper
     */
    default <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
        if (isRight()) {
            return rightMapper.apply(getRight());
        } else {
            return leftMapper.apply(getLeft());
        }
    }

    /**
     * Consumes either the left or the right side of this Either
     *
     * @param leftMapper  consumes the left value if this is a Left
     * @param rightMapper consumes the right value if this is a Right
     * @return void
     */
    default void consume(Consumer<? super L> leftMapper, Consumer<? super R> rightMapper) {
        if (isRight()) {
            rightMapper.accept(getRight());
        } else {
            leftMapper.accept(getLeft());
        }
    }

    /**
     * Creates an new Either from the mapping of the left or the right side of this Either.
     *
     * @param leftMapper  maps the left value if this is a Left
     * @param rightMapper maps the right value if this is a Right
     * @param <LL>        the new left type of the resulting Either
     * @param <RR>        the new right type of the resulting Either
     * @return a new Either instance
     */
    default <LL, RR> Either<LL, RR> bimap(Function<? super L, ? extends LL> leftMapper, Function<? super R, ? extends RR> rightMapper) {
        if (isRight()) {
            return new Right<>(rightMapper.apply(getRight()));
        } else {
            return new Left<>(leftMapper.apply(getLeft()));
        }
    }

    /**
     * Creates an Either based on a boolean expression. A Right will be returned if predicate is true.
     * A Left will be returned if predicate is false.
     *
     * @param predicate the predicate
     * @param right     the right value
     * @param left      the left value
     * @param <L>       the type of the left value
     * @param <R>       the type of the right value
     * @return a new Either instance depending on the boolean expression provided by predicate
     */
    static <L, R> Either<L, R> cond(boolean predicate, R right, L left) {
        return predicate ? right(right) : left(left);
    }

    final class Right<L, R> implements Either<L, R> {
        private static final long serialVersionUID = 44L;

        private final R value;

        private Right(R value) {
            this.value = value;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public R getRight() {
            return value;
        }

        @Override
        public R getRightOrElseGet(Function<? super L, ? extends R> other) {
            return value;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("getLeft() on Right");
        }

        @Override
        public L getLeftOrElseGet(Function<? super R, ? extends L> other) {
            return other.apply(value);
        }
    }

    final class Left<L, R> implements Either<L, R> {
        private static final long serialVersionUID = 46L;

        private final L value;

        private Left(L value) {
            this.value = value;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("getRight() on Left");
        }

        @Override
        public R getRightOrElseGet(Function<? super L, ? extends R> other) {
            return other.apply(value);
        }

        @Override
        public L getLeft() {
            return value;
        }

        @Override
        public L getLeftOrElseGet(Function<? super R, ? extends L> other) {
            return value;
        }
    }
}
