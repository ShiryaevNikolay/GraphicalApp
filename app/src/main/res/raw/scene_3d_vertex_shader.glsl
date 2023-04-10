attribute vec3 position;
attribute vec3 a_normal;
attribute vec2 a_texture_coords;

uniform mat4 matrix;

varying vec3 v_vertex;
varying vec3 v_normal;
varying vec2 v_texture_coords;

void main() {
    v_vertex = position;
    v_normal = normalize(a_normal);
    v_texture_coords = a_texture_coords;

    gl_Position = matrix * vec4(position, 1.0);
}