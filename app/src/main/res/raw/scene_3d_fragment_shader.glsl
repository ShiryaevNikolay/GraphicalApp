precision mediump float;

uniform vec3 u_camera;
uniform vec3 u_light;
uniform sampler2D u_texture;

varying vec3 v_vertex;
varying vec3 v_normal;
varying vec2 v_texture_coords;

void main() {

    vec3 n_normal = normalize(v_normal);
    vec3 light_vector = normalize(u_light - v_vertex);
    vec3 look_vector = normalize(u_camera - v_vertex);

    float ambient = 0.2;
    float k_diffuse = 0.8;
    float k_specular = 0.4;
    float diffuse = k_diffuse * max(dot(n_normal, light_vector), 0.0);
    vec3 reflect_vector = reflect(-light_vector, n_normal);
    float specular = k_specular * pow(max(dot(light_vector, reflect_vector), 0.0), 40.0);
    vec4 one = vec4(1.0, 1.0, 1.0, 1.0);

    vec4 color = vec4(1, 0.5, 0, 1.0);
    gl_FragColor = 2.0 * (ambient + diffuse + specular) * texture2D(u_texture, v_texture_coords) * one;
}