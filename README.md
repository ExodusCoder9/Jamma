# Jamma Math Library

> A modern, immutable-first Java math library for graphics, simulation, and game development. Designed as a ground-up replacement for JOML with Java records, Foreign Function & Memory API support, SIMD acceleration, and a vastly broader mathematical scope.

## Requirements

| Requirement        | Minimum                                                             |
|--------------------|---------------------------------------------------------------------|
| **Java**           | 25+ (for `--enable-preview` and `jdk.incubator.vector`)             |
| **Math Core**      | Java 25+                                                            |
| **Math Incubator** | Java 25+ with `--add-modules jdk.incubator.vector --enable-preview` |
| **Build**          | Gradle 9.x+ (included wrapper)                                      |

[![build](https://github.com/ExodusCoder9/Jamma/actions/workflows/build.yml/badge.svg)](https://github.com/ExodusCoder9/Jamma/actions/workflows/build.yml)

---

## Table of Contents

- [Core Design Philosophy](#core-design-philosophy)
- [Modules](#modules)
- [Vector Types](#vector-types)
- [Matrix Types](#matrix-types)
- [Quaternion Types](#quaternion-types)
- [Axis-Angle](#axis-angle)
- [Dual Numbers](#dual-numbers)
- [Scalar Math](#scalar-math)
- [Interpolation / Easing](#interpolation--easing)
- [Geometry Types](#geometry-types)
- [Intersection Tests](#intersection-tests)
- [Frustum Culling](#frustum-culling)
- [Geometry Utilities](#geometry-utilities)
- [Frustum Ray Builder](#frustum-ray-builder)
- [Math Utility Facade](#math-utility-facade)
- [Incubator: SIMD Vector API](#incubator-simd-vector-api)
- [Incubator: Parallel Operations](#incubator-parallel-operations)
- [Incubator: Off-Heap Memory](#incubator-off-heap-memory)
- [Memory & Buffer I/O](#memory--buffer-io)
- [JOML Comparison](#joml-comparison)
- [Why Not JOML?](#why-not-joml)

---

## Core Design Philosophy

### Immutability by Default
Vectors, quaternions, dual numbers, axis-angle, planes, and spheres are **Java records** — fully immutable, with correct `equals`/`hashCode` out of the box. This eliminates entire categories of bugs:

- No defensive copies needed
- Thread-safe without synchronization
- Safe to use as map keys or in sets
- Predictable, side-effect-free chaining: `v1.add(v2).normalize().scale(2.0)`

### Mutability Where It Matters
Matrices (`Matrix4d`, `Matrix4f`, etc.), `AABB`, `Ray`, and `FrustumIntersection` are **mutable classes** with fluent `return this` setters. Matrix operations are the performance-critical path where mutation avoids allocation, and matrices are rarely shared across threads.

### `Math.fma` Everywhere
All dot products, lerps, and matrix multiply accumulations use `Math.fma` (fused multiply-add) for higher precision and (where available) hardware acceleration.

### Modern Java
- `java.lang.foreign.MemorySegment` for off-heap memory I/O
- `java.lang.foreign.ValueLayout` for typed memory access
- `jdk.incubator.vector.DoubleVector` for SIMD
- `StructuredTaskScope` for parallel decomposition
- JPMS `module-info.java` for proper encapsulation
- `Math.clamp` (JDK 25) for all vector and matrix clamping operations

---

## Modules

### `math-core`
The main library. No dependencies beyond the JDK. All production types: vectors, matrices, quaternions, dual numbers, geometry, intersection tests, scalar math, interpolation.

### `math-incubator`
Experimental features requiring `--add-modules jdk.incubator.vector` and `--enable-preview`. SIMD-accelerated vector ops, parallel batch processing, and off-heap memory management.

### `lwjgl-jamma`
LWJGL 3 interop module. Provides zero-allocation `put`/`get` methods that write Jamma types directly into `FloatBuffer`, `DoubleBuffer`, and `MemorySegment` — ready for GL/Vulkan native calls. Integrates with `MemoryStack` for fast stack-allocated uniforms and vertex buffers.

### `joml-adapter` <sub>*new*</sub>
JOML compatibility adapter. Provides mutable, `return this` adapter classes with JOML's API surface backed by Jamma types under the hood. Drop-in replacement for `org.joml.Vector3f`, `Matrix4f`, `Quaternionf`, etc. — just change your imports. Includes `JomlCompat` for direct JOML ↔ Jamma conversion.

---

## Vector Types

### Precision Variants

Each vector type exists in three precision variants:

| 2D         | 3D         | 4D         | Precision |
|------------|------------|------------|-----------|
| `Vector2d` | `Vector3d` | `Vector4d` | `double`  |
| `Vector2f` | `Vector3f` | `Vector4f` | `float`   |
| `Vector2i` | `Vector3i` | `Vector4i` | `int`     |

### Common Operations (all vector types)

**Arithmetic**
- `add(v)`, `add(x, y, ...)`, `sub(v)`, `sub(x, y, ...)`
- `mul(v)`, `mul(s)`, `mul(x, y, ...)`, `div(v)`, `div(s)`, `div(x, y, ...)`
- `scale(s)`, `negate()`, `abs()`, `sign()`

**Geometry**
- `dot(v)`, `cross(v)` (2D cross returns scalar; 3D returns vector; 4D has no cross)
- `length()`, `lengthSquared()`
- `distance(v)`, `distance(x, y, ...)`, `distanceSquared(v)`, `distanceSquared(x, y, ...)`
- `normalize()`, `normalize(length)`, `safeNormalize(fallback)`, `setLength(len)`

**Reflection & Projection** (2D/3D vectors)
- `reflect(normal)`, `refract(normal, eta)`, `project(onto)`, `reject(onto)`

**Rotation** (2D/3D)
- 2D: `rotate(angle)`, `perpendicular()`
- 3D: `rotate(axis, angle)`, `angleSigned(v, normal)` (3D only)

**Interpolation**
- `lerp(other, t)`, `midpoint(other)`

**Component-wise**
- `min(v)`, `max(v)`, `clamp(min, max)`
- `ceil()`, `floor()`, `round()` (float/double)
- `minComponent()`, `maxComponent()`, `minComponentIndex()`, `maxComponentIndex()`
- `fma(a, b)`, `fma(scalar, b)`

**Vector-specific**
- 2D: `angle(v)`, `angleSigned(v)`, `halfway(v)`, `rotate(angle)`, `perpendicular()`, `isPerpendicular(v, eps)`
- 3D: `angle(v)`
- 4D: `fromMemorySegment`, `writeToMemorySegment`, `read`, `write`

**Conversion**
- `toVector2d/3d/4d()`, `toVector2f/3f/4f()`
- `xy()` (4D→2D), `xyz()` (4D→3D)
- `toArray(dest)`, `get(double[])`

**Buffer / Memory I/O**
- `fromBuffer(Buffer)`, `fromBuffer(index, Buffer)`, `writeToBuffer(Buffer)`, `writeToBuffer(index, Buffer)`
- `fromMemorySegment(MemorySegment, offset)`, `writeToMemorySegment(MemorySegment, offset)`
- Aliases: `read(MemorySegment, offset)`, `write(MemorySegment, offset)`

**Predicates**
- `isFinite()`, `isNaN()`, `equals(v, delta)`

---

## Matrix Types

### Variants

| Type         | Size | Precision | Mutable         |
|--------------|------|-----------|-----------------|
| `Matrix2d`   | 2×2  | `double`  | Yes             |
| `Matrix2f`   | 2×2  | `float`   | Yes             |
| `Matrix3d`   | 3×3  | `double`  | Yes             |
| `Matrix3f`   | 3×3  | `float`   | Yes             |
| `Matrix3x2d` | 3×2  | `double`  | Yes (2D affine) |
| `Matrix3x2f` | 3×2  | `float`   | Yes (2D affine) |
| `Matrix4d`   | 4×4  | `double`  | Yes             |
| `Matrix4f`   | 4×4  | `float`   | Yes             |
| `Matrix4x3d` | 4×3  | `double`  | Yes (affine)    |
| `Matrix4x3f` | 4×3  | `float`   | Yes (affine)    |

### Common Operations (all matrix types)

**Constructors**
- Default (identity), copy, from array, from components, from smaller/larger matrix

**Arithmetic**
- `add(m)`, `sub(m)`, `mul(m)`, `mul(s)`, `mulComponentWise(m)`
- `lerp(other, t)`

**Properties**
- `determinant()`, `trace()`
- `isIdentity()`, `isIdentity(epsilon)`, `isFinite()`, `isAffine()` (4×4)

**Transformations**
- `identity()`, `zero()`
- `scale(x, y [,z])`, `scale(v)`, `scale(s)`
- `translate(x, y [,z])`, `translate(v)` (4×4, 4×3, 3×2)

**Rotation** (3×3, 4×4, 4×3, 3×2)
- `rotate(angle, axisX, axisY, axisZ)`, `rotate(q)`
- `rotateX(angle)`, `rotateY(angle)`, `rotateZ(angle)`
- `rotateXYZ(angleX, angleY, angleZ)`, `rotateZYX(angleZ, angleY, angleX)`
- `rotateLocalX/Y/Z(angle)` (4×4 only)
- Static: `rotationX/Y/Z(angle)`, `scaling(x, y, z)`, `translation(x, y, z)`
- 3×2: 2D `rotate(angle)` (Z-axis rotation in XY)

**Inverse / Transpose**
- `invert()`, `invertAffine()` (4×4 only)
- `transpose()`, `transpose3x3()` (4×4, 3×2)
- `adjugate()`, `normal()` (4×4 only)

**Transform Vectors**
- `transform(v)` (full 4-component; 3×2: 2D point)
- `transformPosition(v)` (position, w=1)
- `transformDirection(v)` (direction, w=0; 3×2: no translation)
- `transformProject(v)` (projected position, divide by w)

**Projection / View** (4×4, 4×3)
- `perspective(fovY, aspect, zNear, zFar)`
- `perspectiveVulkan(fovY, aspect, zNear, zFar)`
- `ortho(left, right, bottom, top, zNear, zFar)`
- `orthoVulkan(left, right, bottom, top, zNear, zFar)` (both Matrix4f + Matrix4d)
- `ortho2D(left, right, bottom, top)`
- `frustum(left, right, bottom, top, zNear, zFar)`
- `lookAt(eye, center, up)`, `lookAt(eyeX, ...)`
- `lookAlong(dir, up)`

**Specialized** (4×4)
- `billboard(objPos, target, up)` — billboard matrix
- `shadow(light, nx, ny, nz, d)` — shadow matrix
- `reflection(nx, ny, nz, d)` — reflection matrix
- `reflect(nx, ny, nz)` — reflect (multiply current)
- `billboardCylindrical(objPos, target, up)` — cylindrical billboard
- `billboardSpherical(objPos, target, up)` — spherical billboard
- `arcball(radius, centerX, centerY, centerZ, angleX, angleY)` — arcball rotation

**Component Access**
- `get(col, row)`, `set(col, row, value)`
- `row(index)`, `column(index)`
- `setRow(row, v)`, `setColumn(col, v)`
- `m00()` through `m33()` getters (4×4)
- `m00(v)` through `m33(v)` fluent setters (4×4)
- `getScale()`, `getTranslation()`, `setTranslation()`
- `getEulerAnglesZYX()`, `getEulerAnglesXYZ()`, `getEulerAnglesYXZ()`
- `positiveX/Y/Z()`, `normalizedPositiveX/Y/Z()`
- `getNormalizedRotation()`, `getUnnormalizedRotation()`
- `get(double[], offset)` — bulk copy to array
- `get3x3(double[] dest)` — extract upper-left 3×3

**Buffer / Memory I/O**
- `fromBuffer(Buffer)`, `fromBuffer(index, Buffer)`, `writeToBuffer(Buffer)`, `writeToBuffer(index, Buffer)`
- `get(MemorySegment, offset)` / `set(MemorySegment, offset)` (4×4, 4×3)
- `read(MemorySegment, offset)` / `write(MemorySegment, offset)` (4×3)

### MatrixStack — Scene graph stack

A simple push/pop matrix stack backed by `ArrayDeque<Matrix4f>`:
- `push()` — duplicate top
- `pop()` — restore previous
- `translate/rotate/scale` — modify top
- `clear()` — reset to single identity

---

## Quaternion Types

### `Quaterniond` / `Quaternionf` — Record (immutable)

**Constructors & Factories**
- Default (identity), copy, from array, from axis-angle, from `Matrix3d/f`, from `Matrix4d/f`
- `fromAxisAngle(axis, angle)`, `fromAxisAngleDeg(axis, angleDeg)`
- `fromEulerAnglesXYZ(xAngle, yAngle, zAngle)`
- `fromEulerAnglesZYX(zAngle, yAngle, xAngle)`
- `fromEulerAnglesYXZ(yAngle, xAngle, zAngle)`
- `rotateTo(from, to)` — rotation aligning two vectors
- `lookAt(dir, up)` — quaternion look-at
- `identity()` — static identity constant

**Arithmetic**
- `add(q)`, `sub(q)`, `mul(q)`, `mul(s)`, `div(q)`
- `premul(q)` — q * this
- `negate()`, `conjugate()`, `invert()`
- `log()`, `exp()`, `pow(exp)`, `sqrt()`

**Properties**
- `length()`, `lengthSquared()`, `norm()`
- `normalize()`, `dot(q)`
- `angle()`, `angle(q)`, `difference(q)`

**Interpolation**
- `lerp(target, t)` — linear interpolation (no normalization)
- `nlerp(target, t)` — normalized linear interpolation
- `slerp(target, t)` — spherical linear interpolation
- `squad(a, b, q2, t)` — cubic spline interpolation
- Static: `squad(q0, q1, q2, q3, t)`, `spline(q0, q1, q2, q3, t)`
- Static: `nlerp(Quaterniond[] path, double t)` — array-based nlerp

**Conversions**
- `toMatrix()` / `toMatrix4()` — converts to 4×4 matrix
- `toMatrix3()` — converts to 3×3 matrix
- `toAxisAngle()` — converts to AxisAngle
- `getEulerAnglesXYZ()` — extracts Euler angles

**Vector Transform**
- `transform(v)` — applies rotation to vector
- `positiveX/Y/Z()` — basis vectors after rotation

**Buffer / Memory I/O**
- `fromBuffer(Buffer)`, `writeToBuffer(Buffer)`
- `fromMemorySegment(MemorySegment, offset)`, `writeToMemorySegment(MemorySegment, offset)`
- `read(MemorySegment, offset)`, `write(MemorySegment, offset)`

**Predicates**
- `isFinite()`, `equals(q, delta)`

---

## Axis-Angle

### `AxisAngle4d` / `AxisAngle4f` — Record (immutable)

- Fields: `x`, `y`, `z` (axis), `angle` (radians)
- Constructors: from components, from quaternion
- `toQuaternion()` — converts to Quaterniond/f
- `rotate(v)` — rotates a vector

---

## Dual Numbers

### `DualNumberd` / `DualNumberf` — Record (immutable)

For automatic differentiation in one variable. Dual numbers encode a value and its derivative simultaneously.

**Arithmetic**
- `add(dn)`, `sub(dn)`, `mul(dn)` (uses `Math.fma`), `div(dn)`
- `negate()`, `conjugate()`

**Functions**
- `pow(n)` — power via dual expansion
- `sqrt()` — sqrt via dual expansion
- `sin()`, `cos()`, `exp()`, `log()` — elementary functions with auto-diff

**Fields**
- `real()` — the value
- `dual()` — the derivative

---

## Scalar Math

### `ScalarMath` — Static utility (final, private constructor)

A comprehensive scalar math library with **~80+ methods** across 20+ categories. Far beyond what `Math` or JOML provide.

**Constants** (12)
- `PI`, `E`, `TAU`, `PHI` (golden ratio), `LN2`, `LN10`, `LOG2E`, `LOG10E`, `SQRT2`, `SQRT3`, `EPSILON`, `FLT_EPSILON`

**Trigonometric** (18)
- Standard: `sin`, `cos`, `tan`, `asin`, `acos`, `atan`, `atan2`
- Fast approx: `fastSin(float/double)`, `fastCos(float/double)`, `sinCos(float/double)` (returns both)
- Reciprocal: `sec`, `csc`, `cot`, `asec`, `acsc`, `acot`
- Historical: `versin`, `coversin`, `haversin`, `exsec`, `excsc`, `vercosin`, `covercosin`, `havercosin`, `hacoversin`, `hacovercosin`
- Misc: `chord`, `sinc`

**Hyperbolic** (8)
- `sinh`, `cosh`, `tanh`, `sech`, `csch`, `coth`, `asinh`, `acosh`, `atanh`

**Exponential & Logarithmic** (9)
- `exp`, `exp2`, `expm1`, `log`, `log10`, `log2`, `log1p`, `logb`, `pow`

**Roots & Powers** (4)
- `sqrt`, `invSqrt`, `cbrt`, `pow`

**Arithmetic Utilities** (8)
- `abs` (double, float, int, long), `min`, `max`, `clamp`, `saturate`
- `mod`, `wrap`, `pingPong`, `sawtooth`, `triangle`, `square`
- `fma`, `hypot`, `hypot3`

**Rounding** (5)
- `floor`, `ceil`, `round`, `trunc`, `frac`
- `roundTo`, `floorTo`, `ceilTo`, `snap`

**Sign / Comparison** (4)
- `signum`, `copySign`
- `nextUp`, `nextDown`, `ulp`
- `isFinite`, `isNaN`, `isInfinite`

**Interpolation** (8)
- `lerp`, `lerpAngle`, `inverseLerp`, `map`
- `bilinear`, `smoothstep`, `smootherstep`, `cosineInterpolation`

**Spline Interpolation** (3)
- `catmullRom`, `hermite`, `bezier`

**Easing / Waves** (4)
- `step`, `pulse`, `ramp`
- `pingPong`, `sawtooth`, `triangle`, `square`

**Angle Conversions** (4)
- `toRadians`, `toDegrees`, `normalizeAngle`, `degrees`, `radians`

**Activation Functions** (10)
- `gaussian`, `logistic`, `logit`, `softplus`, `relu`, `leakyRelu`
- `elu`, `gelu`, `softsign`, `swish`, `hardSigmoid`, `selu`

**Special Functions** (5)
- `erf`, `erfc`, `invErf`, `gamma`, `lgamma`
- `beta`, `riemannZeta`, `lambertW0`
- `fresnelC`, `fresnelS`

**Combinatorics** (6)
- `factorial`, `factorialLong`, `binomial`
- `permutations`, `stirlingFirstKind`, `stirlingSecondKind`

**Number Theory** (7)
- `gcd`, `lcm`, `isPrime`, `isCoprime`, `nextPrime`, `totient`, `radical`

**Bit Operations** (4)
- `isPowerOfTwo`, `nextPowerOfTwo`, `prevPowerOfTwo`, `roundUpToPowerOfTwo`

**Integer Sequences** (3)
- `fibonacci`, `lucas`, `catalan`, `bellNumber`

**Statistics** (20)
- `sum`, `product`, `mean`, `median`, `percentile`
- `geometricMean`, `harmonicMean`, `quadraticMean`, `weightedMean`, `trimmedMean`
- `variance`, `populationVariance`, `stddev`, `populationStddev`
- `skewness`, `kurtosis`, `mode`, `standardError`, `zScore`
- `entropy`, `movingAverage`, `exponentialMovingAverage`, `normalizeData`, `standardize`
- `covariance`, `correlation`
- `arrayMin`, `arrayMax`, `argmin`, `argmax`

**Distance Metrics** (4)
- `distanceManhattan`, `distanceChebyshev`, `distanceMinkowski`, `haversineDistance`

**Geometry** (2)
- `areaTriangleHeron`, `signedArea2D`

### `MathLib` — Facade

A convenience class re-exposing every `ScalarMath` method as a static call. Use `MathLib.sin(x)` instead of `ScalarMath.sin(x)`.

---

## Interpolation / Easing

### `Interpolationd` / `Interpolationf` — Static utility

34 easing functions each in double and float precision:

| Category    | Functions                                             |
|-------------|-------------------------------------------------------|
| Linear      | `linear`                                              |
| Quadratic   | `quadraticIn`, `quadraticOut`, `quadraticInOut`       |
| Cubic       | `cubicIn`, `cubicOut`, `cubicInOut`                   |
| Quartic     | `quarticIn`, `quarticOut`, `quarticInOut`             |
| Quintic     | `quinticIn`, `quinticOut`, `quinticInOut`             |
| Sinusoidal  | `sinusoidalIn`, `sinusoidalOut`, `sinusoidalInOut`    |
| Exponential | `exponentialIn`, `exponentialOut`, `exponentialInOut` |
| Circular    | `circularIn`, `circularOut`, `circularInOut`          |
| Elastic     | `elasticIn`, `elasticOut`, `elasticInOut`             |
| Back        | `backIn`, `backOut`, `backInOut`                      |
| Bounce      | `bounceIn`, `bounceOut`, `bounceInOut`                |
| Misc        | `smoothStep`, `smootherStep`                          |

---

## Geometry Types

### `AABB` — Axis-Aligned Bounding Box (mutable)

- Constructors: default (empty), from min/max vectors, from single point, from sphere
- `center()`, `extent()`, `size()` — Vector3d accessors
- `contains(point)`, `contains(aabb)` — containment tests
- `intersects(aabb)` — overlap test
- `union(aabb)`, `union(point)` — expand to enclose
- `intersection(aabb)` — intersection volume (new AABB)
- `transform(matrix)` — transform and return new AABB
- `isEmpty()`, `isInfinite()`

### `Ray` — Ray (mutable)

- Fields: `origin`, `direction` (Vector3d)
- `pointAt(t)` — parametric point
- `distanceTo(point)` — closest distance
- `closestPoint(point)` — closest point on ray
- `intersects(AABB)`, `intersects(Sphere)`, `intersects(Plane)` — intersection boolean tests
- `intersectsTriangle(v0, v1, v2)` — Möller-Trumbore ray-triangle (returns t or null)
- `transform(matrix)` — transforms ray

### `Plane` — Plane (record, immutable)

- Fields: `a`, `b`, `c`, `d` (plane: ax + by + cz + d = 0)
- Constructors: from normal + point, from three points, from coefficients
- `normal()` — returns the normal (Vector3d)
- `signedDistance(point)` — signed distance
- `distance(point)` — absolute distance
- `intersects(Ray)` — ray intersection (returns t)
- `intersects(Plane)` — plane-plane intersection (returns Ray)
- `normalize()` — returns normalized Plane

### `Sphere` — Sphere (record, immutable)

- Fields: `center` (Vector3d), `radius` (double)
- `contains(point)` — point containment
- `intersects(Sphere)`, `intersects(Ray)`, `intersects(AABB)` — intersection tests

### `AABBf` — Float AABB (mutable)

- Same API as `AABB` but uses `float` precision (`Vector3f`)
- `center()`, `extent()`, `size()`, `contains()`, `intersects()`, `union()`, `intersection()`, `transform()`

### `Planef` — Float Plane (record, immutable)

- Same API as `Plane` but uses `float` fields
- Fields: `a`, `b`, `c`, `d` (float)

### `Rayf` — Float Ray (mutable)

- Same API as `Ray` but uses `Vector3f`
- `origin`, `direction`, `pointAt(t)`, `closestPoint(point)`, `intersectsTriangle(v0, v1, v2)`

### `Spheref` — Float Sphere (record, immutable)

- Same API as `Sphere` but uses `Vector3f` + `float` radius

### `Line` — 2D Line (record, immutable)

- Fields: `point`, `direction` (Vector3f, xy used)
- `distance(Vector3f p)` — distance from point to infinite line
- `closestPoint(Vector3f p)` — closest point on line

### `Circle` — 2D Circle (record, immutable)

- Fields: `center` (Vector3f), `radius` (float)
- `contains(Vector3f p)`, `contains(float px, float py)`
- `intersects(Circle other)`
- `area()`

### `Rectangle` — 2D Rectangle (record, immutable)

- Fields: `minX`, `minY`, `maxX`, `maxY` (float)
- `Rectangle.ofSize(cx, cy, w, h)` — centered construction
- `contains(float px, float py)`, `contains(Vector3f p)`
- `intersects(Rectangle)`, `intersects(Circle)`
- `width()`, `height()`, `area()`, `center()`

---

## Intersection Tests

### `Intersection` / `Intersectiond` — Static utilities (double precision)

~50 intersection and distance methods, both classes providing the same API:

**Ray Intersections**
- `intersectRayTriangle(Ray, v0, v1, v2, result)` — Möller-Trumbore
- `intersectRayAABB(origin, dir, aabb)` — slab method
- `intersectRaySphere(origin, dir, sphere)`
- `intersectRayPlane(origin, dir, plane)`
- `testRayTriangle(Ray, v0, v1, v2, result)` — hit test
- `testRayQuad`, `testRaySphere`, `testRayPlane`, `testRayCircle`, `testRayDisc`

**Segment Intersections**
- `intersectSegmentTriangle(p0, p1, v0, v1, v2, result)`
- `intersectSegmentAABB`, `intersectSegmentSphere`, `intersectSegmentPlane`
- `testLineSegmentTriangle`, `testLineSegmentAABB`

**Shape Intersections**
- `intersectAABBAABB`, `intersectSphereSphere`
- `intersectTriangleTriangle` — boolean
- `testSphereTriangle`, `testSphereAABB`, `testSpherePlane`, `testSphereFrustum`
- `testAABBPlane`, `testAABBFrustum`

**Point Distance & Closest Point**
- `distancePointSegment`, `distancePointTriangle`, `distancePointLine`, `distancePointPlane`
- `signedDistancePointPlane`
- `closestPointTriangle`, `closestPointLine`, `closestPointPlane`
- `closestPointSegmentSegment`

**Plane Intersections**
- `intersectPlanePlane`, `intersectPlanePlanePlane`
- `intersectPolygonPlane`

**Frustum Tests**
- `testPointFrustum`, `intersectSphereFrustum`, `intersectAABBFrustum`
- `isPointInsideFrustum`, `isAabbInsideFrustum`

**Other**
- `transformAab`, `findClosestPointsLineLine`, `intersectLineLine`

### `PolygonsIntersection` — Static utility

Interval-tree accelerated point-in-polygon test:
- `testPolygon(Vector3f point, float[] verticesX, float[] verticesY)` — boolean test
- `testPolygon(Vector3d point, double[] verticesX, double[] verticesY)` — double variant
- `PolygonsIntersection(float[] verticesX, float[] verticesY)` — pre-built with interval tree (thread-safe after construction)

---

## Frustum Culling

### `FrustumIntersection` — Mutable class

- Constructors: `FrustumIntersection()` (OpenGL, z=[-1,1]), `FrustumIntersection(clipSpace)` (Vulkan: clipSpace=true, z=[0,1])
- `set(Matrix4f)` — extracts 6 frustum planes from projection-view matrix
- `intersects(AABB)` — AABB-frustum test (returns INSIDE/INTERSECT/OUTSIDE)
- `intersects(Sphere)` — sphere-frustum test (returns same enum)
- `intersects(Vector3f)` — point-frustum test (returns boolean)
- `plane(index)` — access individual frustum planes

---

## Geometry Utilities

### `GeometryUtils` — Static utility

- `computeTangent(v0, v1, v2, uv0, uv1, uv2)` — tangent from triangle + UVs (float/double)
- `computeBitangent(v0, v1, v2, uv0, uv1, uv2)` — bitangent from triangle + UVs (float/double)
- `computeNormal(v0, v1, v2)` — normalized face normal (float/double)
- `computeTangentBitangent(v0, v1, v2, uv0, uv1, uv2)` — returns Vector3[2] (float/double)
- `rotationMatrix(from, to)` — Matrix3 from two direction vectors (float/double)
- `barycentric(point, v0, v1, v2)` — barycentric coordinates (float/double)
- `interpolateBarycentric(v0, v1, v2, u, v, w)` — scalar barycentric interpolation
- `areaTriangle(v0, v1, v2)` — triangle area (float/double)
- `centroid(v0, v1, v2)` — triangle centroid (float/double)
- `orthonormalBasis(normal)` — returns Vector3[3] {tangent, bitangent, normal} (float/double)
- `transformNormal(matrix, v)` — normal transform via inverse-transpose (Matrix3/Matrix4, float/double)

---

## Noise Generation

### `SimplexNoise` — Static utility

Simplex noise (Ken Perlin's simplex algorithm, Gustavson implementation):
- `noise(x, y)` — 2D, `noise(x, y, z)` — 3D, `noise(x, y, z, w)` — 4D
- Seeded overloads: `noise(x, y, seed)`, `noise(x, y, z, seed)`, `noise(x, y, z, w, seed)`
- Float variants: `noise(float x, float y)`, `noise(float x, float y, float z)`

### `PerlinNoise` — Static utility

Classic Improved Perlin Noise (Perlin 2002):
- `noise(x)` — 1D, `noise(x, y)` — 2D, `noise(x, y, z)` — 3D
- Seeded variants: `noise(x, seed)`, `noise(x, y, seed)`, `noise(x, y, z, seed)`
- Float variants for all dimensions
- `fBm(x, y [, z], octaves, lacunarity, gain)` — fractal Brownian motion

### `ColorMath` — Static utility

Color space conversions and packing:
- `srgbToLinear(float)`, `linearToSrgb(float)` — single component
- `srgbToLinear(r, g, b)` / `linearToSrgb(r, g, b)` — returns `Vector3f`
- `packRgb(r, g, b)` / `packRgba(r, g, b, a)` — float [0,1] to packed int
- `unpackRgb(int)` / `unpackRgba(int)` — unpack to `Vector3f`
- `hsvToRgb(h, s, v)` — HSV → RGB as Vector3f
- `rgbToHsv(r, g, b)` — RGB → HSV as `Vector3f`
- `colorTemperature(kelvin)` — approximate blackbody color

### `MemUtil` — Static utility

Bulk memory operations:
- `copy(float[]/double[], srcOff, dest[], destOff, length)` — array copy
- `fill(float[]/double[], off, len, val)` — array fill
- `memcpy(T[], srcOff, T[], destOff, len)` — generic array copy

### `RandomUtil` — Static utility

Thin wrappers over `java.util.random.RandomGenerator`:
- `nextFloat(rng, min, max)` / `nextDouble(rng, min, max)` — range
- `randomPointInSphere(rng, radius)` — uniform in sphere volume
- `randomPointOnSphere(rng, radius)` — uniform on sphere surface
- `randomPointInCircle(rng, radius)` — uniform in 2D circle
- `randomVector2f(rng, min, max)` / `randomVector3f(rng, min, max)` — random vectors

---

## Frustum Ray Builder

### `FrustumRayBuilder` — Mutable class

- `set(Matrix4d invProjView)` — computes corner rays from inverse projection-view matrix
- `getRay(x, y)` — bilinear interpolation of ray direction (normalized coords)
- `getRay(x, y, dest)` — no-alloc version
- `normalize()` — normalizes all four corner directions

---

## Math Utility Facade

### `MathLib` — Static convenience

Re-exports all `ScalarMath` methods and constants under a single namespace: `MathLib.sin(x)`, `MathLib.exp(x)`, `MathLib.erf(x)`, etc.

---

## Incubator: SIMD Vector API

### `VectorApiMath` — Static utility (package `com.jamma.math.incubator`)

Uses `jdk.incubator.vector.DoubleVector` (SPECIES_256) and `FloatVector` (SPECIES_128) for SIMD-accelerated operations:

**Double-precision (Vector4d)**
- `add(a, b)`, `sub(a, b)`, `mul(a, b)`, `scale(v, s)`
- `dot(a, b)` — uses `reduceLanes(ADD)`
- `batchAdd(results, a, b)` — batch add for arrays of Vector4d
- `batchDot(a, b)` — batch dot product → `double[]`
- `transform(Matrix4d, Vector4d)` — SIMD matrix-vector multiply

**Double-precision padded (Vector3d)**
- `add3(a, b)`, `sub3(a, b)`, `scale3(v, s)`, `dot3(a, b)` — padded to 4-wide for SIMD

**Single-precision (Vector4f)**
- `add4f(a, b)`, `sub4f(a, b)`, `mul4f(a, b)`, `scale4f(v, s)`
- `dot4f(a, b)` — SIMD dot product
- `batchAdd4f(results, a, b)` — batch add for arrays of Vector4f

---

## Incubator: Parallel Operations

### `ParallelOps` — Static utility (package `com.jamma.math.incubator`)

All parallel ops use `StructuredTaskScope` with a 4096-element threshold. Work is divided across `availableProcessors()` cores; falls back to sequential below threshold.

**Vector3d**
- `batchAddParallel(a, b)`, `batchSubParallel(a, b)`, `batchMulParallel(a, b)`
- `batchDotParallel(a, b)` → `double[]`
- `batchCrossParallel(a, b)`
- `batchNormalizeParallel(v)`, `batchScaleParallel(v, s)`
- `batchTransformParallel(Matrix4d, v[])` — transforms positions

**Vector4d**
- `batchAddParallel(a, b)`
- `batchTransformParallel(Matrix4d, v[])` — full 4-component transform

---

## Incubator: Off-Heap Memory

### `MemoryOps` — Static utility (package `com.jamma.math.incubator`)

Structured off-heap memory layouts using `MemoryLayout` + `VarHandle` via `java.lang.foreign`.

**Vec4 (double)** — `allocateVec4(Arena)`, `allocateVec4Array(Arena, count)`
- `setVec4(segment, x, y, z, w)`, `setVec4(segment, index, Vector4d)`
- `getVec4(segment, index)` → `Vector4d`, `getVec4X/Y/Z/W(segment)`

**Vec3 (double)** — `allocateVec3(Arena)`, `allocateVec3Array(Arena, count)`
- `setVec3(segment, index, Vector3d)`, `getVec3(segment, index)` → `Vector3d`

**Vec2 (double)** — `allocateVec2(Arena)`, `allocateVec2Array(Arena, count)`
- `setVec2(segment, index, Vector2d)`, `getVec2(segment, index)` → `Vector2d`

**Quaterniond (double)** — `allocateQuaterniond(Arena)`, `allocateQuaterniondArray(Arena, count)`
- `setQuaterniond(segment, index, Quaterniond)`, `getQuaterniond(segment, index)` → `Quaterniond`

**Matrix4d (column-major, 16 doubles)** — `allocateMatrix4d(Arena)`, `allocateMatrix4dArray(Arena, count)`
- `setMatrix4d(segment, index, Matrix4d)`, `getMatrix4d(segment, index)` → `Matrix4d`

**Bulk operations**
- `copyVec4Array(src, srcIdx, dst, dstIdx, count)`
- `copyVec3Array(src, srcIdx, dst, dstIdx, count)`
- `copyMatrix4dArray(src, srcIdx, dst, dstIdx, count)`
- `writeVec4Array(segment, offset, Vector4d[])`, `readVec4Array(segment, offset, Vector4d[])`

---

## Memory & Buffer I/O

Every vector and matrix type supports multiple I/O backends:

| Backend           | Read                                                  | Write                                                     |
|-------------------|-------------------------------------------------------|-----------------------------------------------------------|
| **NIO Buffer**    | `fromBuffer(Buffer)`, `fromBuffer(idx, Buffer)`       | `writeToBuffer(Buffer)`, `writeToBuffer(idx, Buffer)`     |
| **MemorySegment** | `fromMemorySegment(seg, offset)`, `read(seg, offset)` | `writeToMemorySegment(seg, offset)`, `write(seg, offset)` |

Supported buffer types:
- `DoubleBuffer` — Vector*d, Matrix*d, Quaterniond
- `FloatBuffer` — Vector*f, Matrix*f, Quaternionf
- `IntBuffer` — Vector*i

---

## JOML Comparison

| Feature                    | Jamma                                                                           | JOML 1.10.8                |
|----------------------------|---------------------------------------------------------------------------------|----------------------------|
| **Vector mutability**      | Immutable (records)                                                             | Mutable                    |
| **Vector types**           | 2d/f/i, 3d/f/i, 4d/f/i                                                          | 2d/f, 3d/f, 4d/f           |
| **Integer vectors**        | `Vector2i`, `Vector3i`, `Vector4i`                                              | ❌                          |
| **Matrix types**           | 2×2, 3×3, 3×2, 4×4, 4×3 (d/f)                                                   | 2×2, 3×3, 4×4, 4×3 (d/f)   |
| **Matrix4d parity**        | 100% (all Matrix4f methods)                                                     | N/A (reference)            |
| **MatrixStack**            | ✅ Push/pop scene graph                                                          | ❌                          |
| **Quaternion**             | Record (immutable)                                                              | Mutable class              |
| **SLERP / SQUAD**          | ✅                                                                               | ✅                          |
| **Quaternion log/exp/pow** | ✅                                                                               | ✅                          |
| **AxisAngle**              | Record (immutable)                                                              | Mutable class              |
| **Dual numbers**           | `DualNumberd/f`                                                                 | ❌                          |
| **ScalarMath**             | 80+ methods, stats, special functions, combinatorics, activation functions      | ❌ (only basic `Math`)      |
| **Easing functions**       | 34 functions, double + float                                                    | ❌                          |
| **Noise generation**       | `SimplexNoise` + `PerlinNoise` (2D/3D/4D, seeded, fBm)                          | ❌                          |
| **Color math**             | `ColorMath` (sRGB↔linear, HSV, pack/unpack, color temp)                         | ❌                          |
| **2D geometry**            | `Line`, `Circle`, `Rectangle`                                                   | ❌                          |
| **MemUtil / RandomUtil**   | ✅ Array ops + random vector helpers                                             | ❌                          |
| **Integer vector types**   | ✅ 2i/3i/4i                                                                      | ❌                          |
| **Java records**           | ✅ All vectors and quaternions                                                   | ❌                          |
| **JPMS module-info**       | ✅ Both modules                                                                  | ❌                          |
| **MemorySegment I/O**      | ✅ All types (vectors, matrices, quaternions)                                    | Partial (NIO only)         |
| **NIO Buffer I/O**         | ✅ All types                                                                     | ✅                          |
| **SIMD (Vector API)**      | ✅ Incubator module (`VectorApiMath`)                                            | ✅ (later versions)         |
| **Parallel batch ops**     | ✅ `ParallelOps` with `StructuredTaskScope`                                      | ❌                          |
| **Off-heap memory**        | ✅ `MemoryOps`                                                                   | ❌                          |
| **`Math.fma` usage**       | ✅ All dot products, lerps, matrix mul                                           | Partial                    |
| **Frustum culling**        | ✅ AABB, sphere, point (OpenGL + Vulkan)                                         | ✅ (more variants)          |
| **Ray casting**            | ✅ Ray-AABB, Ray-Sphere, Ray-Plane, Ray-Triangle, Ray-Quad, Ray-Circle, Ray-Disc | ✅                          |
| **AABB**                   | ✅ Union, intersection, containment, transform                                   | ✅                          |
| **Plane**                  | ✅ Record (immutable)                                                            | ✅ Mutable class            |
| **Sphere**                 | ✅ Record (immutable)                                                            | ✅ Mutable class            |
| **Intersection tests**     | ~50 static methods (ray, segment, plane, sphere, AABB, frustum, closest-point)  | ~50 static methods         |
| **Geometry utilities**     | ✅ Tangents, normals, barycentrics, orthonormal basis                            | Partial                    |
| **Frustum ray builder**    | ✅                                                                               | ❌ (manual)                 |
| **Thread safety**          | ✅ Trivially safe (records)                                                      | ❌ Not safe                 |
| **`equals`/`hashCode`**    | ✅ Correct (records)                                                             | ❌ Often omitted/incomplete |
| **Serialization**          | ✅ `Serializable` on all core types                                              | Partial                    |
| **License**                | MIT                                                                             | MIT                        |
| **Documentation**          | This README                                                                     | Extensive Javadoc          |
| **Maturity**               | New                                                                             | Battle-tested (10+ years)  |
| **LWJGL integration**      | ✅ Native (`lwjgl-jamma` module)                                                 | ✅ Native                   |
| **GC pressure**            | Minor (short-lived records)                                                     | None (mutable in-place)    |

---

## Why Not JOML?

### 1. Immutability
JOML uses mutable objects everywhere. This is a legacy design from the Java 6 era. `v.add(w)` mutates `v` instead of returning a new vector. This means:
- You must defensively copy before passing to other code
- Concurrent access is unsafe
- `equals`/`hashCode` are unreliable (inherited from `Object`)
- Chaining mutates intermediate state: `v.add(w).normalize()` changes `v` as a side effect

Jamma's records fix all of this. `v.add(w)` returns a new vector; `v` is untouched.

### 2. No Scalar Math
JOML provides vector and matrix types and nothing else. Need a smoothstep? Write it yourself. Need erf, gamma, Fibonacci, or a moving average? Write it yourself. Jamma's `ScalarMath` covers **80+ functions** from trig to special functions to statistics to combinatorics.

### 3. No Integer Vectors
JOML has no `Vector2i`, `Vector3i`, or `Vector4i`. If you work with integer coordinates (chunks, voxels, screen coords, texture atlases), you're converting to float.

### 4. No Dual Numbers
Automatic differentiation is essential for gradient-based optimization, physics, and numerical methods. JOML doesn't support it.

### 5. No JPMS
JOML ships without `module-info.java`. In modern Java, this means `--add-exports` flags or classpath pollution. Jamma is fully modular.

### 6. No MemorySegment Support
JOML relies on NIO buffers. Jamma supports both NIO buffers and the modern `java.lang.foreign.MemorySegment` API, making it compatible with off-heap memory, GPU interop, and the Foreign Function & Memory API.

### 7. No Parallel / SIMD Incubator
JOML has some SIMD support, but Jamma's incubator module includes batch parallel operations using `StructuredTaskScope` and explicit Vector API usage.

### 8. Immutable Vectors Are Fast Enough
Modern JVMs eliminate short-lived allocations via escape analysis. Even when allocations survive, generational ZGC collects them at sub-millisecond cost. The safety and correctness wins of immutability far outweigh the theoretical performance difference.

---

---

## LWJGL Interop (`lwjgl-jamma`)

### Dependency

```groovy
// build.gradle
dependencies {
    implementation 'com.jamma:lwjgl-jamma:1.0-SNAPSHOT'
    implementation platform('org.lwjgl:lwjgl-bom:3.4.1')
    implementation 'org.lwjgl:lwjgl'
}
```

### Uploading a matrix to an OpenGL shader uniform

```java
import static com.jamma.lwjgl.JammaLWJGL.*;
import static org.lwjgl.opengl.GL20.*;

try (var stack = MemoryStack.stackPush()) {
    FloatBuffer buf = stack.mallocFloat(16);
    put(matrix, buf);
    buf.flip();
    glUniformMatrix4fv(location, false, buf);
}
```

### Uploading to a Vulkan uniform buffer

```java
import static com.jamma.lwjgl.JammaLWJGL.*;

try (var arena = Arena.ofConfined()) {
    MemorySegment seg = arena.allocate(16 * 4); // 16 floats
    put(matrix, seg, 0);
    // pass seg.address() to vkCmdPushConstants or vkMapMemory
}
```

### Writing vertex data

```java
FloatBuffer buf = stack.mallocFloat(9); // 3 vertices × 3 components
put(new Vector3f(1, 0, 0), buf);
put(new Vector3f(0, 1, 0), buf);
put(new Vector3f(0, 0, 1), buf);
buf.flip();
glBufferSubData(GL_ARRAY_BUFFER, 0, buf);
```

### Reading from an Assimp struct

```java
// AIAIBone.mOffsetMatrix is stored column-major as floats
Matrix4f boneMatrix = new Matrix4f();
get(aiBone.mOffsetMatrix().asFloatBuffer(), boneMatrix);
```

### Available methods

| Method                                         | Input        | Output                      |
|------------------------------------------------|--------------|-----------------------------|
| `put(Matrix4f, FloatBuffer)`                   | Jamma type   | LWJGL buffer (column-major) |
| `put(Matrix4f, MemorySegment, long)`           | Jamma type   | Off-heap memory             |
| `get(FloatBuffer, Matrix4f)`                   | LWJGL buffer | Jamma type                  |
| `put(Matrix4d, DoubleBuffer)`                  | Jamma type   | LWJGL buffer                |
| `put(Vector3f, FloatBuffer)`                   | Jamma type   | xyz in buffer               |
| `put(Vector3f, MemorySegment, long)`           | Jamma type   | Off-heap memory             |
| `put(Vector4f, FloatBuffer)`                   | Jamma type   | xyzw in buffer              |
| `put(Vector2f, FloatBuffer)`                   | Jamma type   | xy in buffer                |
| `put(Quaternionf, FloatBuffer)`                | Jamma type   | xyzw in buffer              |
| `segment(ByteBuffer/FloatBuffer/DoubleBuffer)` | NIO buffer   | `MemorySegment`             |

All methods are zero-allocation — they write directly into the provided buffer.

---

## JOML Compatibility Adapter

### Module: `joml-adapter`

A drop-in bridge for projects migrating from JOML to Jamma. Provides mutable adapter classes with JOML's `return this` fluent API, backed by Jamma types under the hood.

**Migration path:**

1. Add `joml-adapter` dependency (replace `org.joml:joml`)
2. Change imports: `org.joml.Vector3f` → `com.jamma.joml.Vector3f`
3. Code compiles and runs identically
4. Gradually opt into Jamma's immutable types via `.toJamma()`

### Adapter Types

| Adapter class         | JOML equivalent      | Key API coverage                                      |
|-----------------------|----------------------|-------------------------------------------------------|
| `Vector3f` / `Vector3d` | `org.joml.Vector3f/d` | set, add, sub, mul, div, normalize, cross, dot, reflect, rotate, transform matrix/quat, mulPosition/Direction, fma, MemorySegment load/store |
| `Vector4f` / `Vector4d` | `org.joml.Vector4f/d` | set, add, sub, mul, div, normalize, dot, mul(Matrix4f/d), mulPosition, mulDirection |
| `Vector2f` / `Vector2d` | `org.joml.Vector2f/d` | set, add, sub, mul, div, normalize, dot, cross, rotate, angle, perpendicular |
| `Quaternionf` / `Quaterniond` | `org.joml.Quaternionf/d` | normalize, conjugate, invert, mul, premul, slerp, nlerp, fromAxisAngle, fromEulerAnglesXYZ |
| `Matrix4f` / `Matrix4d` | `org.joml.Matrix4f/d` | identity, mul, translate, scale, rotateX/Y/Z, transpose, invert, determinant, perspective, lookAt, transformPosition/Direction |
| `JomlCompat`          | —                    | Static `toJamma()` / `toJoml()` between JOML, adapter, and native Jamma types |

### Example

```java
// Before (JOML)
// import org.joml.Vector3f;

// After (Jamma adapter — same API)
import com.jamma.joml.Vector3f;

Vector3f v = new Vector3f(1, 2, 3).normalize().mul(5);
com.jamma.math.Vector3f jamma = v.toJamma(); // convert to immutable record
```

---

## Static Utility Classes (legacy)

### `VectorMath` / `VectorMathf`
Static utility classes mirroring all instance methods on the record types. These exist for compatibility and cases where a functional-style `VectorMath.add(a, b)` is preferred over `a.add(b)`. Note that the instance methods on the records themselves are the primary API.

---

## Building

```bash
# Build math-core only
./gradlew :math-core:build

# Build math-incubator (requires --enable-preview)
./gradlew :math-incubator:build

# Build lwjgl-jamma (requires LWJGL dependencies on classpath)
./gradlew :lwjgl-jamma:build

# Build joml-adapter (optional: needs JOML on classpath for JomlCompat)
./gradlew :joml-adapter:build

# Build everything
./gradlew build

# Run tests
./gradlew test
```

### JVM Flags for Incubator Module

```bash
--add-modules jdk.incubator.vector --enable-preview
```

---

## License

MIT License — see [LICENSE](LICENSE).

Copyright © 2026 ExodusCoder9
