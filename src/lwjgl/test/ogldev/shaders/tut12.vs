#version 330

layout (location = 0) in vec4 Position;

uniform mat4 mMatrix;
uniform mat4 vMatrix;
uniform mat4 pMatrix;

out vec4 Color;

void main() {	
	vec4 worldspace = mMatrix * Position;
	vec4 cameraspace = vMatrix * mMatrix * Position;
	vec4 projspace = pMatrix * vMatrix * mMatrix * Position;

	gl_Position =  projspace;
    Color = clamp(Position, 0.0, 1.0);
}
