package com.jamma.math.incubator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jamma.math.Vector4D;

class VectorApiMathTest {

    @Test
    void add() {
        Vector4D a = new Vector4D(1.0, 2.0, 3.0, 4.0);
        Vector4D b = new Vector4D(5.0, 6.0, 7.0, 8.0);
        assertEquals(new Vector4D(6.0, 8.0, 10.0, 12.0), VectorApiMath.add(a, b));
    }

    @Test
    void sub() {
        Vector4D a = new Vector4D(5.0, 7.0, 9.0, 11.0);
        Vector4D b = new Vector4D(1.0, 2.0, 3.0, 4.0);
        assertEquals(new Vector4D(4.0, 5.0, 6.0, 7.0), VectorApiMath.sub(a, b));
    }

    @Test
    void mul() {
        Vector4D a = new Vector4D(2.0, 3.0, 4.0, 5.0);
        Vector4D b = new Vector4D(5.0, 6.0, 7.0, 8.0);
        assertEquals(new Vector4D(10.0, 18.0, 28.0, 40.0), VectorApiMath.mul(a, b));
    }

    @Test
    void scale() {
        Vector4D v = new Vector4D(2.0, 3.0, 4.0, 5.0);
        assertEquals(new Vector4D(4.0, 6.0, 8.0, 10.0), VectorApiMath.scale(v, 2.0));
    }

    @Test
    void dot() {
        Vector4D a = new Vector4D(1.0, 2.0, 3.0, 4.0);
        Vector4D b = new Vector4D(5.0, 6.0, 7.0, 8.0);
        assertEquals(70.0, VectorApiMath.dot(a, b), 1e-15);
    }

    @Test
    void batchAdd() {
        Vector4D[] a = {new Vector4D(1,0,0,0), new Vector4D(0,1,0,0)};
        Vector4D[] b = {new Vector4D(2,0,0,0), new Vector4D(0,2,0,0)};
        Vector4D[] r = new Vector4D[2];
        VectorApiMath.batchAdd(r, a, b);
        assertEquals(new Vector4D(3,0,0,0), r[0]);
        assertEquals(new Vector4D(0,3,0,0), r[1]);
    }
}
