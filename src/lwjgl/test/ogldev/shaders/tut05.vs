#version 330

layout (location = 0) in vec3 Position;

uniform float gScale;

void main(){
    gl_Position = vec4(Position * gScale, 1.0);
}
