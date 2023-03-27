uniform mat4 u_modelViewProjectionMatrix;

attribute vec3 a_vertex;
attribute vec3 a_normal;
attribute vec4 a_color;

varying vec3 v_vertex;
varying vec3 v_normal;
varying vec4 v_color;
// определяем переменные для передачи
// координат текстур на интерполяцию
varying vec2 v_texcoord;

void main() {
    v_vertex = a_vertex;

    vec3 n_normal = normalize(a_normal);
    v_normal = n_normal;
    v_color = a_color;

    // вычисляем координаты первой текстуры и отравляем их на интерполяцию
    // пусть координата текстуры S будет равна координате вершины X
    // v_texcoord.s=a_vertex.x;
    // а координата текстуры T будет равна координате вершины Z
    //v_texcoord0.t=a_vertex.z;

    float r = a_vertex.x * a_vertex.x + a_vertex.y * a_vertex.y;
    v_texcoord = 0.3 * r * a_vertex.xy;

    gl_Position = u_modelViewProjectionMatrix * vec4(a_vertex, 1.0);
}
