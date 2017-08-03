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

import me.franzgranlund.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EitherTests {
    @Test
    @DisplayName("Tests the Right side")
    void rightTest() {
        Either<String, Integer> r =  Either.right(1);
        assertEquals(true, r.isRight(), "right(1) should be right");
        assertEquals(false, r.isLeft(), "right(1) should not be left");
        assertEquals(new Integer(1), r.getRight(), "right(1) should hold an integer 1");
        assertThrows(NoSuchElementException.class, r::getLeft);
        assertThrows(NullPointerException.class, () -> Either.right(null));
        assertEquals(new Integer(1), r.getRightOrElseGet(lv -> 2));
        assertEquals("bye", r.getLeftOrElseGet(rv -> "bye"));
    }

    @Test
    @DisplayName("Tests the bimap function")
    void bimapTest() {
        Either<String, Integer> r =  Either.right(1);
        Either<String, Long> n = r.bimap(Function.identity(), rv -> 10L + rv);
        assertEquals(new Long(11), n.getRight());
        assertEquals(new Integer(1), r.getRight(), "Original Either should not change");

        Either<String, Integer> l =  Either.left("hello");
        Either<String, Integer> m = l.bimap(lv -> lv + " world", Function.identity());
        assertEquals("hello world", m.getLeft());
        assertEquals("hello", l.getLeft(), "Original Either should not change");
    }

    @Test
    @DisplayName("Tests the fold function")
    void foldTest() {
        Either<String, Integer> r =  Either.right(1);
        Long rFoldResult = r.fold(lv -> 2L, rv -> 10L + rv);
        assertEquals(new Long(11), rFoldResult);

        Either<String, Integer> l =  Either.left("Hello");
        Long lFoldResult = l.fold(lv -> 2L, rv -> 10L + rv);
        assertEquals(new Long(2), lFoldResult);
    }

    @Test
    @DisplayName("Tests the Left side")
    void leftTest() {
        Either<String, Integer> l =  Either.left("Hello");
        assertEquals(false, l.isRight(), "left(hello) should not be right");
        assertEquals(true, l.isLeft(), "left(hello) should be left");
        assertEquals("Hello", l.getLeft(), "left should hold a string");
        assertThrows(NoSuchElementException.class, l::getRight);
        assertThrows(NullPointerException.class, () -> Either.left(null));
        assertEquals("Hello", l.getLeftOrElseGet(rh -> "bye"));
        assertEquals(new Integer(1), l.getRightOrElseGet(lv -> 1));
    }
}
