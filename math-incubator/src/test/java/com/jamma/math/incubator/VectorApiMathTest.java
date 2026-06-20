package com.jamma.math.incubator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jamma.math.Vector4d;

class VectorApiMathTest {

    @Test
    void add() {
        Vector4d a = new Vector4d(1.0, 2.0, 3.0, 4.0);
        Vector4d b = new Vector4d(5.0, 6.0, 7.0, 8.0);
        assertEquals(new Vector4d(6.0, 8.0, 10.0, 12.0), VectorApiMath.add(a, b));
    }

    @Test
    void sub() {
        Vector4d a = new Vector4d(5.0, 7.0, 9.0, 11.0);
        Vector4d b = new Vector4d(1.0, 2.0, 3.0, 4.0);
        assertEquals(new Vector4d(4.0, 5.0, 6.0, 7.0), VectorApiMath.sub(a, b));
    }

    @Test
    void mul() {
        Vector4d a = new Vector4d(2.0, 3.0, 4.0, 5.0);
        Vector4d b = new Vector4d(5.0, 6.0, 7.0, 8.0);
        assertEquals(new Vector4d(10.0, 18.0, 28.0, 40.0), VectorApiMath.mul(a, b));
    }

    @Test
    void scale() {
        Vector4d v = new Vector4d(2.0, 3.0, 4.0, 5.0);
        assertEquals(new Vector4d(4.0, 6.0, 8.0, 10.0), VectorApiMath.scale(v, 2.0));
    }

    @Test
    void dot() {
        Vector4d a = new Vector4d(1.0, 2.0, 3.0, 4.0);
        Vector4d b = new Vector4d(5.0, 6.0, 7.0, 8.0);
        assertEquals(70.0, VectorApiMath.dot(a, b), 1e-15);
    }

    @Test
    void batchAdd() {
        Vector4d[] a = {new Vector4d(1,0,0,0), new Vector4d(0,1,0,0)};
        Vector4d[] b = {new Vector4d(2,0,0,0), new Vector4d(0,2,0,0)};
        Vector4d[] r = new Vector4d[2];
        VectorApiMath.batchAdd(r, a, b);
        assertEquals(new Vector4d(3,0,0,0), r[0]);
        assertEquals(new Vector4d(0,3,0,0), r[1]);
    }
}
