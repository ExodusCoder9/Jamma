module com.jamma.joml {
    exports com.jamma.joml;

    requires transitive com.jamma.math;

    // JOML is optional — only needed for conversion utilities
    requires static org.joml;
}
