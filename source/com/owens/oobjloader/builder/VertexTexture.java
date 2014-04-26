/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.builder;

import java.util.*;
import java.text.*;
import java.io.*;
import java.io.IOException;

public class VertexTexture {

    public float u = 0;
    public float v = 0;

    VertexTexture(float u, float v) {
        this.u = u;
        this.v = v;
    }

    public String toString() {
        if (null == this) {
            return "null";
        } else {
            return u + "," + v;
        }
    }
}