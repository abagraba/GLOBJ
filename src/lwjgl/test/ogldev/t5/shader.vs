#version 430

layout (location = 0) in vec3 Position;

uniform sampleBlock {
	float gScale;
	float hScale;
	float iScale;
};
uniform sampleBlockx {
	float uScale;
	float vScale;
	float nScale;
};

layout (location = 0) uniform float i;
layout (location = 3) uniform float y;
layout (location = 9) uniform float t;

void main() {
    gl_Position = vec4(gScale * Position, 1.0 + i * gScale + hScale + y * t);
}
