attribute vec3 position;
attribute vec3 a_normal;

uniform mat4 matrix;

varying vec3 v_vertex;
varying vec3 v_normal;

void main() {
    v_vertex = position;
    v_normal = normalize(a_normal);

    gl_Position = matrix * vec4(position, 1.0);
}