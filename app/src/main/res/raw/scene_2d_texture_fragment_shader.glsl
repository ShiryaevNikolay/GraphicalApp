precision mediump float;

uniform vec3 u_camera;
uniform vec3 u_lightPosition;

uniform sampler2D u_texture;

varying vec3 v_vertex;
varying vec3 v_normal;
varying vec4 v_color;
// принимаем координат текстур после интерполяции
varying vec2 v_texcoord;

void main()
{
    vec3 n_normal = normalize(v_normal);
    vec3 lightvector = normalize(u_lightPosition - v_vertex);
    vec3 lookvector = normalize(u_camera - v_vertex);
    float ambient = 0.2;
    float k_diffuse = 0.8;
    float k_specular = 0.4;
    float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0);
    vec3 reflectvector = reflect(-lightvector, n_normal);
    float specular = k_specular * pow(max(dot(lookvector, reflectvector), 0.0), 40.0);
    vec4 one = vec4(1.0, 1.0, 1.0, 1.0);

    // оставим пока квадрат временно без освещения и выполним смешивание текстуры
    // вычисляем координаты первой текстуры
//    float r = v_vertex.x * v_vertex.x + v_vertex.z * v_vertex.z;
//    vec2 texcoord = 0.3 * r * v_vertex.xz;
    // вычисляем цвет пикселя для текстуры
    vec4 textureColor = texture2D(u_texture, v_texcoord);

    gl_FragColor = 2.0 * (ambient + diffuse) * textureColor + specular * one;

    //    vec4 lightColor = (ambient + diffuse + specular) * one;
    //    gl_FragColor = mix(lightColor, v_color, 0.6);
}
